package com.bitcamp.nc4_all;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bitcamp.nc4_all.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        // 데이터 바인딩을 사용하여 레이아웃 인플레이트
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // 데이터 바인딩을 사용하여 클릭 리스너 설정
        binding.loginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpActivity로 이동
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}