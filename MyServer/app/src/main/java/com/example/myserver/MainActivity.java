package com.example.myserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ServerThread thread = new ServerThread();
//                thread.start();
                Intent intent = new Intent(getApplicationContext(), ChatService.class);
                startService(intent);
            }
        });
    }

    /*class ServerThread extends Thread{
        @Override
        public void run() {
            int port = 5001;
            try {
                ServerSocket server = new ServerSocket(port);
                Log.d("ServerThread", "서버가 실행됨.");

                while(true){
                    Socket socket = server.accept();    //소켓 객체를 통해 클라이언트가 요청한 정보를 받을 수 있음
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());    //들어오는 데이터를 받는다
                    Object input = inputStream.readObject();
                    Log.d("ServerThread","input : "+input);

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(input + " from Server");
                    outputStream.flush();   //버퍼가 남아있을 수 있으므로 사용한 후에는 꼭 flush
                    Log.d("ServerThread","output 보냄.");

                    socket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }*/
}
