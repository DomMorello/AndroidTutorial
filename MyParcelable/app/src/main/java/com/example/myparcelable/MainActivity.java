package com.example.myparcelable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                ArrayList<String> names = new ArrayList<>();
                names.add("이동현");
                names.add("김동현");
                intent.putExtra("names",names); //ArrayList객체 자체를 extra data로 넘겨준다.

                SimpleData data = new SimpleData(100, "Hollo");
                intent.putExtra("data",data);

                startActivityForResult(intent, 101);    //requestCode를 포함해서 새로운 activity를 실행하는 intent

            }
        });

    }

}
