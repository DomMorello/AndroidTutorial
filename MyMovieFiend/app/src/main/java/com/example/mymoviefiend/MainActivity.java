package com.example.mymoviefiend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoviefiend.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button likeButton;
    TextView likeCountView;
    Button dislikeButton;
    TextView dislikeCountView;
    ScrollView scrollView;

    boolean likeState = false;
    boolean dislikeState = false;
    int likeCount = 1;  //좋아요 수
    int dislikeCount = 1;   //싫어요 수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //모두보기 버튼을 눌렀을 때 스낵바 띄우기
        Button readMoreButton = findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "모두보기를 눌렀습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        //작성하기 버튼을 눌렀을 때 토스트메시지(내가 직접 디자인한 모양으로)를 띄우는 코드
        Button writeCommentButton = findViewById(R.id.writeCommentButton);
        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();  //레이아웃 인플레이터를 얻어와서
                View toastLayout = inflater.inflate(R.layout.toast_border, (ViewGroup) findViewById(R.id.toast_layout_root));   //내가 짠 레이아웃과 루트를 포함하여 인플레이트한다.
                TextView text = toastLayout.findViewById(R.id.text);    //TextView를 찾아서
                text.setText("작성하기를 눌렀습니다.");   //텍스트를 작성한다.

                Toast toast = new Toast(getApplicationContext());   //토스트 객체를 만들고
                toast.setGravity(Gravity.TOP, 0, 100);  //위치를 정한다
                toast.setDuration(Toast.LENGTH_SHORT);  //떠 있는 시간을 정하고
                toast.setView(toastLayout); //View를 설정한다
                toast.show();
            }
        });

        /*스크롤 뷰 안에서는 리스트뷰가 스크롤이 안 되는 경우가 발생
        이는 같은 기능이 충돌하여 스크롤뷰만 기능하는 오류인데
        이를 해결하기 위해서는 스크롤뷰의 스크롤 기능을 리스트뷰에 터치가 된 경우에는 기능을 멈추게 함으로써
        리스트뷰가 스크롤하는 기능을 가져와 리스트뷰 안에서 가능하게 하는 것이다.*/
//        scrollView = findViewById(R.id.scroll_view);
        ListView commentListView = findViewById(R.id.comment_listview);
//        commentListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        CommentAdapter commentAdapter = new CommentAdapter();

        commentAdapter.addItem(new CommentItem("DomMorello", "아주그지같군요!"));
        commentAdapter.addItem(new CommentItem("DomMorello", "정말 환상적인 영화에요! 꼭 보세요!"));
        commentAdapter.addItem(new CommentItem("DomMorello", "기존 영화와는 아주 다른 느낌입니다. 되게 독특한 영화이니 한 번쯤 봐도 시간 아깝지 않을 것 같아요ㅎㅎ"));
        commentAdapter.addItem(new CommentItem("DomMorello", "연기 개 어색함.. 근데 나오는 사람들이 약간 스타일리시하긴 하네요"));
        commentAdapter.addItem(new CommentItem("DomMorello", "음....노코멘트 하겠습니다."));

        commentListView.setAdapter(commentAdapter);

        likeButton = findViewById(R.id.likeButton);
        //likeButton을 눌렀을 때
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likeState) {
                    decrLikeCount();
                } else {
                    incrLikeCount();
                    if (dislikeState) {   //싫어요 버튼 눌러져있는 상태였다면 상태를 안 눌린 상태로 바꾸고 싫어요 숫자도 하나 줄인다.
                        decrDislikeCount();
                        dislikeState = !dislikeState;
                    }
                }

                likeState = !likeState; //클릭을 했기 때문에 상태가 눌린상태에서 안눌린상태로, 안눌린 상태에서 눌린 상태로 변해야 함.
            }
        });
        likeCountView = findViewById(R.id.likeCountView);

        dislikeButton = findViewById(R.id.dislikeButton);
        //dislikeButton을 눌렀을 때
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dislikeState) {
                    decrDislikeCount();
                } else {
                    incrDislikeCount();
                    if (likeState) {  //좋아요 버튼 눌러져있는 상태였다면 상태를 안 눌린 상태로 바꾸고 좋아요 숫자도 하나 줄인다.
                        decrLikeCount();
                        likeState = !likeState;
                    }
                }

                dislikeState = !dislikeState;   //클릭을 했기 때문에 상태가 눌린상태에서 안눌린상태로, 안눌린 상태에서 눌린 상태로 변해야 함.
            }
        });
        dislikeCountView = findViewById(R.id.dislikeCountView);
    }

    //좋아요 수를 증가
    public void incrLikeCount() {
        likeCount += 1; //좋아요 수를 하나 증가시키고
        likeCountView.setText(String.valueOf(likeCount));   //증가된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);  //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 누른상태이미지로 바꿔서 유지한다.
    }

    //좋아요 수를 감소
    public void decrLikeCount() {
        likeCount -= 1; //좋아요 수를 하나 감소시키고
        likeCountView.setText(String.valueOf(likeCount));   //감소된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.thumb_up_selector); //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 안눌린 상태로 바꿔서 유지한다. (클릭상태를 유지하면 눌린이미지가 보이게함.)
    }

    //싫어요 수를 증가
    public void incrDislikeCount() {
        dislikeCount += 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));
        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
    }

    //싫어요 수를 감소
    public void decrDislikeCount() {
        dislikeCount -= 1;
        dislikeCountView.setText(String.valueOf(dislikeCount));
        dislikeButton.setBackgroundResource(R.drawable.thumb_down_selector);
    }

    //한줄평 리스트를 보여주는 리스트뷰 어댑터
    class CommentAdapter extends BaseAdapter {

        ArrayList<CommentItem> commentItems = new ArrayList<>();    //CommentItem들을 담을 수 있는 ArrayList생성

        @Override
        public int getCount() {
            return commentItems.size();
        }

        public void addItem(CommentItem commentItem) {
            commentItems.add(commentItem);  //ArrayList에 추가한다.
        }

        @Override
        public Object getItem(int i) {
            return commentItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            CommentItemView commentItemView = null;
            //convertView가 있으면 그 자원을 다시 써서 메모리관리를 효과적으로 한다
            if (convertView == null) {
                commentItemView = new CommentItemView(getApplicationContext());
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            CommentItem commentItem = commentItems.get(i);
            commentItemView.setComment(commentItem.getComment());

            return commentItemView;
        }
    }
}
