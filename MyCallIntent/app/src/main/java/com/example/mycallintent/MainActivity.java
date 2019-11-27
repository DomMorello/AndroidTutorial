package com.example.mycallintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiver = editText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+receiver));
                startActivity(intent);

                /*Intent intent1 = new Intent();
                ComponentName name = new ComponentName("com.example.mycallintent", "com.example.mycallintent.MenuActivity");
                intent1.setComponent(name);
                startActivity(intent1);*/
                //위 코드는 메뉴 액티비티를 띄우고 싶다면 MenuActivity.class 라는 클래스 인스턴스를 인텐트의 파라미터로 넘겨주는 방법과 그 외에도 ComponentName 객체를 만들어 설정하는 방법이 있습니다.
                //메뉴 액티비티를 띄우고 싶다면 MenuActivity.class 라는 클래스 인스턴스를 인텐트의 파라미터로 넘겨주는 방법과 그 외에도 ComponentName 객체를 만들어 설정하는 방법이 있습니다.
            }
        });
    }
}
