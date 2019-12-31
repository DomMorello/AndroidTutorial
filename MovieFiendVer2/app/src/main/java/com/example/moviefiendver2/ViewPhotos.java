package com.example.moviefiendver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ViewPhotos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        PhotoView photoView = findViewById(R.id.photoView);

        Intent passedIntent = getIntent();
        String photo = passedIntent.getStringExtra("photo");    //상세보기에서 보낸 인텐트 사진 url정보를 받아온다.

//        ImageLoadTask imageLoadTask = new ImageLoadTask(photo,photoView);   //imageView로 돼있는데 photoView가 될까? 된다.
//        imageLoadTask.execute();
        Glide.with(getApplicationContext()).load(photo).into(photoView);
    }
}
