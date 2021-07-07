package com.sungbin.stompclient_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.sungbin.stompclient_java.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SOCKET_URL = "wss://varlos-smartwork.com/websocket";   // http = ws로 시작하며 https = wss로 시작
    private static final String REQUEST_MESSAGE = "/socket/message/";                  // 소켓 주소
    private static final int MESSAGE = 0;

    private ActivityMainBinding binding;

    private StompClient mStompClient;

    private ArrayBlockingQueue<HashMap<Integer, String>> gWebSocketData = new ArrayBlockingQueue<>(10);

    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectStomp();

        sendMessage("헬로 성빈");
    }

    private void connectStomp(){
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.i(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.i(TAG, "Error", lifecycleEvent.getException());

                    break;

                case CLOSED:
                    Log.i(TAG, "Stomp connection closed");

                    break;

                case FAILED_SERVER_HEARTBEAT:
                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT");
                    break;
            }
        });

        mStompClient.topic("/socket/message" + "/" + "hello").subscribe(stompMessage -> {
            Log.d(TAG, "receive messageData :" + stompMessage.getPayload());
            HashMap<Integer, String> stompData = new HashMap<>();
            stompData.put(MESSAGE, stompMessage.getPayload());
            gWebSocketData.put(stompData);
        });

        mStompClient.connect();
    }

    private void sendMessage(String message){
        MessageVO messageVO = new MessageVO("문성빈", message);
        String messageData = gson.toJson(messageVO);

        mStompClient.send("/message" + "/" + "hello", messageData).subscribe();
        Log.d(TAG, "send messageData : " + messageData);
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        super.onDestroy();
    }
}