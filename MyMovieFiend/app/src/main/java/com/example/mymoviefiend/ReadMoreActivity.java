package com.example.mymoviefiend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadMoreActivity extends AppCompatActivity {

    MainActivity.CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        ListView commentListView = findViewById(R.id.comment_listview);
        commentListView.setAdapter(adapter);
    }

    private void processIntent(Intent intent){
        if(intent != null){
            ArrayList<CommentItem> items = (ArrayList<CommentItem>) intent.getSerializableExtra("list");    //list를 넘겨받았는데 이걸로 어떻게 화면에 보이게하지?
            Toast.makeText(getApplicationContext(),"전달받은 리스트개수: "+items.size(), Toast.LENGTH_SHORT).show();
        }
    }
}

//모두 보기 클릭시 리스트뷰에 메인액티비티에 있는 것을 전부 보여주기 위한 어댑터
/*class InCommentAdapter extends BaseAdapter {

    ArrayList<CommentItem> inCommentItems = new ArrayList<>();

    @Override
    public int getCount() {
        return inCommentItems.size();
    }

    @Override
    public Object getItem(int i) {
        return inCommentItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CommentItemView commentItemView;
        //convertView가 있으면 그 자원을 다시 써서 메모리관리를 효과적으로 한다
        if (convertView == null) {
            commentItemView = new CommentItemView();
        } else {
            commentItemView = (CommentItemView) convertView;
        }

        CommentItem commentItem = inCommentItems.get(inCommentItems.size()-1-i);    //순서대로 나오지 않고 역순으로 나오게 하려고 사이즈-1에서 i를 뺌
        //이렇게 하면 새로 등록한 한줄평이 제일 위로 나올 수 있게 됨.
        //이거 지렸다...
        commentItemView.setComment(commentItem.getComment());   //뷰에서 comment내용을 설정함.
        commentItemView.setRating(commentItem.getRating()); //뷰에서 별점을 설정함.

        return commentItemView;
    }
}*/
