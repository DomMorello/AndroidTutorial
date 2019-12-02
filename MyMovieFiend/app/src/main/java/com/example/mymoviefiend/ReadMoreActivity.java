package com.example.mymoviefiend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ReadMoreActivity extends AppCompatActivity {

    InCommentAdapter inCommentAdapter;  //내부클래스 어댑터를 사용하기 위해 이 위치에 선언
    ArrayList<CommentItem> inCommentItems = new ArrayList<>();  //다른 메서드에서도 사용하기 위해 여기에 선언
    Button writeCommentButton;  //작성하기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        Intent passedIntent = getIntent();  //전달받은 인텐트를
        if(passedIntent != null){
            inCommentItems = (ArrayList<CommentItem>) passedIntent.getSerializableExtra("list"); //CommentItem을 Parcelable 구현해서 받아냄
        }

        inCommentAdapter = new InCommentAdapter();  //이게 없어서 안됐었다. 어댑터를 쓰려면 어댑터 객체가 있어야지 당연히!
        ListView commentListView = findViewById(R.id.comment_listview);
        commentListView.setAdapter(inCommentAdapter);   //리스트뷰를 어댑터에 연결해준다.

        //작성하기를 눌렀을 때
        writeCommentButton = findViewById(R.id.writeCommentButton);
        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeCommentIntent = new Intent(getApplicationContext(), WriteCommentActivity.class);
                startActivityForResult(writeCommentIntent, 103); //작성하기 activity실행
                //작성하기 액티비티를 실행시키면서 결과를 기대한다.
            }
        });
        /*ForResult였으니까 list에 변화가 없으면 그냥 빈 결과를 세팅하고 메인으로 전달해준다 test*/
        Intent emptyIntent = new Intent(getApplicationContext(),MainActivity.class);
        setResult(RESULT_OK,emptyIntent);
    }

    //작성하기 액티비티에서 받은 결과로 list에 추가한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 103) {
            float rating = intent.getFloatExtra("rating", 0.0f);
            String comment = intent.getStringExtra("comment");
            if (rating > 0.0 && comment.length() > 0) {
                inCommentAdapter.addItem(new CommentItem("hkkim93", comment, rating)); //한줄평 리스트에 추가하기
                inCommentAdapter.notifyDataSetChanged();  //변화가 있으면 갱신해라.
                Snackbar.make(writeCommentButton, "한줄평이 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
                //추가를 해서 모두보기 화면에서 갱신된 list가 보이도록 한 이후에
                //뒤로가기 버튼을 누르면 메인 액티비티로 result를 설정해서 list를 부가데이터로 보내준다.
            }
        }
    }

    //뒤로가기 버튼을 눌렀을 때 액티비티 종료 오버라이드한 이유? ForResult라서 Result값을 세팅해주기 위해
    @Override
    public void onBackPressed() {
//        super.onBackPressed();    //이것을 없앤 이유는 super를 통해 액티비티를 종료해버리기 때문에 setResult가 효과가 없어짐. (내 생각)
        //뒤로가기 버튼을 누르면 메인 액티비티로 result를 설정해서 list를 부가데이터로 보내준다.
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("list",inCommentItems);
        setResult(Activity.RESULT_OK, intent);  //startActivityForResult로 불러냈기 때문에 결과를 세팅해줘야 한다.
        finish(); 
    }

    //모두 보기 클릭시 리스트뷰에 메인액티비티에 있는 것을 전부 보여주기 위한 어댑터
    class InCommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return inCommentItems.size();
        }

        public void addItem(CommentItem commentItem) {
            inCommentItems.add(commentItem);  //ArrayList에 추가한다.
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
                commentItemView = new CommentItemView(getApplicationContext());
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            CommentItem commentItem = inCommentItems.get(inCommentItems.size()-1-i); //순서대로 나오지 않고 역순으로 나오게 하려고 사이즈-1에서 i를 뺌
            //이렇게 하면 새로 등록한 한줄평이 제일 위로 나올 수 있게 됨.
            //이거 지렸다...
            commentItemView.setComment(commentItem.getComment());   //뷰에서 comment내용을 설정함.
            commentItemView.setRating(commentItem.getRating()); //뷰에서 별점을 설정함.
            commentItemView.setId(commentItem.getId()); //id를 설정함.

            return commentItemView;
        }
    }
}


