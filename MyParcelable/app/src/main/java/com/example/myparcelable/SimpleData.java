package com.example.myparcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleData implements Parcelable {

    int number;
    String message;

    public SimpleData(int number, String message) {
        this.number = number;
        this.message = message;
    }

    public SimpleData(Parcel src){
        number = src.readInt();
        message = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public SimpleData createFromParcel(Parcel src) {
            return new SimpleData(src);
        }

        @Override
        public SimpleData[] newArray(int i) {
            return new SimpleData[i];
        }
        /*public SimpleData createFromParcel(Parcel src){
            return new SimpleData(src);
        }

        public SimpleData[] newArray(int size){
            return new SimpleData[size];
        }*/
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override   //SimpleData를 Parcel로 바꿔주는 역할
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeString(message);
    }
}
