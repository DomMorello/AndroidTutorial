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
import com.example.moviefiendver2.MovieData.MovieInfo;
import com.example.moviefiendver2.MovieData.MovieList;
import com.example.moviefiendver2.MovieData.ResponseInfo;
import com.example.moviefiendver2.helper.AppHelper;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

//델타보이즈 화면 프래그먼트
public class MainFragmentMovie extends Fragment {

    FragmentCallback fragmentCallback;  //인터페이스 참조 -> 상세보기로 넘어갈 때 다른 프래그먼트를 보여주기 위함
    ImageView poster;
    TextView mainTitle;
    TextView reservationRate;
    TextView grade;
    TextView date;
    int position; //영화 서버에서 넘어오는 것을 구분하기 위한 인덱스

    //Fragment 객체를 생성하는 메서드 position int값을 갖는다
    public static MainFragmentMovie newInstance(int position) {
        MainFragmentMovie mainFragmentMovie = new MainFragmentMovie();
        //position int값을 갖는다
        Bundle args = new Bundle();
        args.putInt("position", position);
        Log.d("MainFragment position: ", String.valueOf(position));
        mainFragmentMovie.setArguments(args);
        return mainFragmentMovie;
        /* 프래그먼트끼리 데이터를 주고 받을 때는 FragmentManager Transaction 등의 상황이 있어야 된다. 개 좆같네 시발 */
    }

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

        Button informationButton = rootView.findViewById(R.id.information_button);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCallback != null) {
                    fragmentCallback.onFragmentChange(0);   //상세화면 프래그먼트 실행
                    //프래그먼트끼리 직접 호출하면 안 되므로 MainActivity에 정의한 메서드를 통해 호출한다.
                }
            }
        });

        //서버에서 받아온 정보들을 표시할 뷰들을 찾아온다.
        poster = rootView.findViewById(R.id.main_poster); //영화 포스터
        mainTitle = rootView.findViewById(R.id.main_title);    //영화 제목
        reservationRate = rootView.findViewById(R.id.reservation_rate);    //예매율
        grade = rootView.findViewById(R.id.grade); //영화 관람가 제한
        date = rootView.findViewById(R.id.d_day);  //영화 개봉일

        //RequestQueue 객체를 생성한다.
        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        requestMovieList(); //서버에 영화 목록 데이터를 요청하는 메소드

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
                        Log.d("MainActivity", "응답 받음 -> " + response);

                        processResponse(response);
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
    public void processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            MovieList movieList = gson.fromJson(response, MovieList.class); //movieList에 서버 데이터들이 다 넘어왔다.
            Log.d("MainActivity", "" + movieList.result.size());

            MovieInfo movieInfo = movieList.result.get(position);
            mainTitle.setText(movieInfo.title);
            grade.setText(movieInfo.grade + "세 관람가");
            reservationRate.setText(movieInfo.reservation_rate + "%");
            date.setText(movieInfo.date);

            /* poster = rootView.findViewById(R.id.main_poster); 이미지 다운받기 해야됨. */

        }
    }


}