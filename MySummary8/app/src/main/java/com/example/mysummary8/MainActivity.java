package com.example.mysummary8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Animation translateUp;
    Animation translateDown;

    LinearLayout menuContainer;
    boolean isShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        translateUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_up);
        translateDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_down);

        translateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menuContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuContainer = findViewById(R.id.menuContainer);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShown){
                    menuContainer.startAnimation(translateUp);
                }else{
                    menuContainer.setVisibility(View.VISIBLE);
                    menuContainer.startAnimation(translateDown);
                }
                isShown = !isShown;
            }
        });


    }
}
