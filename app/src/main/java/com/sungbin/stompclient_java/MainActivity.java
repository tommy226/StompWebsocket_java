package com.sungbin.stompclient_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sungbin.stompclient_java.adapter.MessageAdapter;
import com.sungbin.stompclient_java.databinding.ActivityMainBinding;
import com.sungbin.stompclient_java.vo.MessageVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity {
    public static final int MY = 1000;                                                 // 메세지 전송자 구분
    public static final int OTHER = 1001;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SOCKET_URL = "wss://Your Url/websocket";   // http = ws로 시작하며 https = wss로 시작
    private static final String MSSAGE_DESTINATION= "/socket/message";                  // 소켓 주소

    private static final int MESSAGE = 0;                                               // 핸들러 메세지 타입

    private ActivityMainBinding binding;

    private StompClient mStompClient;

    private Gson gson = new Gson();

    private Handler mHandler;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecycler();

        String room =  getIntent().getStringExtra("room");
        String name = getIntent().getStringExtra("name");

        connectStomp(room);

        binding.sendBtn.setOnClickListener(v -> {                               // 메세지 전송
            String content = binding.messageEdit.getText().toString();

            if (!TextUtils.isEmpty(content)) {
                String time = new SimpleDateFormat("k:mm").format(new Date(System.currentTimeMillis()));    // 현재 시간
                sendMessage(name, content, time, room);
                binding.messageEdit.setText("");
            }else{
                Toast.makeText(this, "메세지를 입력해주세요", Toast.LENGTH_SHORT).show();
            }

        });

        mHandler = new Handler(Looper.getMainLooper(), msg -> {                                         // stomp 메세지 핸들러 처리
           switch (msg.what){
               case MESSAGE:
                   Log.d(TAG, "Messsage in :"+ msg.obj.toString());
                   MessageVO messageVO = gson.fromJson(String.valueOf(msg.obj), MessageVO.class);

                   if(messageVO.getName().equals(name)) messageVO.setType(MY);      // 메세지 객체 타입 비교
                   else messageVO.setType(OTHER);

                   adapter.add(messageVO);
                   binding.recyclerview.smoothScrollToPosition(adapter.getItemCount());
                   break;
           }
           return false;
        });
    }

    private void initRecycler(){
        adapter = new MessageAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(adapter);
    }

    private void connectStomp(String room){
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.i(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.i(TAG, "Error", lifecycleEvent.getException());
                    connectStomp(room);
                    break;

                case CLOSED:
                    Log.i(TAG, "Stomp connection closed");
                    break;

                case FAILED_SERVER_HEARTBEAT:
                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT");
                    break;
            }
        });

        mStompClient.topic(MSSAGE_DESTINATION + "/" + room).subscribe(stompMessage -> {     // 구독 메세지 handler 전달
            Log.d(TAG, "receive messageData :" + stompMessage.getPayload());
            Message msg = new Message();
            msg.what = MESSAGE;
            msg.obj = stompMessage.getPayload();
            mHandler.sendMessage(msg);
        });

        mStompClient.connect();
    }

    private void sendMessage(String name, String content, String time, String room){                                 // 구독 하는 방과 같은 주소로 메세지 전송
        MessageVO messageVO = new MessageVO(name, content, time);
        String messageJson = gson.toJson(messageVO);

        mStompClient.send(MSSAGE_DESTINATION + "/" + room, messageJson).subscribe();
        Log.d(TAG, "send messageData : " + messageJson);
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}