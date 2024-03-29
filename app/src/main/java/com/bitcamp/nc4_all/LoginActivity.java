package com.bitcamp.nc4_all;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bitcamp.nc4_all.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityLoginBinding binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());

        // findViewById를 여기서 호출하여 버튼을 찾습니다.
        join = findViewById(R.id.login_sign_up_btn);

        /// 클릭 이벤트 리스너 설정
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 액티비티로 이동하는 코드를 작성합니다.
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}