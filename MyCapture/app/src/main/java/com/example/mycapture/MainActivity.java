package com.example.mycapture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    CameraSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "권한이 승인됨.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "카메라 권한이 없음.", Toast.LENGTH_SHORT).show();

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                Toast.makeText(this, "권한 설명이 필요함.", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
            }
        }

        imageView = findViewById(R.id.imageView);
        surfaceView = findViewById(R.id.surfaceView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "사용자가 승인함.", Toast.LENGTH_SHORT).show();
                    }else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "사용자가 승인을 거부함.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "권한을 부여받지 못함.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void capture(){
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;   //8분의 1 크기로 만들어라.
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                imageView.setImageBitmap(bitmap);

                camera.startPreview();
            }
        });
    }
}
