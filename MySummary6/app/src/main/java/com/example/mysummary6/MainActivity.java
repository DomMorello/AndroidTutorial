package com.example.mysummary6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppHelper.openDatabase(getApplicationContext(), "movie");
        AppHelper.createTable("outline");
        AppHelper.insertData(2,"df","dsg","sdfg",3.4f,3.3f,2.8f,4.5f,2,3,"df","dffss");
        AppHelper.selectData("outline");
        AppHelper.updateData(6,"바보","바보","바보",6.6f,6.6f,6.6f,6.6f,6,6,"바보","바보");
        AppHelper.selectData("outline");
        if(AppHelper.isMovieExsist("outline",6)){
            Log.d("mmmm","outline 테이블에 id 6인 레코드가 있음!");
        }else{
            Log.d("mmmm","outline 테이블에 id 6인 레코드가 없음!");
        }
    }
}
