package com.example.myfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout button1 = (LinearLayout)findViewById(R.id.button1);
        LinearLayout button2 = (LinearLayout)findViewById(R.id.button2);
        LinearLayout button3 = (LinearLayout)findViewById(R.id.button3);
        LinearLayout button4 = (LinearLayout)findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter_anim2,R.animator.exit_anim2).replace(R.id.main_frame,new MyFragment1()).commit();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter_anim2,R.animator.exit_anim2).replace(R.id.main_frame,new MyFragment2()).commit();

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter_anim,R.animator.exit_anim).replace(R.id.main_frame,new MyFragment3()).commit();

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter_anim,R.animator.exit_anim).replace(R.id.main_frame,new MyFragment4()).commit();

            }
        });


    }
}
