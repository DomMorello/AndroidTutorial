package com.example.mysocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientThread thread = new ClientThread();
                thread.start();
            }
        });


    }

    class ClientThread extends Thread{
        @Override
        public void run() {
            String host = "localhost";
            int port = 5001;
            try {
                Socket socket = new Socket(host,port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject("안녕!");    //서버로 데이터를 보낸다
                outputStream.flush();
                Log.d("ClientThread","서버로 보냄.");

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final Object input = inputStream.readObject();
                Log.d("ClientThread","받은데이터: "+input);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("받은데이터: "+input);
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
