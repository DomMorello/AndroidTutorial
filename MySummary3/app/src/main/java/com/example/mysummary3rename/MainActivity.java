package com.example.mysummary3rename;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mysummary3rename.R;

public class MainActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView outputView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar = findViewById(R.id.ratingBar);
        outputView = findViewById(R.id.outputView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentWriteActivity();
            }
        });
    }

    public void showCommentWriteActivity(){
        float rating = ratingBar.getRating();  //사용자가 입력한 값을 불러온다.

        Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
        intent.putExtra("rating",rating);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 101){
            if(intent != null){
                String contents = intent.getStringExtra("contents");    //전달받은 인텐트 부가 데이터를
                outputView.setText(contents);   //ouputView 텍스트뷰에 텍스트로 보여준다.
            }
        }
    }
}
