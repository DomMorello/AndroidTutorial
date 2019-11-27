package com.example.myparcelable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent passedIntent = getIntent();  //넘겨 받은 intent를 얻어와서
        processIntent(passedIntent);    //process한다
    }

    private void processIntent(Intent intent){
        if(intent != null){
            ArrayList<String> names = (ArrayList<String>)intent.getSerializableExtra("names");  //getSerializableExtra 사용할 수 있는 이유는 ArrayList가 serializable 인터페이스를 구현했기 때문에 가능하다.
            if(names != null){
                Toast.makeText(getApplicationContext(),"전달받은 이름 리스트 개수: "+names.size(),Toast.LENGTH_SHORT).show();
            }

            SimpleData data = (SimpleData) intent.getParcelableExtra("data");
            if(data != null){
                Toast.makeText(getApplicationContext(),"전달받은 SimpleData: "+data.message,Toast.LENGTH_LONG).show();
            }
        }
    }
}
