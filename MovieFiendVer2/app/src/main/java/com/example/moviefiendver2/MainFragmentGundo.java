package com.example.moviefiendver2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//군도 화면 프래그먼트
public class MainFragmentGundo extends Fragment {

    FragmentCallback fragmentCallback;  //인터페이스 참조

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_gundo,container,false);
        Button informationButton = rootView.findViewById(R.id.information_button);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentCallback != null){
                    fragmentCallback.onFragmentChange(2);   //상세화면 프래그먼트 실행
                    //프래그먼트끼리 직접 호출하면 안 되므로 MainActivity에 정의한 메서드를 통해 호출한다.
                }
            }
        });

        return rootView;
    }
}