package com.example.moviefiendver2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.moviefiendver2.MovieData.CommentItem;

import org.w3c.dom.Text;

public class CommentItemView extends LinearLayout {

    TextView commentContent;
    RatingBar commentRatingBar;
    TextView userId;
    TextView time;
    Button recommendation;
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
        recommendation = findViewById(R.id.comment_recommend);
        recommendationNum = findViewById(R.id.comment_recommend_num);
        border = findViewById(R.id.칸막이);
        call = findViewById(R.id.신고하기버튼);

        //리스트뷰 안에 있는 뷰객체들의 textColor를 바꿔줌
        commentContent.setTextColor(Color.WHITE);
        userId.setTextColor(Color.WHITE);
        time.setTextColor(Color.GRAY);
//        recommendation.setTextColor(Color.GRAY);  눌렀을 때 색깔바뀌는 selector적용하려고 주석처리함
        recommendationNum.setTextColor(Color.GRAY);
        border.setTextColor(Color.GRAY);
        call.setTextColor(Color.GRAY);

        recommendation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 서버에 추천을 저장하는 코드 */
                //id값이 있어야 추천을 할 수 있다. commentItem에 id값을 세팅을 하긴 했는데 어떻게 여기서 사용할 수 있지..?

            }
        });




    }

    public void setCommentContent(String data){
        commentContent.setText(data);
    }

    public void setCommentRatingBar(float data){
        commentRatingBar.setRating(data);
    }

    public void setUserId(String data){
        userId.setText(data);
    }

    public void setTime(String data){
        time.setText(data);
    }

    public void setRecommendationNum(String data){
        recommendationNum.setText(data);    //int로 받으면 오류나려나?
    }


}