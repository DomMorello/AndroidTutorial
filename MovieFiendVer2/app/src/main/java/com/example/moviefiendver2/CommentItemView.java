package com.example.moviefiendver2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

public class CommentItemView extends LinearLayout {

    TextView commentContent;
    RatingBar commentRatingBar;
    TextView userId;
    TextView time;
    TextView recommendation;
    TextView recommendationNum;
    TextView border;
    Button call;

    public CommentItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.one_line_comment, this, true);

        commentContent = findViewById(R.id.comment_content);
        commentRatingBar = findViewById(R.id.commentRatingBar);
        userId = findViewById(R.id.user_id);
        time = findViewById(R.id.time);
        recommendation = findViewById(R.id.추천);
        recommendationNum = findViewById(R.id.추천수);
        border = findViewById(R.id.칸막이);
        call = findViewById(R.id.신고하기버튼);

        //리스트뷰 안에 있는 뷰객체들의 textColor를 바꿔줌
        commentContent.setTextColor(Color.WHITE);
        userId.setTextColor(Color.WHITE);
        time.setTextColor(Color.GRAY);
        recommendation.setTextColor(Color.GRAY);
        recommendationNum.setTextColor(Color.GRAY);
        border.setTextColor(Color.GRAY);
        call.setTextColor(Color.GRAY);

    }

    public void setComment(String comment) {
        commentContent.setText(comment);    //텍스트뷰에 작성한 comment가 나오게 한다.
    }

    public void setRating(float rating) {
        commentRatingBar.setRating(rating); //RatingBar에 입력한 rating이 색칠되게 한다.
    }

    public void setId(String id) {
        userId.setText(id); //아이디를 텍스트뷰에 보이게 한다.
    }
}