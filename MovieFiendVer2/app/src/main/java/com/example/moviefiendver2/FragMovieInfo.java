package com.example.moviefiendver2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviefiendver2.MovieData.CommentItem;
import com.example.moviefiendver2.MovieData.CommentResponse;
import com.example.moviefiendver2.MovieData.LikeResponse;
import com.example.moviefiendver2.MovieData.MovieResponse;
import com.example.moviefiendver2.MovieData.RecyclerItem;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.helper.NetworkStatus;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FragMovieInfo extends Fragment {

    ScrollView scrollView;
    Button likeButton;  //좋아요 이미지
    Button dislikeButton;   //싫어요 이미지
    Button writeCommentButton;  //작성하기 버튼
    CommentAdapter commentAdapter;
    ArrayList<CommentItem> commentItems = new ArrayList<>();
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
    String likeyn;  //좋아요 버튼을 눌렀을 때 서버에 요청할 parameter
    String dislikeyn;   //싫어요 버튼을 눌렀을 때 서버에 요청할 parameter

    MovieResponse movieResponse;    //JAON 다른 액티비티에 넘겨주려면 다른 메소드에서도 사용해야 하므로 여기에 선언
    CommentResponse commentResponse;    //JSON 다른 액티비티에 넘겨주려면 다른 메소드에서도 사용해야 하므로 여기에 선언

    RecyclerView recyclerView;  //갤러리 부분 recyclerView
    GalleryAdapter galleryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position", 0) + 1;
        //position정보를 MainFragmentMovie에서 객체를 생성할 때 매개변수로 받아서 상세보기 눌렀을 때
        //이 프래그먼트까지 보내준다. +1 인 이유는 서버상 id정보가 0부터가 아닌 1부터이기 때문이다.
        Log.d("FragMovieInfo", "MainFragmentMovie에서 넘어온 position + 1 : " + position);

    }

    //onResume에 한 이유: 한줄평을 작성하고 나서 프래그먼트로 다시 돌아왔을 때 작성한 최신화된 한줄평을 보여주기 위해서는
    //프래그먼트가 화면에 보여질 때마다 최신화된 서버정보를 가져오기 위해서 onResume에 작성함.
    @Override
    public void onResume() {
        super.onResume();

        scrollView.smoothScrollTo(0, 0); //프래그먼트가 화면에 보여질때마다 스크롤뷰의 최초 위치가 최상단으로 고정되게 함

        //인터넷이 연결돼있으면 서버에서 정보를 가져와라.
        if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI) {
            //영화 상세정보를 서버에서 얻어오는 메소드
            requestMovieInfo();
            //한줄평 정보를 서버에서 얻어오는 메소드
            requestCommentList();

            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getContext());
            }
            //인터넷에 연결돼있지 않으면 데이터베이스에서 데이터를 가져와 뷰에 보여줘라.
        } else {
            Log.d("FargMovieInfo", "인터넷 연결 안 돼있을 때 상세화면 데이터베이스에서 가져온거임.");
            //selectData 메소드에서 테이블이 데이터베이스에 이미 있으면 true를 반환, 아니면 table이 false를 반환하므로 테이블이 없으면 아무것도 하지 마라.
            if (AppHelper.selectData(AppHelper.MOVIE_INFO, position, 0)) {
                AppHelper.selectData(AppHelper.MOVIE_INFO, position, 0);
                title.setText(AppHelper.info_title);
                date.setText(AppHelper.info_date.replace("-", ". ") + " 개봉");
                genre.setText(AppHelper.info_genre);
                duration.setText(AppHelper.info_duration + "분");
                like.setText(AppHelper.info_like + "");
                dislike.setText(AppHelper.info_dislike + "");
                reservation_grade.setText(AppHelper.info_reservation_grade + "위");
                reservation_rate.setText(AppHelper.info_reservation_rate + " %");
                audience_rating.setText(AppHelper.info_audience_rating + " 점");
                infoRatingBar.setRating(AppHelper.info_audience_rating / 2);  //레이팅바 서버 평점 정보로 표시, 2로 나눠야 별 색깔이 수치에 맞게 채워짐
                DecimalFormat formatter = new DecimalFormat("###,###");
                audience.setText(formatter.format(AppHelper.info_audience) + "명");
                synopsis.setText(AppHelper.info_synopsis);
                director.setText(AppHelper.info_director);
                actor.setText(AppHelper.info_actor);
                //데이터베이스에서 grade 데이터가 12,15,19이냐에 따라 몇세 관람가 이미지를 다르게 설정한다.
                switch (AppHelper.info_grade) {
                    case 12:
                        grade.setImageResource(R.drawable.ic_12);
                        break;
                    case 15:
                        grade.setImageResource(R.drawable.ic_15);
                        break;
                    case 19:
                        grade.setImageResource(R.drawable.ic_19);
                        break;
                    default:
                        grade.setImageResource(R.drawable.announcement);
                }
                //IamgeLoadTask는 url을 이용하므로 인터넷이 필요하다. 그래서 데이터베이스에서 url을 가져오는 방법으로는 할 수 없다.
//                ImageLoadTask imageLoadTask = new ImageLoadTask(AppHelper.info_thumb, poster);   //클래스 내부에 set하게 정의해 놓음.
//                imageLoadTask.execute();
                //인터넷이 없을 때도 몇세 관람가 이미지는 축소해서 보내야 하기 때문에 여기에 아래 코드 추가
                //이미지를 전달하기 위해 코드 작성(이미지 축소)
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable) grade.getDrawable()).getBitmap();
                float scale = (1024 / (float) bitmap.getWidth());
                int image_w = (int) (bitmap.getHeight() * scale);
                int image_h = (int) (bitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                //이 아래 코드는 getCommentFromDatabase메소드에서 해당 영화에 해당하는 모든 한줄평 정보를 list에 담아서 갖고 온다.
                //그 이후에 여기 onResume에서 어댑터에 추가해서 getView메소드를 통해 데이터베이스에서 가져온 정보를
                //인터넷이 없을 때 보여주는 역할을 하기 위해서 짜여진 코드이다. 지렸다. -> 메모리 측면에서 좋은 방법인지는 모르겠다.
                ArrayList list = AppHelper.getCommentFromDatabase(position); //데이터베이스에 있는 movieId가 ?인 데이터를 내림차순으로 조회한것 불러옴
                Log.d("FragMovieInfo", "인터넷없을 때 AppHelper에 담은 comment list내용: " + list.toString()); //값이 넘어옴
                Iterator it = list.iterator();
                //commentItems에 계속 item들이 쌓이기 때문에 매번 프래그먼트를 실행할 때마다 원래 데이터가 있으면 clear 해주고 다시 넣어야 한다.
                if (commentItems.size() > 0) {
                    Log.d("FragMovieInfo", "commentItems 안에 데이터가 있어서 클리어 실시함!!!");
                    commentItems.clear();
                }
                while (it.hasNext()) {
                    CommentItem item = (CommentItem) it.next();
                    commentAdapter.addItem(item);
                }
                Log.d("FragMovieInfo", "!!! 어댑터 내부에 있는 정보 개수: " + commentItems.size());

            }
        }

        likeState = false;
        dislikeState = false;
        Log.d("FragMovieInfo", "onResume likeState: " + likeState);
        Log.d("FragMovieInfo", "onResume dislikeState: " + dislikeState);
        //프래그먼트가 재실행될때마다 좋아요를 안 누른 걸로 한다. 이렇게 하지 않았을 때 프래그먼트를 종료하고 다시 돌아왔을 때 로직에 문제가 생김.
        //이러한 문제들은 영화 고유아이디 정보 등을 데이터베이스에 저장을 하면 가능할 것 같음. 현재 단계에서는 해결하기가 어려움.

        /* 좋아요 문제점: 프래그먼트를 종료하고 다시 돌아왔을 때 좋아요를 눌렀었다면 좋아요 눌러져있는 이미지로 보이게 하고 다시 눌렀을 때 취소되는 기능 구현 못함
         *               프래그먼트를 종료하고 다시 들어왔을 때 좋아요를 눌러져있는 이미지로 보이게 하는 것은 가능하지만 다른 영화정보와 구분할 방법을 못찾음.
         *               예를 들어, 상세화면에서 좋아요를 눌러 놓고 나갔다가 다시 들어왔을 때 좋아요 이미지가 눌러져있는 이미지로 세팅해 놓으면 likeState가 true인
         *               것을 인지하고 코딩을 하는 것이다. 그렇게 하면 꾼에서 좋아요를 눌러놓은 상태에서 프래그먼트를 나가고 저스티스 리그 상세화면을 키면 저스티스리그
         *               상세화면에 있는 좋아요 버튼이 눌러져있는 이미지로 돼있는 오류가 발생함. 그래서 아예 그 기능을 없앰. 좋아요를 누른 경험을 기억하지 못하는 코딩으로 대체함.
         *               영화상세화면이 갖고 있는 고유정보인 position을 활용해서 이 position을 갖는 영화마다 likeState dislikeState 정보를 각각 따로 저장하면 가능할 것으로 보임.
         *               내 생각엔 현재 프로젝트에서 사용하는 API를 가지고는 불가능할 것으로 보임.
         *               좋아요를 누른 적이 있는지 없는지를 저장할 수 있는 데이터베이스 요소가 없기 때문에 이 부분은 구현이 어렵다고 판단.
         *
         *  + API 자체적으로 문제가 있는건지 모르겠는데 싫어요를 누른 상태에서 좋아요를 누르면 싫어요 취소요청을 보내고 좋아요 증가요청을 보내도록 코드를 짰다.
         * Log를 찍어봤을 때 메소드는 정상적으로 성공했다는 메세지를 받았다. 그런데 실제 서버상에 저장되는 데이터가 의도된대로 될 때가 있고 안 될 때가 있다.
         * 싫어요를 누른 상태에서 좋아요를 누르거나 좋아요를 누른 상태에서 싫어요를 누를 때 어떤 한 동작이 서버에 저장되지 않는다. 여기서 성공하였다라는 메세지를 받았음에도 불구하고
         * 서버에 저장이 안 되는 문제가 있기 때문에 API의 문제라고 생각한다.
         *  */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_info, container, false);

        ListView commentListView = rootView.findViewById(R.id.comment_listview);
        scrollView = rootView.findViewById(R.id.scroll_view);

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

        recyclerView = rootView.findViewById(R.id.recyclerView);

        //RecyclerView 가로로 스크롤되게 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        galleryAdapter = new GalleryAdapter(getActivity()); //갤러리 어댑터 객체 생성
        recyclerView.setAdapter(galleryAdapter);    //GalleryAdapter를 어댑터로 설정

        //RecyclerView 아이템 클릭시 확대화면 보여주기(인터넷 될 때만 해야됨.)
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GalleryAdapter.ViewHolder holder, View view, int position) {

                //인터넷이 연결돼있을 때만 볼 수 있다.
                if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI) {

                    RecyclerItem item = galleryAdapter.getItem(position);
                    //동영상이면 재생하고 사진이면 사진보기 activity를 실행.
                    if(item.getIsVideo()){
                        Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getVideo()));
                        startActivity(playIntent);
                    }else{
                        Intent viewPhotoIntent = new Intent(getContext(), ViewPhotos.class);
                        viewPhotoIntent.putExtra("photo", item.getPhoto()); //Intent를 통해서 photo url 정보를 보내준다.
                        startActivity(viewPhotoIntent);
                    }
                }
            }
        });


        commentAdapter = new CommentAdapter();  //어댑터를 사용하기 위해 객체 생성
        commentListView.setAdapter(commentAdapter);

        //작성하기 버튼을 눌렀을 때
        writeCommentButton = rootView.findViewById(R.id.writeCommentButton);
        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //한줄평 작성은 인터넷에 연결돼있을 때만 할 수 있다.
                if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI) {
                    Intent writeCommentIntent = new Intent(getActivity(), WriteCommentActivity.class);
                    writeCommentIntent.putExtra("title", movieResponse.result.get(0).title);    //작성하기 activity에 영화제목을 넘겨줌
                    writeCommentIntent.putExtra("position", position);   //영화 인덱스인 position을 넘겨준다.
                    //이미지 보내주기
                    writeCommentIntent.putExtra("integer", 300);
                    writeCommentIntent.putExtra("double", 3.141592);
                    writeCommentIntent.putExtra("image", byteArray);

                    startActivity(writeCommentIntent); //작성하기 activity실행
                } else {
                    Toast.makeText(getContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //모두보기 버튼을 눌렀을 때 한줄평 전체 listview를 보여주는 코드
        final Button readMoreButton = rootView.findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readMoreIntent = new Intent(getActivity(), ReadMoreActivity.class);
                //인터넷이 있을 때는 서버에서 받아온 정보를 모두보기액티비티에 보내지만 인터넷이 없을 때는 데이터베이스에서 가져와서 title정보를 보낸다.
                if (NetworkStatus.getConnectivityStatus(getContext()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getContext()) == NetworkStatus.TYPE_WIFI) {
                    readMoreIntent.putExtra("title", movieResponse.result.get(0).title);    //모두보기 activity 에 영화제목을 넘겨줌
                    readMoreIntent.putExtra("rating", movieResponse.result.get(0).audience_rating);  //평점 모두보기에 넘겨줌
                    readMoreIntent.putExtra("totalCount", commentResponse.totalCount);   //한줄평 개수 -> 평점 참여 총 인원
                } else {
                    AppHelper.selectData(AppHelper.MOVIE_INFO, position, 0);
                    AppHelper.selectData(AppHelper.TOTAL_COUNT, position, 0);
                    //위 메소드를 먼저 실행해줘야 영화정보에 맞는 title, rating이 AppHelper.info_title, info_audience_rating 에 저장된다.
                    //totalCount 에도 마찬가지로 적용된다.
                    readMoreIntent.putExtra("title", AppHelper.info_title);
                    readMoreIntent.putExtra("rating", AppHelper.info_audience_rating);
                    readMoreIntent.putExtra("totalCount", AppHelper.total_totalCount);
                }
//                readMoreIntent.putExtra("list", commentItems);   //그 안에 들어있는 객체들은 Parcelable 구현해서 넘겨줌.
//                -> 일단 주석. 왜? readMoreActivity에서도 서버에서 받아올 거니까 보내줄 필요 없음.
                readMoreIntent.putExtra("position", position);   //모두보기 activity에 영화 인덱스인 position정보를 보내준다.
                Log.d("FragMovieInfo", "보내려는 position 값: " + position);

                //이미지 보내주기
                readMoreIntent.putExtra("integer", 300);
                readMoreIntent.putExtra("double", 3.141592);
                readMoreIntent.putExtra("image", byteArray);

                startActivity(readMoreIntent);  //새로운 activity에서 작성하기를 누른 후 리스트정보를 받아와야 하기 때문에 ForResult
                //-> 모두보기 액티비티에서도 서버에서 받아와서 보여주면 되니까 forResult 취소.
            }


        });

        like = rootView.findViewById(R.id.info_like);
        likeButton = rootView.findViewById(R.id.likeButton);
        //likeButton을 눌렀을 때
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //인터넷이 연결돼있어야 좋아요를 누를 수 있다.
                if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI) {
                    if (likeState) {
                        decrLikeCount();
                        likeyn = "N";   //좋아요를 1감소하는 메소드 이므로 서버에 N으로 전달
                        sendLikeynToServer(likeyn);
                    } else {
                        incrLikeCount();
                        likeyn = "Y"; //좋아요를 1증가하는 메소드 이므로 서버에 Y로 전달
                        sendLikeynToServer(likeyn);
                        if (dislikeState) {   //싫어요 버튼 눌러져있는 상태였다면 상태를 안 눌린 상태로 바꾸고 싫어요 숫자도 하나 줄인다.
                            decrDislikeCount();
                            dislikeyn = "N";    //싫어요를 1감소하는 메소드 이므로 서버에 N으로 전달
                            sendDisLikeynToServer(dislikeyn);
                            dislikeState = !dislikeState;
                        }
                    }

                    likeState = !likeState; //클릭을 했기 때문에 상태가 눌린상태에서 안눌린상태로, 안눌린 상태에서 눌린 상태로 변해야 함.
                } else {
                    Toast.makeText(getContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dislike = rootView.findViewById(R.id.info_dislike);
        dislikeButton = rootView.findViewById(R.id.dislikeButton);
        //dislikeButton을 눌렀을 때
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //인터넷이 연결돼있어야 싫어요를 누를 수 있다.
                if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI) {
                    if (dislikeState) {
                        decrDislikeCount();
                        dislikeyn = "N";    //싫어요를 1감소하는 메소드 이므로 서버에 N으로 전달
                        sendDisLikeynToServer(dislikeyn);
                    } else {
                        incrDislikeCount();
                        dislikeyn = "Y";   //좋아요를 1증가하는 메소드 이므로 서버에 Y로 전달
                        sendDisLikeynToServer(dislikeyn);
                        if (likeState) {  //좋아요 버튼 눌러져있는 상태였다면 상태를 안 눌린 상태로 바꾸고 좋아요 숫자도 하나 줄인다.
                            decrLikeCount();
                            likeyn = "N";   //좋아요를 1감소하는 메소드 이므로 서버에 N으로 전달
                            sendLikeynToServer(likeyn);
                            likeState = !likeState;
                        }
                    }
                    dislikeState = !dislikeState;   //클릭을 했기 때문에 상태가 눌린상태에서 안눌린상태로, 안눌린 상태에서 눌린 상태로 변해야 함.
                } else {
                    Toast.makeText(getContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }//onCreatView 메서드 끝

    public void requestMovieInfo() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";  //영화 상세정보 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FragMainInfo", "상세보기 화면에서 서버로부터 응답 받음: " + response);

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
                        Log.d("FragMainInfo", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = Integer.toString(position);
                params.put("id", id);    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화정보가 넘어온다.
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("FragMovieInfo", "영화상세정보 서버에 요청함");

    }

    public void processResponse(String response) throws ParseException {
        Gson gson = new Gson();

        MovieResponse info = gson.fromJson(response, MovieResponse.class);
        if (info.code == 200) {
            movieResponse = gson.fromJson(response, MovieResponse.class);
            Log.d("FragMovieInfo", "서버에서 얻어온 데이터 감독: " + movieResponse.result.get(0).director);  //result에 id값으로 한 영화만 받아오기 때문에 서버상 list size가 1이다. //그래서 인덱스가 0인 데이터만 접근할 수 있다.

            //서버에서 얻은 데이터를 이용해 FragMovieInfo 뷰들에 세팅한다.
            title.setText(movieResponse.result.get(0).title);
            date.setText(movieResponse.result.get(0).date.replace("-", ". ") + " 개봉"); //2017-10-1 을 2017. 10. 1 로 바꿔줌.
            genre.setText(movieResponse.result.get(0).genre);
            duration.setText(movieResponse.result.get(0).duration + "분");
            like.setText(movieResponse.result.get(0).like + "");
            dislike.setText(movieResponse.result.get(0).dislike + "");
            reservation_grade.setText(movieResponse.result.get(0).reservation_grade + "위");
            reservation_rate.setText(movieResponse.result.get(0).reservation_rate + " %");
            audience_rating.setText(movieResponse.result.get(0).audience_rating + " 점");
            infoRatingBar.setRating((movieResponse.result.get(0).audience_rating) / 2);   //레이팅바 서버 평점 정보로 표시, 2로 나눠야 별 색깔이 수치에 맞게 채워짐
            DecimalFormat formatter = new DecimalFormat("###,###"); //숫자 사이에 , 를 넣는 코드
            audience.setText(formatter.format(movieResponse.result.get(0).audience) + "명");
            synopsis.setText(movieResponse.result.get(0).synopsis);
            director.setText(movieResponse.result.get(0).director);
            actor.setText(movieResponse.result.get(0).actor);
//            ImageLoadTask imageLoadTask = new ImageLoadTask(movieResponse.result.get(0).thumb, poster);   //클래스 내부에 set하게 정의해 놓음.
//            imageLoadTask.execute();
            Glide.with(getActivity())
                    .load(movieResponse.result.get(0).thumb)
                    .placeholder(R.drawable.loading)
                    .error(R.mipmap.ic_launcher)
                    .thumbnail(0.1f)
                    .into(poster);

            //서버에서 grade 데이터가 12,15,19이냐에 따라 몇세 관람가 이미지를 다르게 설정한다.
            switch (movieResponse.result.get(0).grade) {
                case 12:
                    grade.setImageResource(R.drawable.ic_12);
                    break;
                case 15:
                    grade.setImageResource(R.drawable.ic_15);
                    break;
                case 19:
                    grade.setImageResource(R.drawable.ic_19);
                    break;
                default:
                    grade.setImageResource(R.drawable.announcement);
            }

            //영화의 사진, 동영상 정보를 갤러리 리싸이클러뷰 어댑터에 넣어서 보여준다
            if (movieResponse.result.get(0).photos != null && galleryAdapter.isEmpty()) {
                String[] photos = movieResponse.result.get(0).photos.split(",");    //서버에 , 를 기준으로 여러 사진 url이 있으므로 나눠서 받는다.
                for (int i = 0; i < photos.length; i++) {
                    RecyclerItem item = new RecyclerItem();
                    item.setPhoto(photos[i]);
                    galleryAdapter.addItem(item);  //모든 사진 url을 어댑터에 추가한다.
                }

                String[] videos = movieResponse.result.get(0).videos.split(",");

                //동영상 썸네일 이미지 recyclerView에 추가
                RecyclerItem item1 = new RecyclerItem();
                item1.setPhoto("https://img.youtube.com/vi/VJAPZ9cIbs0/0.jpg");
                item1.setIsVideo(true);
                item1.setVideo(videos[0]);
                galleryAdapter.addItem(item1);
                RecyclerItem item2 = new RecyclerItem();
                item2.setPhoto("https://img.youtube.com/vi/y422jVFruic/0.jpg");
                item2.setIsVideo(true);
                item2.setVideo(videos[1]);
                galleryAdapter.addItem(item2);
                RecyclerItem item3 = new RecyclerItem();
                item3.setPhoto("https://img.youtube.com/vi/JNL44p5kzTk/0.jpg");
                item3.setIsVideo(true);
                item3.setVideo(videos[2]);
                galleryAdapter.addItem(item3);

                galleryAdapter.notifyDataSetChanged();  //어댑터에 변화가 있으면 갱신해라.
            }

            //이미지를 전달하기 위해 코드 작성(이미지 축소) -> 여기서 축소를 해줘야 서버에서 받아온 파일을 축소해서 보낼 수가 있다.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) grade.getDrawable()).getBitmap();
            float scale = (1024 / (float) bitmap.getWidth());
            int image_w = (int) (bitmap.getHeight() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();

            //서버에 정보를 요청할 때마다 영화data를 받아오는데 조건이 있다.
            if (AppHelper.isDataExsist(AppHelper.MOVIE_INFO, movieResponse.result.get(0).id)) {  //database에 이미 id값이 서버에서 넘어오는 id값과 동일한 것이 존재하면(즉, 중복되게 저장되는 것을 피하기 위해)
                //insert를 해서 중복되게 record를 삽입하지 말고 원래 있던 record를 서버에서 오는 새로운 정보로 update해라
                AppHelper.updateMovieInfoData(movieResponse.result.get(0).id, movieResponse.result.get(0).title, movieResponse.result.get(0).id, movieResponse.result.get(0).date, movieResponse.result.get(0).user_rating, movieResponse.result.get(0).audience_rating, movieResponse.result.get(0).reviewer_rating, movieResponse.result.get(0).reservation_rate, movieResponse.result.get(0).reservation_grade, movieResponse.result.get(0).grade, movieResponse.result.get(0).thumb, movieResponse.result.get(0).image, movieResponse.result.get(0).photos, movieResponse.result.get(0).videos, movieResponse.result.get(0).outlinks, movieResponse.result.get(0).genre, movieResponse.result.get(0).duration, movieResponse.result.get(0).audience, movieResponse.result.get(0).synopsis, movieResponse.result.get(0).director, movieResponse.result.get(0).actor, movieResponse.result.get(0).like, movieResponse.result.get(0).dislike);
                AppHelper.selectData(AppHelper.MOVIE_INFO);    //로그찍기
            } else {
                //최초로 서버에서 받아오는 거면(즉, 영화 id값이 database에 없으면) 새로 record를 만들어서 insert 삽입해라.
                AppHelper.insertMovieInfoData(movieResponse.result.get(0).title, movieResponse.result.get(0).id, movieResponse.result.get(0).date, movieResponse.result.get(0).user_rating, movieResponse.result.get(0).audience_rating, movieResponse.result.get(0).reviewer_rating, movieResponse.result.get(0).reservation_rate, movieResponse.result.get(0).reservation_grade, movieResponse.result.get(0).grade, movieResponse.result.get(0).thumb, movieResponse.result.get(0).image, movieResponse.result.get(0).photos, movieResponse.result.get(0).videos, movieResponse.result.get(0).outlinks, movieResponse.result.get(0).genre, movieResponse.result.get(0).duration, movieResponse.result.get(0).audience, movieResponse.result.get(0).synopsis, movieResponse.result.get(0).director, movieResponse.result.get(0).actor, movieResponse.result.get(0).like, movieResponse.result.get(0).dislike);
                AppHelper.selectData(AppHelper.MOVIE_INFO);    //로그찍기
            }

        }
    }

    //서버에 한줄평 데이터를 요청하는 메소드
    public void requestCommentList() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";  //한줄평 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FragMainInfo", "상세보기에서 한줄평 정보 받아옴: " + response);

                        //받아온 JSON데이터를 GSON을 이용해 파싱해서 처리한다.
                        processCommentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FragMainInfo", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = Integer.toString(position);
                params.put("id", id);    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화의 한줄평정보가 넘어온다.
                params.put("limit", Integer.toString(3));    //상세보기 화면에서는 3개만 요청해서 보여줘라
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("FragMovieInfo", "한줄평 정보 서버에 요청함");

    }


    public void processCommentResponse(String response) {
        Gson gson = new Gson();

        CommentResponse info = gson.fromJson(response, CommentResponse.class);
        if (info.message.equals("movie readCommentList 성공")) {
            commentResponse = gson.fromJson(response, CommentResponse.class);
            Log.d("FragMovieInfo", "테스트중: " + commentResponse.result.size()); //서버상 list가 여러개이므로 전부(3개로 세팅) 다 온다.

            //서버에 default로 지정된 최근 3개 한줄평을 다 얻어와서 commentItem에 세팅한 후 3개를 전부 어댑터 내부에 있는 commentItems List에 add한다.
            if (commentItems.size() == 0) {
                for (int i = 0; i < commentResponse.result.size(); i++) {
                    CommentItem commentItem = new CommentItem();    //CommentItem객체를 생성해서
                    //아이템의 각 필드에 서버에서 얻어온 데이터를 set한다.
                    commentItem.setWriter(commentResponse.result.get(i).writer);
                    commentItem.setTime(commentResponse.result.get(i).time);
                    commentItem.setRating(commentResponse.result.get(i).rating);
                    commentItem.setContents(commentResponse.result.get(i).contents);
                    commentItem.setRecommend(commentResponse.result.get(i).recommend);
                    commentItem.setId(commentResponse.result.get(i).id);    //id값을 저장해놔야 추천할 때 사용할 수 있다.
                    commentAdapter.addItem(commentItem);    //어댑터에 들어갈 items ArrayList에 추가한다.

                    /* 서버로부터 와서 저장한 것도 3개 있으니까 이 반복문 안에 데이터베이스에서 조회한 것들을 세팅해주는데;
                     * comment_id를 내림차순으로 정렬하고 그거를 위에 있는 것부터 하나씩 commentItem에 세팅해주면
                     * 리스트뷰에 하나씩 들어가지 않나? 인터넷이 없을 때는 여기에다가 데이터베이스의 내용을 내림차순으로
                     * 조회한 순서대로 set해주고 아래서 인터넷없을 때 commentItemView에 보여줄 때는 get해서 하면 되지 않을까?
                     * -> 이 부분은 인터넷이 연결돼있을 때만 실행된다. 그래서 다른 곳에서 코드를 짜야 된다. */

//                    //서버에 정보를 요청할 때마다 Comment data를 받아오는데 조건이 있다.
//                    //comment 고유 id와 movieId가 동일한 데이터가 데이터베이스에 이미 있다면 update를 해라.
//                    if (AppHelper.isCommentExsist(AppHelper.COMMENT, commentResponse.result.get(i).id, position)) {  //database에 이미 한줄평 id값이 서버에서 넘어오는 id값과 동일한 것이 존재하면(즉, 중복되게 저장되는 것을 피하기 위해)
//                        //insert를 해서 중복되게 record를 삽입하지 말고 원래 있던 record를 서버에서 오는 새로운 정보로 update해라
//                        AppHelper.updateCommentData(commentResponse.result.get(i).id,commentResponse.result.get(i).writer,commentResponse.result.get(i).movieId,commentResponse.result.get(i).writer_image,commentResponse.result.get(i).time,commentResponse.result.get(i).timestamp,commentResponse.result.get(i).rating,commentResponse.result.get(i).contents,commentResponse.result.get(i).recommend);
//                        AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
//                    } else {
//                        //최초로 서버에서 받아오는 거면(즉, 한줄평 id값과 movieId값이 database에 없으면) 새로 record를 만들어서 insert 삽입해라.
//                        AppHelper.insertCommentData(commentResponse.result.get(i).id,commentResponse.result.get(i).writer,commentResponse.result.get(i).movieId,commentResponse.result.get(i).writer_image,commentResponse.result.get(i).time,commentResponse.result.get(i).timestamp,commentResponse.result.get(i).rating,commentResponse.result.get(i).contents,commentResponse.result.get(i).recommend);
//                        AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
//                    }
                }//for 끝
            }
            commentAdapter.notifyDataSetChanged();
            Log.d("FragMovieInfo", "어댑터 리스트에 추가: " + commentItems.size());
            //반복문만 있으면 상세화면에 들어갈 때 마다 commentItems list에 10개씩 추가로 들어간다.

            //totalCount테이블에 해당 영화 레코드가 있으면 업데이트하고 없으면 새로 만드는 코드
            if (AppHelper.isDataExsist(AppHelper.TOTAL_COUNT, position)) {
                AppHelper.updateTotalCountData(position, commentResponse.totalCount);
                AppHelper.selectData(AppHelper.TOTAL_COUNT);
            } else {
                AppHelper.insertTotalCountData(position, commentResponse.totalCount);
                AppHelper.selectData(AppHelper.TOTAL_COUNT);
            }


        }
    }

    //좋아요 수를 증가
    public void incrLikeCount() {
        Log.d("FragMovieInfo", "이 영화가 갖고 있는 좋아요 숫자: " + movieResponse.result.get(0).like);
        like.setText(String.valueOf(movieResponse.result.get(0).like + 1));   //증가된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);  //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 누른상태이미지로 바꿔서 유지한다.
    }

    //좋아요 수를 감소
    public void decrLikeCount() {
        like.setText(String.valueOf(movieResponse.result.get(0).like));   //감소된 int likeCount를 문자열로 변환하여 텍스트뷰에 보이게 한다.
        likeButton.setBackgroundResource(R.drawable.thumb_up_selector); //버튼을 눌렀을 때만 실행되는 메소드이므로 버튼의 배경을 안눌린 상태로 바꿔서 유지한다. (클릭상태를 유지하면 눌린이미지가 보이게함.)
        /* 이 부분에서 프래그먼트 나갔다가 다시 들어왔을 때 좋아요를 취소했는데 그냥 그대로 보이는 문제; 해결해야 된다. */
    }

    //싫어요 수를 증가
    public void incrDislikeCount() {
        dislike.setText(String.valueOf(movieResponse.result.get(0).dislike + 1));
        dislikeButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
    }

    //싫어요 수를 감소
    public void decrDislikeCount() {
        dislike.setText(String.valueOf(movieResponse.result.get(0).dislike));
        dislikeButton.setBackgroundResource(R.drawable.thumb_down_selector);
    }

    //좋아요 정보를 서버에 전달하는 메소드
    public void sendLikeynToServer(final String likeyn) {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseLikeDisLike";  //좋아요싫어요 적용 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FragMovieInfo", "상세화면에서 좋아요 서버에 요청함: " + response);

                        //받아온 JSON 데이터를 GSON을 이용해 파싱해서 처리한다.
                        processMovieResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FragMovieInfo", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(position));    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화에 좋아요 수를 전달한다.
                params.put("likeyn", likeyn);  //좋아요 정보를 서버에 parameter로 넘긴다
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("FragMovieInfo", "상세화면에서 좋아요 정보 서버에 저장 요청함");
    }

    //싫어요 정보를 서버에 전달하는 메소드
    public void sendDisLikeynToServer(final String dislikeyn) {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseLikeDisLike";  //좋아요싫어요 적용 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FragMovieInfo", "상세화면에서 좋아요 서버에 요청함: " + response);

                        //받아온 JSON 데이터를 GSON을 이용해 파싱해서 처리한다.
                        processMovieResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FragMovieInfo", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(position));    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화에 좋아요 수를 전달한다.
                params.put("dislikeyn", dislikeyn); //싫어요 정보를 서버에 parameter로 넘긴다.
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("FragMovieInfo", "상세화면에서 좋아요 정보 서버에 저장 요청함");
    }

    //좋아요 서버에 요청한 자료를 갖고 파싱하는 메소드
    public void processMovieResponse(String response) {
        Gson gson = new Gson();

        //성공했는지 실패했는지 로그를 찍어보자
        LikeResponse likeResponse = gson.fromJson(response, LikeResponse.class);
        if (likeResponse.code == 200) {
            Log.d("FragMovieInfo", "좋아요,싫어요 정보 서버에 보내기 성공: " + likeResponse.message);
        } else {
            Log.d("FragMovieInfo", "좋아요,싫어요 정보 서버에 보내기 실패: " + likeResponse.message);
        }
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

            CommentItem commentItem = commentItems.get(i);  //서버에 연결한 이후에는 이렇게 해야 순서대로 나온다.
//            CommentItem commentItem = commentItems.get(commentItems.size() - 1 - i);    //순서대로 나오지 않고 역순으로 나오게 하려고 사이즈-1에서 i를 뺌
            //이렇게 하면 새로 등록한 한줄평이 제일 위로 나올 수 있게 됨.
            //이거 지렸다...


            //인터넷 연결 유무에 따른 뷰 설정 차이
            if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE) {

                //-> 여기다가 하니까 다른 영화 상세화면에 들어갈 때도 잘 되는구만.
                //서버에 정보를 요청할 때마다 Comment data를 받아오는데 조건이 있다.
                //comment 고유 id와 movieId가 동일한 데이터가 데이터베이스에 이미 있다면 update를 해라.
                //근데 이거는 인터넷이 연결돼있을 때만 해라.
                if (AppHelper.isDataExsist(AppHelper.COMMENT, commentResponse.result.get(i).id)) {  //database에 이미 한줄평 id값이 서버에서 넘어오는 id값과 동일한 것이 존재하면(즉, 중복되게 저장되는 것을 피하기 위해)
                    //insert를 해서 중복되게 record를 삽입하지 말고 원래 있던 record를 서버에서 오는 새로운 정보로 update해라
                    AppHelper.updateCommentData(commentResponse.result.get(i).id, commentResponse.result.get(i).writer, commentResponse.result.get(i).movieId, commentResponse.result.get(i).writer_image, commentResponse.result.get(i).time, commentResponse.result.get(i).timestamp, commentResponse.result.get(i).rating, commentResponse.result.get(i).contents, commentResponse.result.get(i).recommend);
                    AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
                } else {
                    //최초로 서버에서 받아오는 거면(즉, 한줄평 id값과 movieId값이 database에 없으면) 새로 record를 만들어서 insert 삽입해라.
                    AppHelper.insertCommentData(commentResponse.result.get(i).id, commentResponse.result.get(i).writer, commentResponse.result.get(i).movieId, commentResponse.result.get(i).writer_image, commentResponse.result.get(i).time, commentResponse.result.get(i).timestamp, commentResponse.result.get(i).rating, commentResponse.result.get(i).contents, commentResponse.result.get(i).recommend);
                    AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
                }

                //CommentItem에 서버로부터 정보를 받아와서 그 정보를 listView에 보일 뷰들에 세팅한다.
                //위에 int i 가 position의 역할을 하므로 i값을 얻어오면 commentItems list에 있는 인덱스에 적용돼서 잘 된다.
                commentItemView.setUserId(commentResponse.result.get(i).writer);
                commentItemView.setCommentContent(commentResponse.result.get(i).contents);
                commentItemView.setCommentRatingBar(commentResponse.result.get(i).rating / 2);    //나누기2를 하는 이유는 곱하기 2를 해서 서버에 저장하기 때문에 다시 리스트에 보여질 때는 자신이 입력한 별점만큼 보여야 하기 때문이다.
                commentItemView.setTime(commentResponse.result.get(i).time);
                commentItemView.setRecommendationNum(commentResponse.result.get(i).recommend + "");
                Log.d("FragMovieInfo", "CommentItemView에서 세팅한 것 TEST: " + commentResponse.result.get(i).contents);

                //이 메소드 내부적으로 순서대로 item들에 아래 값들을 적용 시키는 것 같다.
                //->이렇게 하면 작성한 이후에 서버에 요청한 정보가 들어오는 것이 아니고 기존에 저장돼있는 정보가 넘어와서 바로 최신화된 정보가 보이지 않는다.
//            commentItemView.setUserId(commentItem.writer);
//            commentItemView.setCommentContent(commentItem.contents);
//            commentItemView.setCommentRatingBar(commentItem.rating);
//            commentItemView.setTime(commentItem.time);
//            commentItemView.setRecommendationNum(commentItem.recommend+"");
//            Log.d("FragMovieInfo","상세화면에서 한줄평리스트 정보TEST: " + commentItem.writer);

                Log.d("ReadMoreActivity", "한줄평의 고유 id값: " + commentResponse.result.get(i).id);
                commentItemView.setId(commentResponse.result.get(i).id);    //각 한줄평 리스트 아이템들에 고유 id값을 서버에서 받아와 적용시킨다.
                commentItemView.setRecommendation_num(commentResponse.result.get(i).recommend); //원래 서버에 저장된 값을 commentItemView에 저장한다.
                //그 후에 거기서 1 증가한 수를 즉각적으로 보여주기 위해 저장함.
            } else {
                //데이터베이스에서 가져와서 보여줘야 된다.
                Log.d("FragMovieInfo", "인터넷이 연결돼있지 않아 데이터베이스에서 정보를 가져와 뷰에 보여줌.");

                if (AppHelper.selectData(AppHelper.COMMENT, position, commentItem.getId())) {  //getId 위에 onResume에서 인터넷없을 때 넣어준 id값들을 차례대로 가져오기 때문에 한줄평들을 각각 보여줄 수 있다.
                    commentItemView.setUserId(AppHelper.com_writer);
                    commentItemView.setCommentContent(AppHelper.com_contents);
                    commentItemView.setCommentRatingBar(AppHelper.com_rating / 2);    //나누기2를 하는 이유는 곱하기 2를 해서 서버에 저장하기 때문에 다시 리스트에 보여질 때는 자신이 입력한 별점만큼 보여야 하기 때문이다.
                    commentItemView.setTime(AppHelper.com_time);
                    commentItemView.setRecommendationNum(AppHelper.com_recommend + "");
                    commentItemView.setId(AppHelper.com_id);
                    commentItemView.setRecommendation_num(AppHelper.com_recommend);
                }
            }

            return commentItemView;
        }
    }//어댑터 클래스 끝

    /* 이 부분도 서버에 한줄평작성을 저장하면 필요가 없군! */
    //한줄평 작성과 평점 데이터를 불러오는 것 코드
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        if (requestCode == 102) {
//            float rating = intent.getFloatExtra("rating", 0.0f);
//            String comment = intent.getStringExtra("comment");
//            if (rating > 0.0 && comment.length() > 0) {
//                commentAdapter.addItem(new CommentItem("DomMorello", comment, rating)); //한줄평 리스트에 추가하기
//                commentAdapter.notifyDataSetChanged();  //변화가 있으면 갱신해라.
//                Snackbar.make(likeButton, "한줄평이 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
//            }
//        }
//        //모두보기에서 결과를 가져올 때
//        if (requestCode == 104) {
//            commentItems = (ArrayList<CommentItem>) intent.getSerializableExtra("list");
//            commentAdapter.notifyDataSetChanged();  //갱신된 list로 어댑터한테 갱신하라고 말해줌
//        }
//
//    }

}
