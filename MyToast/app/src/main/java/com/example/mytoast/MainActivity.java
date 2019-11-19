package com.example.mytoast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),"위치가 바뀐 토스트", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.LEFT,200,200);
                toast.show();
            }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toastborder, (ViewGroup)findViewById(R.id.toast_layout_root));
                TextView text = (TextView)layout.findViewById(R.id.text);
                text.setText("모양을 바꾼 토스트");
                Toast toasts = new Toast(getApplicationContext());
                toasts.setGravity(Gravity.CENTER, 0, -100);
                toasts.setDuration(Toast.LENGTH_LONG);
                toasts.setView(layout);
                toasts.show();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "스낵바입니다.",Snackbar.LENGTH_LONG).show();

            }//스낵바 사용하려면 외부 라이브러리인 design을 사용해야 한다.
        });
    }
}
