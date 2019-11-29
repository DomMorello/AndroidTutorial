package com.example.mysummary3rename;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.mysummary3rename.R;

public class CommentWriteActivity extends AppCompatActivity {
    RatingBar ratingBar;
    EditText contentsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        ratingBar = findViewById(R.id.ratingBar);
        contentsInput = findViewById(R.id.contentsInput);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMain();
            }
        });

        Intent intent = getIntent();
        processIntent(intent);

    }

    private void processIntent(Intent intent){
        if(intent != null){
            float rating = intent.getFloatExtra("rating",0.0f); //main activity에서 보낸 값을 받는다.
            ratingBar.setRating(rating);    //main에서 받은 값을 this activity에 적용한다.
        }
    }

    public void returnToMain(){
        String contents = contentsInput.getText().toString();

        Intent intent = new Intent();   //Intent에서 시스템에 딱히 아무 것도 하지 말고 extra data만 전달할 목적이라면 빈 Intent를 만들어서 전달할 수 있다.
        intent.putExtra("contents",contents);

        setResult(RESULT_OK,intent);    //이 intent를 전달한다. 이를 Main activity(전달받는 activity)에서 onActivityResult 메서드로 받는다.


        finish();
    }
}
