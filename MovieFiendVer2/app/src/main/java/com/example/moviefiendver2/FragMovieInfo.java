package com.example.moviefiendver2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefiendver2.MovieData.MovieList;
import com.example.moviefiendver2.MovieData.ResponseInfo;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.helper.ImageLoadTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class FragMovieInfo extends Fragment {

    Button likeButton;  //좋아요 이미지
    Button dislikeButton;   //싫어요 이미지
    Button writeCommentButton;  //작성하기 버튼
    CommentAdapter commentAdapter;
    ArrayList<CommentItem> commentItems = new ArrayList<>();
    String movieTitle;  //TextView에서 얻어온 title을 저장하기 위한 String변수
    byte[] byteArray;   //이미지를 보내줄 때 사용됨
    int position;   //영화API 서버에서 id값을 구분하기 위한 영화 인덱스

    ImageView poster;   //영화 포스터 뷰
    ImageView grade;    //몇세 관람가 이미지
    TextView title; //제목을 넘기기 위해 선언 //영화 제목
    TextView date;  //개봉일자
    TextView genre; //장르
    TextView duration; //러닝타임
    TextView like;  //좋아요 개수
    TextView dislike;   //싫어요 개수
    TextView reservation_grade; //예매율 순위
    TextView reservation_rate; //예매율
    TextView audience_rating;   //평점
    RatingBar infoRatingBar;    //상세화면의 레이팅바
    TextView audience;  //누적관객수
    TextView synopsis;  //줄거리
    TextView director;  //감독
    TextView actor; //출연

    boolean likeState = false;
    boolean dislikeState = false;
    int likeCount = 1;  //좋아요 수
    int dislikeCount = 1;   //싫어요 수

    MovieList movieList;    //다른 액티비티에 넘겨주려면 다른 메소드에서도 사용해야 하므로 여기에 선언

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position", 0)+1;  //position정보를 MainFragmentMovie에서 객체를 생성할 때 매개변수로 받아서 상세보기 눌렀을 때
        //이 프래그먼트까지 보내준다. +1 인 이유는 서버상 id정보가 0부터가 아닌 1부터이기 때문이다.
        Log.d("FragMovieInfo","MainFragmentMovie에서 넘어온position + 1 : "+position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_info, container, false);

        ListView commentListView = rootView.findViewById(R.id.comment_listview);

        grade = rootView.findViewById(R.id.info_grade);
        poster = rootView.findViewById(R.id.info_poster);
        title = rootView.findViewById(R.id.info_title);
        date = rootView.findViewById(R.id.info_date);
        genre = rootView.findViewById(R.id.info_genre);
        duration = rootView.findViewById(R.id.info_duration);
        like = rootView.findViewById(R.id.info_like);
        dislike = rootView.findViewById(R.id.info_dislike);
        reservation_grade = rootView.findViewById(R.id.info_reservation_grade);
        reservation_rate = rootView.findViewById(R.id.info_reservation_rate);
        audience_rating = rootView.findViewById(R.id.info_audience_rating);
        infoRatingBar = rootView.findViewById(R.id.info_ratingBar);
        audience = rootView.findViewById(R.id.info_audience);
        synopsis = rootView.findViewById(R.id.info_synopsis);
        director = rootView.findViewById(R.id.info_director);
        actor = rootView.findViewById(R.id.info_actor);

        //영화 상세정보를 서버에서 얻어오는 메소드
        requestMovieInfo();

        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());
        }

        movieTitle = title.getText().toString();    //영화 제목을 작성하기 액티비티에 넘겨주기 위해 얻어옴.



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
                writeCommentIntent.putExtra("title",movieList.result.get(0).title);    //작성하기 activity에 영화제목을 넘겨줌
                //이미지 보내주기
                writeCommentIntent.putExtra("integer", 300);
                writeCommentIntent.putExtra("double", 3.141592 );
                writeCommentIntent.putExtra("image", byteArray);

                startActivityForResult(writeCommentIntent, 102); //작성하기 activity실행
            }
        });

        //모두보기 버튼을 눌렀을 때 한줄평 전체 listview를 보여주는 메소드
        final Button readMoreButton = rootView.findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readMoreIntent = new Intent(getActivity(), ReadMoreActivity.class);
                readMoreIntent.putExtra("title",movieList.result.get(0).title);    //모두보기 activity에 영화제목을 넘겨줌
                readMoreIntent.putExtra("list", commentItems);   //그 안에 들어있는 객체들은 Parcelable 구현해서 넘겨줌.
                //이미지 보내주기
                readMoreIntent.putExtra("integer", 300);
                readMoreIntent.putExtra("double", 3.141592 );
                readMoreIntent.putExtra("image", byteArray);

                startActivityForResult(readMoreIntent, 104);  //새로운 activity에서 작성하기를 누른 후 리스트정보를 받아와야 하기 때문에 ForResult
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
        like = rootView.findViewById(R.id.info_like);

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
        dislike = rootView.findViewById(R.id.info_dislike);

        return rootView;
    }//onCreatView 메서드 끝

    public void requestMovieInfo(){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";  //영화 상세정보 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FragMainInfo","상세보기 화면에서 서버로부터 응답 받음: " + response);

                        //Dday얻는 메소드가 사용돼서 parseEception 예외처리
                        try {
                            processResponse(response);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FragMainInfo","에러 발생! " + error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = Integer.toString(position);
                params.put("id",id);    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화정보가 넘어온다.
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("FragMovieInfo","영화상세정보 서버에 요청함");

    }

    public void processResponse(String response) throws ParseException {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if(info.code == 200){
            movieList = gson.fromJson(response, MovieList.class);
            Log.d("FragMovieInfo","서버에서 얻어온 데이터 감독: " + movieList.result.get(0).director);  //result에 id값으로 한 영화만 받아오기 때문에 서버상 list size가 1이다.
                                                                                                                //그래서 인덱스가 0인 데이터만 접근할 수 있다.
            /* 포스터 이미지 받아와서 해야됨 */
            title.setText(movieList.result.get(0).title);
            date.setText(movieList.result.get(0).date.replace("-",". ")+" 개봉"); //2017-10-1 을 2017. 10. 1 로 바꿔줌.
            genre.setText(movieList.result.get(0).genre);
            duration.setText(movieList.result.get(0).duration+"분");
            like.setText(movieList.result.get(0).like+"");
            dislike.setText(movieList.result.get(0).dislike+"");
            reservation_grade.setText(movieList.result.get(0).reservation_grade+"위");
            reservation_rate.setText(movieList.result.get(0).reservation_rate+" %");
            audience_rating.setText(movieList.result.get(0).audience_rating+" 점");
            infoRatingBar.setRating((movieList.result.get(0).audience_rating)/2);   //레이팅바 서버 평점 정보로 표시, 2로 나눠야 별 색깔이 수치에 맞게 채워짐
            DecimalFormat formatter = new DecimalFormat("###,###"); //숫자 사이에 , 를 넣는 코드
            audience.setText(formatter.format(movieList.result.get(0).audience)+"명");
            synopsis.setText(movieList.result.get(0).synopsis);
            director.setText(movieList.result.get(0).director);
            actor.setText(movieList.result.get(0).actor);
            ImageLoadTask imageLoadTask = new ImageLoadTask(movieList.result.get(0).thumb, poster);   //클래스 내부에 set하게 정의해 놓음.
            imageLoadTask.execute();

            //서버에서 데이터가 12,15,19이냐에 따라 몇세 관람가 이미지를 다르게 설정한다.
            switch (movieList.result.get(0).grade){
                case 12: grade.setImageResource(R.drawable.ic_12);
                break;
                case 15: grade.setImageResource(R.drawable.ic_15);
                break;
                case 19: grade.setImageResource(R.drawable.ic_19);
                break;
                default: grade.setImageResource(R.drawable.announcement);
            }

            //이미지를 전달하기 위해 코드 작성(이미지 축소) -> 여기서 축소를 해줘야 서버에서 받아온 파일을 축소해서 보낼 수가 있다.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable)grade.getDrawable()).getBitmap();
            float scale = (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getHeight() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();


        }
    }

    //좋아요 수를 증가
    public void incrLikeCount() {
        likeCount += 1; //좋아요 수를 하나 증가시키고
        like.setText(String.valueOf(likeCount));   //증가된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);  //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 누른상태이미지로 바꿔서 유지한다.
    }

    //좋아요 수를 감소
    public void decrLikeCount() {
        likeCount -= 1; //좋아요 수를 하나 감소시키고
        like.setText(String.valueOf(likeCount));   //감소된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.thumb_up_selector); //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 안눌린 상태로 바꿔서 유지한다. (클릭상태를 유지하면 눌린이미지가 보이게함.)
    }

    //싫어요 수를 증가
    public void incrDislikeCount() {
        dislikeCount += 1;
        dislike.setText(String.valueOf(dislikeCount));
        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
    }

    //싫어요 수를 감소
    public void decrDislikeCount() {
        dislikeCount -= 1;
        dislike.setText(String.valueOf(dislikeCount));
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
