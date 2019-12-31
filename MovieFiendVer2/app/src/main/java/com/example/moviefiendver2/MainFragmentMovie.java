package com.example.moviefiendver2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviefiendver2.MovieData.MovieInfo;
import com.example.moviefiendver2.MovieData.MovieResponse;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.helper.NetworkStatus;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//델타보이즈 화면 프래그먼트
public class MainFragmentMovie extends Fragment {

    FragmentCallback fragmentCallback;  //인터페이스 참조 -> 상세보기로 넘어갈 때 다른 프래그먼트를 보여주기 위함
    ImageView poster;
    TextView mainTitle;
    TextView reservationRate;
    TextView grade;
    TextView date;
    int position; //영화 서버에서 넘어오는 것을 구분하기 위한 인덱스

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //인터페이스를 구현했으면 해당 activity에 context를 참조한다.
        if (context instanceof FragmentCallback) {
            fragmentCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //position 정보를 newInstance메소드를 통해서 받아온다.
        position = getArguments().getInt("position", 0);
        Log.d("MainFragmentMovie", "position 정보가 넘어왔음: " + position);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (fragmentCallback != null) {
            fragmentCallback = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_movie, container, false);
        Log.d("MainFragmentMovie", "onCreateView가 호출됨!!!");

        //서버에서 받아온 정보들을 표시할 뷰들을 찾아온다.
        poster = rootView.findViewById(R.id.main_poster); //영화 포스터
        mainTitle = rootView.findViewById(R.id.main_title);    //영화 제목
        reservationRate = rootView.findViewById(R.id.reservation_rate);    //예매율
        grade = rootView.findViewById(R.id.grade); //영화 관람가 제한
        date = rootView.findViewById(R.id.d_day);  //영화 개봉일 -> d-day로 바꿔야 된다.

        //인터넷에 연결돼있을 때는 서버에서 가져온 정보들을 뷰에 보여준다.
        if (NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_WIFI || NetworkStatus.getConnectivityStatus(getActivity()) == NetworkStatus.TYPE_MOBILE) {
            //RequestQueue 객체를 생성한다.
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            }
            requestMovieList(); //서버에 영화 목록 데이터를 요청하는 메소드
        //인터넷에 연결이 안 돼있을 때는 데이터베이스에 있는 정보를 가져와서 뷰에 보여준다.
        } else {
            Log.d("MainFragmentMovie","인터넷 연결 안 돼있을 때 메인화면 데이터베이스에서 가져온거임.");
            //table이 기존에 존재 하면 실행 아니면 아무것도 하지 않는다.
            if(AppHelper.selectData(AppHelper.MAIN_MOVIE)){ //select메소드가 table존재 유무를 알려준다. 존재하면 true, 아니면 false이다.
                AppHelper.selectData(AppHelper.MAIN_MOVIE,position+1,0); //position+1 해야 영화 id값이 나옴.
                mainTitle.setText(AppHelper.main_title);    //AppHelper클래스에 public으로 선언해놔서 갖고와서 사용할 수 있는데 좋은 방법인지는 모르겠다.
                grade.setText(AppHelper.main_grade + "세 관람가");
                reservationRate.setText(AppHelper.main_reservation_rate + "%");
                try {
                    date.setText("D - " + getDday(AppHelper.main_dateValue));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
//            ImageLoadTask imageLoadTask = new ImageLoadTask(AppHelper.main_image, poster);
//            imageLoadTask.execute();
            //ImageLoadTask는 인터넷을 사용하여 URL에 저장돼있는 이미지를 불러오는 것이기 때문에 인터넷이 없을 때는 사용할 수 없다.
            //그럼 어떻게 해야 되나? 서버에 처음 접속했을 때 이미지를 다운로드해서 기기 내부에 저장해놓고 그 이미지를 사용해야 할 것 같다.
        }

        //상세보기를 눌렀을 때
        Button informationButton = rootView.findViewById(R.id.information_button);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCallback != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    fragmentCallback.onFragmentChange(bundle);   //상세화면 프래그먼트 실행
                    //프래그먼트끼리 직접 호출하면 안 되므로 MainActivity에 정의한 메서드를 통해 호출한다.
                }
            }
        });
        return rootView;
    }

    //서버에 영화 목록 데이터를 요청하는 메소드
    public void requestMovieList() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainFragmentMovie", "응답 받음 -> " + response);

                        //processResponse 메소드 안에 Dday얻는 메소드가 있는데 여기서 예외처리를 해주었기 때문에 여기서도 예외처리
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
                        Log.d("MainActivity", "에러 발생: " + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("MainActivity", "영화 목록 요청 보냄");
    }

    //받아온 response를 파싱해서 자바 객체로 만든다.
    public void processResponse(String response) throws ParseException {
        Gson gson = new Gson();

        MovieResponse info = gson.fromJson(response, MovieResponse.class);
        if (info.code == 200) {
            MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class); //movieList에 서버 데이터들이 다 넘어왔다.
            Log.d("MainActivity", "" + movieResponse.result.get(0).duration);

            MovieInfo movieInfo = movieResponse.result.get(position);   //서버로부터 받은 ArrayList에서 position에 맞게 객체를 얻는다.
            mainTitle.setText(movieInfo.title);
            grade.setText(movieInfo.grade + "세 관람가");
            reservationRate.setText(movieInfo.reservation_rate + "%");
            date.setText("D - " + getDday(movieInfo.date));   //dDay 메소드를 통해 날짜 차이를 얻어 낸 후 뷰에 표시
//            ImageLoadTask imageLoadTask = new ImageLoadTask(movieInfo.image, poster);   //클래스 내부에 set하게 정의해 놓음.
//            imageLoadTask.execute();
            Glide.with(getActivity())
                    .load(movieInfo.image)
                    .placeholder(R.drawable.loading)
                    .error(R.mipmap.ic_launcher)
                    .thumbnail(0.1f)
                    .into(poster);

            //서버에 정보를 요청할 때마다 영화data를 받아오는데 조건이 있다.
            if (AppHelper.isDataExsist(AppHelper.MAIN_MOVIE, movieInfo.id)) {  //database에 이미 id값이 서버에서 넘어오는 id값과 동일한 것이 존재하면(즉, 중복되게 저장되는 것을 피하기 위해)
                //insert를 해서 중복되게 record를 삽입하지 말고 원래 있던 record를 서버에서 오는 새로운 정보로 update해라
                AppHelper.updateMainMovieData(movieInfo.id, movieInfo.id, movieInfo.title, movieInfo.title_eng, movieInfo.date, movieInfo.user_rating, movieInfo.audience_rating, movieInfo.reviewer_rating, movieInfo.reservation_rate, movieInfo.reservation_grade, movieInfo.grade, movieInfo.thumb, movieInfo.image);
                AppHelper.selectData(AppHelper.MAIN_MOVIE);    //로그찍기
            } else {
                //최초로 서버에서 받아오는 거면(즉, 영화 id값이 database에 없으면) 새로 record를 만들어서 insert 삽입해라.
                AppHelper.insertMainMovieData(movieInfo.id, movieInfo.title, movieInfo.title_eng, movieInfo.date, movieInfo.user_rating, movieInfo.audience_rating, movieInfo.reviewer_rating, movieInfo.reservation_rate, movieInfo.reservation_grade, movieInfo.grade, movieInfo.thumb, movieInfo.image);
                AppHelper.selectData(AppHelper.MAIN_MOVIE);    //로그찍기
            }
        }
    }

    //Dday 계산하는 메소드
    public static long getDday(String date) throws ParseException {
        String future = date;
        String current = "2017-10-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date serverTime = format.parse(future);
        Date currentTime = format.parse(current);

        long calDate = serverTime.getTime() - currentTime.getTime();
        long dDay = calDate / (24 * 60 * 60 * 1000);
        dDay = Math.abs(dDay);

        return dDay;
    }

    //MainFragmentMovie 인스턴스를 생성할 때 position정보를 갖는 메소드
    //MovieListFragmnet에서 이 메서드를 호출할 때 입력하는 매개변수를 Bundle에 넣고 이 메소드를 통해 이 클래스로 보내준다.
    //position매개 변수를 받아와서 ArrayList에서 영화 정보를 뽑을 때 사용된다.
    public static MainFragmentMovie newInstance(int position) {
        MainFragmentMovie mainFragmentMovie = new MainFragmentMovie();
        Bundle args = new Bundle();
        args.putInt("position", position);
        mainFragmentMovie.setArguments(args);

        return mainFragmentMovie;
    }
}