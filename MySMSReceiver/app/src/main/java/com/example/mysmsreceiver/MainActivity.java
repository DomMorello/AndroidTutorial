package com.example.mysmsreceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission이 돼있는지 안돼있는지 확인하는 코드
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"SMS수신 권한 있음",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"SMS수신 권한 없음",Toast.LENGTH_SHORT).show();

            //권한설명이 필요하면
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){
//                Toast.makeText(this,"SMS권한 설명이 필요함",Toast.LENGTH_SHORT).show();
//            }else{    //이 부분을 주석처리하면 앱을 실행시킬때마다 권한을 요청하는 대화상자를 띄움.
                //시스템에 권한을 요청하는 코드
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS},1);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"사용자가 권한을 승인함",Toast.LENGTH_SHORT).show();
                    }else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this,"사용자가 권한을 거부함",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this,"SMS권한을 부여받지 못함",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
