
package com.example.myhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HandshakeCompletedEvent;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    String urlStr;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlStr = editText.getText().toString();

                RequestThread thread = new RequestThread();
                thread.start();
            }
        });
    }

    class RequestThread extends Thread{
        @Override
        public void run() {

            try {
                URL url = new URL(urlStr);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000);  //10초 동안 아무 응답이 없으면 그냥 끝난다.
                    conn.setRequestMethod("GET");   //request 방식을 GET 방식으로 요청한다.
                    conn.setDoInput(true);
                    conn.setDoOutput(true); //입출력이 다 가능하도록 한다.  //output 서버로 보낸다, input 서버에서 받는다
                    int resCode = conn.getResponseCode(); //연결

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));   //데이터를 받는 통로를 만든다.
                    String line = null;

                    while(true){
                        line = reader.readLine();
                        if(line == null){
                            break;  //다 읽었으면 그만 반복해라.
                        }

                        println(line);
                    }

                    reader.close();
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });

    }
}
