package com.example.moviefiendver2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//델타보이즈 화면 프래그먼트
public class MainFragmentDelta extends Fragment {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_fragment_delta,container,false);
        Button informationButton = rootView.findViewById(R.id.information_button);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 버튼을 클릭하면 상세화면이 떠야함. 프래그먼트에서 프래그먼트를 불러와야 함 */
            }
        });
        return rootView;
    }
}