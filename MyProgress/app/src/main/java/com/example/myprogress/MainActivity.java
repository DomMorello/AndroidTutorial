package com.example.myprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    CompletionThread completionThread;
    String msg = "";

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*handler.postDelayed(new Runnable() {    //이 밑에 코드를 일정시간이 지나고 나서 실행되게 하는 메소드
                    @Override
                    public void run() {
                        ProgressThread thread = new ProgressThread();
                        thread.start();
                    }
                },5000);*/
                ProgressTask task = new ProgressTask();
                task.execute("시작"); //이 문자열이 doInBackground메소드로 전달이 된다.
            }
        });
        completionThread = new CompletionThread();
        completionThread.start();   //완료 쓰레드를 실행
    }

    class ProgressTask extends AsyncTask<String, Integer, Integer>{
        //지네릭 타입이 아래 3개 메서드 순서대로 적용된다.
        int value = 0;

        //쓰레드 안에서 작동할 코드를 이 메소드 안에 작성해라
        //이 메소드에서 return하는 타입이 onPostExecute에 전달된다. 그래서 반환타입이 Integer이다.
        @Override
        protected Integer doInBackground(String... strings) {
            while(true){
                if(value > 100){
                    break;
                }
                value += 1;

                publishProgress(value); //onProgressUpdate를 자동으로 호출한다.

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return value;
        }   //이 과정이 다 끝나면 onPostExecute 메소드가 호출된다.

        //UI를 업데이트 하고 싶으면 이 메소드 안에 코드 작성
        //핸들러에서 UI 업데이트하듯이 이 메소드 안에서 실행이 되는 것임
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0].intValue());  //doInBackground메소드 안에서 publishProgress(value) 메소드에서 넘어온
                                                            //value값을 받아와서 int 값으로 바꿔준다.
        }


        //쓰레드가 작업을 끝내면 자동으로 호출됨
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Toast.makeText(MainActivity.this, "완료됨", Toast.LENGTH_SHORT).show();
        }


    }

    //ProgrssBar 진행되게 하는 Thread
    class ProgressThread extends Thread{
        int value = 0;

        @Override
        public void run() {
            while(true){
                if(value > 100){
                    break;
                }
                value += 1;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(value);
                    }
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //완료 쓰레드에 접근해서 완료 핸들러를 통해 UI에 접근한다.
            completionThread.completionHandler.post(new Runnable() {
                @Override
                public void run() {
                    msg = "OK";

                    Log.d("MainActivity","메시지: "+msg);
                }
            });
        }
    }

    //완료 쓰레드
    class CompletionThread extends Thread{
        public Handler completionHandler = new Handler();   //쓰레드 안에 핸들러 객체 생성

        @Override
        public void run() {
            Looper.prepare();   //Looper...대기 하고 있는다
            Looper.loop();
        }
    }
}
