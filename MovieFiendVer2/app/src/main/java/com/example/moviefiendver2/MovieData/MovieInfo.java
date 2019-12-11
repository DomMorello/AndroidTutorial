package com.example.moviefiendver2.MovieData;

import android.os.Parcel;
import android.os.Parcelable;

//서버에서 GSON으로 JSON을 읽어내기 위해 정의한 class
public class MovieInfo {

    public int id;
    public String title;
    public String title_eng;
    public String date;
    public float user_rating;
    public float audience_rating;
    public float reviewer_rating;
    public float reservation_rate;
    public int reservation_grade;
    public int grade;
    public String thumb;
    public String image;
    public String photos;
    public String videos;
    public String outlinks;
    public String genre;
    public int duration;
    public int audience;
    public String synopsis;
    public String director;
    public String actor;
    public int like;
    public int dislike;
}
