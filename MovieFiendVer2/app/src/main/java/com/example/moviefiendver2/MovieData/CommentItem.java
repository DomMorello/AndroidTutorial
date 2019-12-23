package com.example.moviefiendver2.MovieData;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem {

    public int id;  //한줄평 고유 번호
    public String writer;   //한줄평 작성자 아이디
    public int movieId; //한줄평 해당 영화 인덱스
    public String writer_image; //한줄평 작성자 프로필
    public String time; //한줄평 작성 시간
    public int timestamp;   //작성 시간
    public float rating;    //한줄평 작성시 메긴 평점
    public String contents; //한줄평 내용
    public int recommend;   //한줄평 추천받은 수

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setWriter_image(String writer_image) {
        this.writer_image = writer_image;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", movieId=" + movieId +
                ", writer_image='" + writer_image + '\'' +
                ", time='" + time + '\'' +
                ", timestamp=" + timestamp +
                ", rating=" + rating +
                ", contents='" + contents + '\'' +
                ", recommend=" + recommend +
                '}';
    }

    //    String tmpid;
//    String comment;
//    float tmprating;   //평점
//
//    public CommentItem(String ids, String comment, float rating) {
//        this.tmpid = ids;
//        this.comment = comment;
//        this.tmprating = rating;
//    }
//
//    public String getTmpid() {
//        return tmpid;
//    }
//
//    public void setId(String ids) {
//        this.tmpid = ids;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public float getRating(){
//        return tmprating;
//    }
//
//    public void setRating(float rating){
//        this.tmprating = rating;
//    }
//
//    @Override
//    public String toString() {
//        return "CommentItem{" +
//                "id='" + tmpid + '\'' +
//                ", comment='" + comment + '\'' +
//                ", rating=" + tmprating +
//                '}';
//    }
//
//    public CommentItem(Parcel src){
//        tmpid = src.readString();
//        comment = src.readString();
//        tmprating = src.readFloat();
//    }
//
//    public static final Creator CREATOR = new Creator(){
//
//        @Override
//        public CommentItem createFromParcel(Parcel parcel) {
//            return new CommentItem(parcel);
//        }
//
//        @Override
//        public CommentItem[] newArray(int i) {
//            return new CommentItem[i];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(tmpid);
//        parcel.writeString(comment);
//        parcel.writeFloat(tmprating);
//    }
}

