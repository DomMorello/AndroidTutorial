package com.example.myfragment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ListFragment fragment1;
    ViewerFragment fragment2;

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        fragment1 = (ListFragment) manager.findFragmentById(R.id.listFragment); //main xml에 태그를 통해 추가를 해줬기 때문에 불러올 수 있다.
        fragment2 = (ViewerFragment) manager.findFragmentById(R.id.viewerFragment);

    }

    public void onImageChange(int index){
        fragment2.setImage(index);
    }
}
