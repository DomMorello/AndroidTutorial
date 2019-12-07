package com.example.moviefiendver2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//델타보이즈 화면 프래그먼트
public class MainFragmentDelta extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_delta, container, false);
        Button informationButton = rootView.findViewById(R.id.information_button);
        TextView title = rootView.findViewById(R.id.textView2);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentCallback != null){
                    fragmentCallback.onFragmentChange(0);   //상세화면 프래그먼트 실행
                    //프래그먼트끼리 직접 호출하면 안 되므로 MainActivity에 정의한 메서드를 통해 호출한다.
                }
            }
        });
        return rootView;
    }
}