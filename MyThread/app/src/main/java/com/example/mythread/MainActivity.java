package com.example.mythread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
//    int value = 0;
    ValueHandler handler = new ValueHandler();  //내부 클래스로 정의한 핸들러 클래스

    Handler handler2 = new Handler();   //두 번째 방법, 핸들러를 아예 객체로 생성해서 사용한다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BackGroundThread thread = new BackGroundThread();
//                thread.start();
                new Thread(new Runnable() { //일회용으로 사용할 수 있는 방법
                    boolean running = false;
                    int value = 0;

                    @Override
                    public void run() {
                        running = true;
                        while(running) {
                            value += 1;

                            //객체로 생성한 핸들러를 이용해서 스레드를 바로 사용
                            handler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("현재 값: "+value);
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }).start(); //스레드 start
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //내부 클래스로 스레드를 정의
    class BackGroundThread extends Thread{
        boolean running = false;
        int value = 0;

        @Override
        public void run() {
            running = true;
            while(running){
                value += 1;

                //핸들러에 data를 전달한다.
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                message.setData(bundle);
                handler.sendMessage(message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ValueHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {

            //스레드에서 받은 데이터로 메인 UI에 접근할 수 있다
            Bundle bundle = msg.getData();
            int value = bundle.getInt("value");
            textView.setText("현재 값"+value);
        }
    }
}
