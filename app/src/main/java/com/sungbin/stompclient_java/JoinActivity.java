package com.sungbin.stompclient_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.sungbin.stompclient_java.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.joinroomBtn.setOnClickListener(v -> {
            String room = binding.joinroomEdit.getText().toString();
            String name = binding.nameEdit.getText().toString();

            if(!TextUtils.isEmpty(room) && !TextUtils.isEmpty(name)){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("room", room);
                intent.putExtra("name", name);
                startActivity(intent);
            }else{
                Toast.makeText(this, "모두 입력 해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}