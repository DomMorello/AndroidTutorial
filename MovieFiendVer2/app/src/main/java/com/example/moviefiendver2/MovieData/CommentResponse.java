package com.example.moviefiendver2.MovieData;

import java.util.ArrayList;

//서버에서 response 정보를 담는 클래스
public class CommentResponse {

    public String message;
    public int code;
    public String resultType;
    public int totalCount;
    public ArrayList<CommentItem> result = new ArrayList<>();
}
