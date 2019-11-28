package com.example.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "myService";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate() 호출됨");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy() 호출됨");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() 호출됨");

        if(intent == null ){
            return Service.START_STICKY;    //sticky 끈적, 서비스가 종료되어도 다시 자동으로 실행해달라고 하는 옵션
        }else{
            processIntent(intent);
        }

        return super.onStartCommand(intent, flags, startId);


    }

    private void processIntent(Intent intent){
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        Log.d(TAG,"전달받은 데이터: "+command+","+name);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent showIntent = new Intent(getApplicationContext(),MainActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|  //서비스는 화면이 없으므로 플래그 옵션을 줘야 한다.
                            Intent.FLAG_ACTIVITY_SINGLE_TOP|    //만들어져있는 액티비티를 재활용하는 옵션 플래그
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);    //만약 그 위에 다른 화면이 있으면 그 화면들을 제거해주는 역할을 하는 플래그
        showIntent.putExtra("command","show");
        showIntent.putExtra("name",name+" from service");
        startActivity(showIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
