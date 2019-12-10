package com.example.moviefiendver2.helper;

import com.android.volley.RequestQueue;

public class AppHelper {

    public static RequestQueue requestQueue;    //어디서든 접근할 수 있도록 public static으로 requestQueue를 정의

    public static String host = "boostcourse-appapi.connect.or.kr"; //서버 주소
    public static int port = 10000; //서버 포트
}
