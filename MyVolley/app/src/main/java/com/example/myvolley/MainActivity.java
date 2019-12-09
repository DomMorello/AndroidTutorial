package com.example.myvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


    }

    public void sendRequest(){
        String url = "http://www.google.co.kr";
        StringRequest request = new StringRequest(
                Request.Method.GET, //get방식으로 요청하겠다
                url,
                new Response.Listener<String>() {   //응답을 문자열로 받아서 여기에 넣겠다.
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> :" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {    //중간에 에러가 발생할 경우 이 메소드가 호출됨
                        println("에러: " + error.getMessage());
                    }
                }
        //GET이 아니라 POST 방식으로 파라미터를 넣고 싶다고 할 경우에는
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        request.setShouldCache(false);  //매번 받은 결과를 그대로 보여주세요(이전 결과가 있더라도 새로 요청해서 응답을 보여주게 됨)
        AppHelper.requestQueue.add(request);    //이렇게 넣을 때 Volley 내부에서 캐싱을 한다
        println("요청 보냄.");
    }

    public void println(String data){
        textView.append(data + "\n");
    }
}
