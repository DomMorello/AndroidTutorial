package com.example.mylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,"onCreate()호출됨",Toast.LENGTH_SHORT).show();
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"onDestroy()호출됨",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"onStart()호출됨",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"onStop()호출됨",Toast.LENGTH_SHORT).show();
    }

    @Override
    //일반적으로 onPause상태에서 필요한 데이터를 저장함.
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"onPause()호출됨",Toast.LENGTH_SHORT).show();

        //sharedpreferences
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name","소녀시대");
        editor.commit();
    }

    @Override
    //앱을 다시 실행시켰을 때 onResume이 호출되면서 아래 SharedPrefernces 에 저장된 값이 복구된다.
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume()호출됨",Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        if(pref != null){
            String name = pref.getString("name","");
            Toast.makeText(this,"복구된 이름: "+name, Toast.LENGTH_SHORT).show();
        }
    }
}
