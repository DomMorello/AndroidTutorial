package com.example.moviefiendver2.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;
    }
}
