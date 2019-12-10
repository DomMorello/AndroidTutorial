package com.example.moviefiendver2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

//델타보이즈 화면 프래그먼트
public class MainFragmentMovie extends Fragment {

    FragmentCallback fragmentCallback;  //인터페이스 참조
    ImageView poster;
    TextView mainTitle;
    TextView reservationRate;
    TextView grade;
    TextView date;

    public ImageView getPoster() {
        return poster;
    }

    public TextView getMainTitle() {
        return mainTitle;
    }

    public TextView getReservationRate() {
        return reservationRate;
    }

    public TextView getGrade() {
        return grade;
    }

    public TextView getDate() {
        return date;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //인터페이스를 구현했으면 해당 activity에 context를 참조한다.
        if(context instanceof FragmentCallback){
            fragmentCallback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(fragmentCallback != null){
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
                if(fragmentCallback != null){
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

        return rootView;
    }
}