package com.example.myintent1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        /*
           id값은 서로 다른 activity에서는 중복이 돼도 서로 충돌하지 않는다.
           마찬가지로 서로 다른 activity라면 참조변수 이름도 중복되도 된다.
        */
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name","mike");   //extra data는 OS에서는 무시하지만 다른 activity로 Data를 전달할 수 있음.

                setResult(Activity.RESULT_OK, intent);  //이를 통해서 결과를 전달해준다. 그럼 호출한 Activity에서 정의한 응답을 받을 수 있는 메소드(onActivityResult())를 통해 결과 Data를 전달받는다.

                finish();
                /*
                 * 메인화면에서 이 activity를 띄우면 메인화면은 뒤에 깔려있다고 생각하면 된다.
                 * 이를 activity Stack이라고 하는 데서 관리를 한다.
                 * */
            }
        });
    }
}
