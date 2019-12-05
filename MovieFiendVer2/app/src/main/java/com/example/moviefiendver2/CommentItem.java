package com.example.moviefiendver2;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem implements Parcelable {

    String id;
    String comment;
    float rating;   //평점

    public CommentItem(String id, String comment, float rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating(){
        return rating;
    }

    public void setRating(float rating){
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }

    public CommentItem(Parcel src){
        id = src.readString();
        comment = src.readString();
        rating = src.readFloat();
    }

    public static final Creator CREATOR = new Creator(){

        @Override
        public CommentItem createFromParcel(Parcel parcel) {
            return new CommentItem(parcel);
        }

        @Override
        public CommentItem[] newArray(int i) {
            return new CommentItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(comment);
        parcel.writeFloat(rating);
    }
}

