package com.example.mymoviefiend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class WriteCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        //취소 버튼을 눌렀을 때
        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();   //아무 데이터를 저장하지 않고 액티비티를 종료한다. ??왜 여기 오류나지
            }
        });

        //저장 버튼을 눌렀을 때
        Button saveButton = findViewById(R.id.saveButton);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final EditText editText = findViewById(R.id.editText);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                float rating = ratingBar.getRating();
                intent.putExtra("rating", rating);  //작성한 한줄평에서 평점을 메인에 전달한다.
                String comment = editText.getText().toString(); //작성한 한줄평 내용을 메인에 전달한다.
                intent.putExtra("comment", comment);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

}
