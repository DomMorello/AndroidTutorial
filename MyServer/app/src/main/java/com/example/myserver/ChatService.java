package com.example.myserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//서버를 액티비티로 구현하면 액티비티스택에 뒤로 쌓이면서 시스템에서 리소스가 부족해지면
//시스템이 서버 액티비틸를 중지시켜버릴 수가 있다. 그래서 서비스로 구현하는 것이 좋다.

public class ChatService extends Service {
    public ChatService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ServerThread thread = new ServerThread();
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class ServerThread extends Thread{
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
