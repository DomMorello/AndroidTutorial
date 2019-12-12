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

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

public class WriteCommentActivity extends AppCompatActivity {

    TextView title; //작성하기 액티비티에서 영화제목 뷰
    ImageView grade; //몇세관람가 아이콘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        title = findViewById(R.id.write_comment_title);
        grade = findViewById(R.id.write_comment_rated);

        Intent passedIntent = getIntent();  //상세보기 화면에서 보내준 intent를 받아서
        String passedTitle = passedIntent.getStringExtra("title");    //부가data인 영화제목을 받아서

        //이미지 받아오기
        Bundle extras = getIntent().getExtras();
        int i = extras.getInt("integer");
        double d = extras.getDouble("double");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        grade.setImageBitmap(bitmap);   //받아온 이미지로 세팅

        title.setText(passedTitle);   //이 액티비티 화면xml에 영화제목을 표시한다.

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                float rating = ratingBar.getRating();
                String comment = editText.getText().toString();

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
                //모두 다 입력했을 때 저장버튼을 누르면 Intent를 전달하면서 activity를 종료한다.
                else{
                    intent.putExtra("rating", rating);  //작성한 한줄평에서 평점을 메인에 전달한다.
                    intent.putExtra("comment", comment);    //작성한 한줄평 내용을 메인에 전달한다.
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    //뒤로가기 버튼을 눌렀을 때 액티비티 종료 오버라이드한 이유? ForResult라서 Result값을 세팅해주기 위해
    @Override
    public void onBackPressed() {
//        super.onBackPressed();    //이것을 없앤 이유는 super를 통해 액티비티를 종료해버리기 때문에 setResult가 효과가 없어짐. (내 생각)
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(Activity.RESULT_OK, intent);  //startActivityForResult로 불러냈기 때문에 결과를 세팅해줘야 한다.
        finish();   //아무 데이터를 저장하지 않고 액티비티를 종료한다.
    }

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
