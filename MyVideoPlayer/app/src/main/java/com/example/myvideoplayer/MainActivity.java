package com.example.myvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;

    public static String url = "http://sites.google.com/site/ubiaccessmobile/sample_video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);   //리모컨같은 역할
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();   //동영상 파일에 대한 일부 정보를 가져온다.
        //이것도 준비과정이 필요한데 준비과정이 끝나야 실행할 수 있다. 그러므로 아래 코드처럼 준비과정이 끝났는지 확인하는 코드를 넣어야 한다.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "동영상 준비됨.", Toast.LENGTH_SHORT).show();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
    }
}
