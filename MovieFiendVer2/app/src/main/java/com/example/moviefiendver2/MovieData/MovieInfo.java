package com.example.moviefiendver2.MovieData;

import android.os.Parcel;
import android.os.Parcelable;

//Activity(MainActivity)에서 Fragment(MovieListFragment)로 데이터를 보내주기 위해서 Parcelable을 구현함.
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

}
