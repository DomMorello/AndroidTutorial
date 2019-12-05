package com.example.moviefiendver2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FragGongjoInfo extends Fragment {

    Button likeButton;  //좋아요 이미지
    TextView likeCountView; //좋아요 숫자
    Button dislikeButton;   //싫어요 이미지
    TextView dislikeCountView;  //싫어요 숫자
    //        ScrollView scrollView;
    Button writeCommentButton;  //작성하기 버튼
    CommentAdapter commentAdapter;
    ArrayList<CommentItem> commentItems = new ArrayList<>();

    boolean likeState = false;
    boolean dislikeState = false;
    int likeCount = 1;  //좋아요 수
    int dislikeCount = 1;   //싫어요 수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gongjo_info, container, false);

        ListView commentListView = rootView.findViewById(R.id.comment_listview);

        commentAdapter = new CommentAdapter();

        commentAdapter.addItem(new CommentItem("DomMorel**", "아주그지같군요!", 4.5f));
        commentAdapter.addItem(new CommentItem("BomnieK**", "정말 환상적인 영화에요! 꼭 보세요!", 3.0f));
        commentAdapter.addItem(new CommentItem("estelleCh**", "기존 영화와는 아주 다른 느낌입니다. 되게 독특한 영화이니 한 번쯤 봐도 시간 아깝지 않을 것 같아요ㅎㅎ", 5.0f));
        commentAdapter.addItem(new CommentItem("haha**", "연기 개 어색함.. 근데 나오는 사람들이 약간 스타일리시하긴 하네요", 1.5f));
        commentAdapter.addItem(new CommentItem("zuzud**", "음....노코멘트 하겠습니다.", 2.5f));

        commentListView.setAdapter(commentAdapter);

        //작성하기 버튼을 눌렀을 때
        writeCommentButton = rootView.findViewById(R.id.writeCommentButton);
        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeCommentIntent = new Intent(getActivity(), WriteCommentActivity.class);
                startActivityForResult(writeCommentIntent, 102); //작성하기 activity실행
            }
        });

        //모두보기 버튼을 눌렀을 때 한줄평 전체 listview를 보여주는 메소드
        final Button readMoreButton = rootView.findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readMoreIntent = new Intent(getActivity(), ReadMoreActivity.class);

                readMoreIntent.putExtra("list", commentItems);   //그 안에 들어있는 객체들은 Parcelable 구현해서 넘겨줌.
                startActivityForResult(readMoreIntent, 104);  //새로운 activity에서 작성하기를 누른 후 리스트정보를 받아와야 하기 때문에 ForResult 테스트중
            }
        });


        likeButton = rootView.findViewById(R.id.likeButton);
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
        likeCountView = rootView.findViewById(R.id.likeCountView);

        dislikeButton = rootView.findViewById(R.id.dislikeButton);
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
        dislikeCountView = rootView.findViewById(R.id.dislikeCountView);

        return rootView;
    }//onCreatView 메서드 끝

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
            CommentItemView commentItemView;
            //convertView가 있으면 그 자원을 다시 써서 메모리관리를 효과적으로 한다
            if (convertView == null) {
                commentItemView = new CommentItemView(getActivity());
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            CommentItem commentItem = commentItems.get(commentItems.size() - 1 - i);    //순서대로 나오지 않고 역순으로 나오게 하려고 사이즈-1에서 i를 뺌
            //이렇게 하면 새로 등록한 한줄평이 제일 위로 나올 수 있게 됨.
            //이거 지렸다...
            commentItemView.setComment(commentItem.getComment());   //뷰에서 comment내용을 설정함.
            commentItemView.setRating(commentItem.getRating()); //뷰에서 별점을 설정함.
            commentItemView.setId(commentItem.getId()); //id를 설정함.

            return commentItemView;
        }
    }//어댑터 클래스 끝

    //한줄평 작성과 평점 데이터를 불러오는 것 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 102) {
            float rating = intent.getFloatExtra("rating", 0.0f);
            String comment = intent.getStringExtra("comment");
            if (rating > 0.0 && comment.length() > 0) {
                commentAdapter.addItem(new CommentItem("hkkim93", comment, rating)); //한줄평 리스트에 추가하기
                commentAdapter.notifyDataSetChanged();  //변화가 있으면 갱신해라.
                Snackbar.make(likeButton, "한줄평이 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
            }
        }
        //모두보기에서 결과를 가져올 때
        if (requestCode == 104) {
            commentItems = (ArrayList<CommentItem>) intent.getSerializableExtra("list");
            commentAdapter.notifyDataSetChanged();  //갱신된 list로 어댑터한테 갱신하라고 말해줌
        }

    }

}
