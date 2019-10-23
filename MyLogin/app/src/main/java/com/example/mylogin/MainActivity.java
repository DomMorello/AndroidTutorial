package com.example.mylogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.loginbutton);

        final EditText id = (EditText)findViewById(R.id.id);
        final EditText password = (EditText)findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password.getText().toString().equals("1234")){
                    Intent intent = new Intent(view.getContext(),NextActivity.class);
                    intent.putExtra("id",id.getText().toString());
                    startActivityForResult(intent,10);
                }else{
                    Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 20) {
            //requestCode가 10이고 resultCode가 20이면 이 코드는 로그아웃한 사람이 누군지 알려주는 텍스트뷰를 작성한다
            TextView textView = (TextView) findViewById(R.id.logout_user_textview);
            textView.setText(data.getStringExtra("logout_user"));
        }
    }
}
