package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        final TextView textView = (TextView)findViewById(R.id.user_name);
        textView.setText(getIntent().getStringExtra("id"));

        Button button = (Button)findViewById(R.id.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("logout_user",textView.getText().toString()+"님이 로그아웃 하셨습니다.");
                setResult(20,intent);
                finish();
            }
        });
    }
}
