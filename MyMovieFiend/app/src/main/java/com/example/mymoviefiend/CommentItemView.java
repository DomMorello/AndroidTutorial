package com.example.mymoviefiend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mymoviefiend.R;

public class CommentItemView extends LinearLayout {

    TextView commentContent;
    RatingBar commentRatingBar;
    TextView userId;

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