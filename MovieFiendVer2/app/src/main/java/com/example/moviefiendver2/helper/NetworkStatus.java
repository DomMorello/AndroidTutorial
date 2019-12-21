package com.example.moviefiendver2.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStatus {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

    //인터넷 연결이 와이파이, 모바일데이터, 또는 연결이 안 돼있는지 알려주는 메서드
    public static int getConnectivityStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){
                Log.d("NetworkStatus","모바일 인터넷에 연결돼있음");
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){
                Log.d("NetworkStatus","와이파이 인터넷에 연결돼있음");
                return TYPE_WIFI;
            }else{
                Log.d("NetworkStatus","인터넷에 연결돼있지 않음");
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}
