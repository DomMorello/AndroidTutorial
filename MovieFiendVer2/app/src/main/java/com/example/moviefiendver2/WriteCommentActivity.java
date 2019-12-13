package com.example.moviefiendver2;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefiendver2.MovieData.CommentItem;
import com.example.moviefiendver2.MovieData.CommentResponse;
import com.example.moviefiendver2.helper.AppHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class WriteCommentActivity extends AppCompatActivity {

    TextView title; //작성하기 액티비티에서 영화제목 뷰
    ImageView grade; //몇세관람가 아이콘
    int position;   //서버에서 접근할 영화의 인덱스
    CommentResponse commentResponse;    //다른 메소드에서도 사용해야 하므로 여기에 선언
    float rating;
    String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        title = findViewById(R.id.write_comment_title);
        grade = findViewById(R.id.write_comment_rated);

        Intent passedIntent = getIntent();  //상세보기 화면에서 보내준 intent를 받아서
        String passedTitle = passedIntent.getStringExtra("title");    //부가data인 영화제목을 받아서
        position = passedIntent.getIntExtra("position",0);   //상세화면, 모두보기에서 받아온 position을 이 activity에서 사용한다.
        title.setText(passedTitle);   //이 액티비티 화면xml에 영화제목을 표시한다.

        //이미지 받아오기
        Bundle extras = getIntent().getExtras();
        int i = extras.getInt("integer");
        double d = extras.getDouble("double");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        grade.setImageBitmap(bitmap);   //받아온 이미지로 세팅

        //취소 버튼을 눌렀을 때
        final Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMessage();
            }
        });

        //저장 버튼을 눌렀을 때
        Button saveButton = findViewById(R.id.saveButton);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final EditText editText = findViewById(R.id.editText);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class); -> 서버에 저장하면 되니까 intent필요없음.
                rating = ratingBar.getRating();
                comment = editText.getText().toString();

                //평점과 한줄평을 입력하지 않았을 때
                if(rating < 0.5f && comment.length() < 1){
                    Toast.makeText(getApplicationContext(), "한줄평과 평점을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                //한줄평만 입력했을 때
                else if(rating < 0.5f && comment.length() >= 1){
                    Toast.makeText(getApplicationContext(), "평점을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                //평점만 입력했을 때
                else if(rating >= 0.5f && comment.length() < 1){
                    Toast.makeText(getApplicationContext(), "한줄평을 작성해 주세요.", Toast.LENGTH_SHORT).show();
                }

                else{
                    //모두 다 입력했을 때 저장버튼을 누르면 Intent를 전달하면서 activity를 종료한다. -> 서버에 한줄평을 저장하면 되므로 인텐트를 전달해줄 필요가 없다.
//                    intent.putExtra("rating", rating);  //작성한 한줄평에서 평점을 메인에 전달한다.
//                    intent.putExtra("comment", comment);    //작성한 한줄평 내용을 메인에 전달한다.
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
                    /* 서버에 한줄평을 저장하는 코드가 들어가야 함 */
                    if (AppHelper.requestQueue == null) {
                        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                    }

                    //커멘트를 작성해서 서버로 보내는 메소드
                    sendCommentToServer();

                }
            }
        });
    }

    //서버에 한줄평 데이터를 저장하는 메소드
    public void sendCommentToServer() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/createComment";  //한줄평 작성 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WriteCommentActivity", "작성하기에서 한줄평 작성을 시도함: " + response);

                        //받아온 JSON데이터를 GSON을 이용해 파싱해서 처리한다.
//                        processCommentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WriteCommentActivity", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(position));    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화의 한줄평정보가 넘어온다.
                /* 이 부분에서 한줄평에 들어갈 params를 짜야한다. */
                params.put("writer","domMorello");
                params.put("rating",Float.toString(rating));
                params.put("contents",comment);
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("WriteCommentActivity", "작성하기에서 한줄평 정보 서버에 저장 요청함");

    }

    /* response로 status가 온다는데 그걸 확인해서 로그를 찍어보자! */
    public void processCommentResponse(String response) {
        Gson gson = new Gson();

        CommentResponse info = gson.fromJson(response, CommentResponse.class);
        if (info.message.equals("movie createComment 성공")) {
            commentResponse = gson.fromJson(response, CommentResponse.class);
            Log.d("ReadMoreActivity", "테스트중: " + commentResponse.result.size()); //서버상 list가 여러개이므로 전부(10개로 세팅) 다 온다.

//            //서버에 default로 지정된 최근 10개 한줄평을 다 얻어와서 commentItem에 세팅한 후 10개를 전부 어댑터 내부에 있는 commentItems List에 add한다.
//            if (inCommentItems.size() == 0) {
//                for (int i = 0; i < commentResponse.result.size(); i++) {
//                    CommentItem commentItem = new CommentItem();    //CommentItem객체를 생성해서
//                    //아이템의 각 필드에 서버에서 얻어온 데이터를 set한다.
//                    commentItem.setWriter(commentResponse.result.get(i).writer);
//                    commentItem.setTime(commentResponse.result.get(i).time);
//                    commentItem.setRating(commentResponse.result.get(i).rating);
//                    commentItem.setContents(commentResponse.result.get(i).contents);
//                    commentItem.setRecommend(commentResponse.result.get(i).recommend);
//                    inCommentItems.add(commentItem);  //어댑터에 들어갈 items ArrayList에 추가한다.
//                }
//            }
//            inCommentAdapter.notifyDataSetChanged();
//            Log.d("ReadMoreActivity", "어댑터 리스트에 추가: " + inCommentItems.size());
//
//            //1. 뒤로 가기를 누르거나 프래그먼트를 나갈 때 사이즈 0으로 초기화 해줘야 한다.
//            //2. size가 0이면 반복문을 실행하게 한다.(Test)->일단 되는데 나중에 서버에 한줄평을 저장하고 난 후에는 어떻게 될지 봐야된다.

        }
    }

    //뒤로가기 버튼을 눌렀을 때 액티비티 종료 오버라이드한 이유? ForResult라서 Result값을 세팅해주기 위해 -> 서버에 저장하면 되니까 일단 없어도 된다
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();    //이것을 없앤 이유는 super를 통해 액티비티를 종료해버리기 때문에 setResult가 효과가 없어짐. (내 생각)
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        setResult(Activity.RESULT_OK, intent);  //startActivityForResult로 불러냈기 때문에 결과를 세팅해줘야 한다.
//        finish();   //아무 데이터를 저장하지 않고 액티비티를 종료한다.
//    }

    //취소 버튼을 눌렀을 때 정말 취소하는 건지 물어보는 알림 대화상자
    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("취소");
        builder.setMessage("한줄평 작성을 취소하시겠습니까?");
        builder.setIcon(R.drawable.announcement);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();    //뒤로가기 버튼과 같은 코드
            }
        });

        //아니오 버튼을 누르면 아무 것도 실행하지 않고 알림상자만 꺼진다.
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
