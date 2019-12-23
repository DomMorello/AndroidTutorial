package com.example.moviefiendver2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefiendver2.MovieData.WriteCommentResponse;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.helper.NetworkStatus;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class CommentItemView extends LinearLayout {

    TextView commentContent;
    RatingBar commentRatingBar;
    TextView userId;
    TextView time;
    public Button recommendation;   //public으로 한 이유는 다른 클래스에서 색을 바꾸기 위해
    TextView recommendationNum;
    TextView border;
    Button call;

    int review_id; //한줄평 어댑터에서 이 변수로 값을 전달해주기 위해 선언. TEST
    int recommendation_num; //추천을 누르면 바로 값이 증가한 것이 보여지게 하기 위한 int변수
    int count_pressed;  //추천을 누른 횟수

    public int getCount_pressed() {
        return count_pressed;
    }

    public void setCount_pressed(int count_pressed) {
        this.count_pressed = count_pressed;
    }

    public int getRecommendation_num() {
        return recommendation_num;
    }

    public void setRecommendation_num(int recommendation_num) {
        this.recommendation_num = recommendation_num;
    }

    @Override
    public int getId() {
        return review_id;
    }

    @Override
    public void setId(int id) {
        this.review_id = id;
    }

    public CommentItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.one_line_comment, this, true);

        commentContent = findViewById(R.id.comment_content);
        commentRatingBar = findViewById(R.id.commentRatingBar);
        userId = findViewById(R.id.user_id);
        time = findViewById(R.id.time);
        recommendation = findViewById(R.id.comment_recommend);
        recommendationNum = findViewById(R.id.comment_recommend_num);
        border = findViewById(R.id.칸막이);
        call = findViewById(R.id.신고하기버튼);

        //리스트뷰 안에 있는 뷰객체들의 textColor를 바꿔줌
        commentContent.setTextColor(Color.WHITE);
        userId.setTextColor(Color.WHITE);
        time.setTextColor(Color.GRAY);
        recommendation.setTextColor(Color.GRAY);
        recommendationNum.setTextColor(Color.GRAY);
        border.setTextColor(Color.GRAY);
        call.setTextColor(Color.GRAY);
        if(count_pressed > 0){  //한 번이라도 추천을 눌렀으면
            recommendation.setTextColor(Color.MAGENTA); //색깔을 이 색으로 해놔라
        }

        recommendation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkStatus.getConnectivityStatus(getContext()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getContext()) == NetworkStatus.TYPE_WIFI){
                    /* 서버에 추천을 저장하는 코드 */
                    //id값이 있어야 추천을 할 수 있다. commentItem에 id값을 세팅을 하긴 했는데 어떻게 여기서 사용할 수 있지..?

                    count_pressed++;    //한 번 누를 때마다 누른 횟수를 증가시킨다.
                    if(count_pressed == 1){  //한 번 이상 누르면 추천이 안 된다.
                        recommendation.setTextColor(Color.MAGENTA); //추천을 누르면 색깔이 바뀐다. 추천을 취소하는 기능은 없다.

                        Log.d("CommentItemView", "과연 getId로 고유id를 받아올까? " + getId());    //된다. 지렸다 ...
                        if (AppHelper.requestQueue == null) {
                            AppHelper.requestQueue = Volley.newRequestQueue(getContext());  //getContext 될까..? 된다.
                        }

                        //추천을 누르면 그 결과를 서버로 보내는 메소드
                        sendRecommendToServer();
                    }
                    /* 추천 관련 문제점: 다양한 상황(프래그먼트 종료 이후, 모두보기 액티비티 실행시마다 초기화 등)에서 추천을 한 번 누르고 나서 한 한줄평 아이템에 대해서 계속 추천을 누를 수가 있다. */
                }else{
                    Toast.makeText(getContext(), "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void sendRecommendToServer() {
        //서버에 한줄평 데이터를 저장하는 메소드
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseRecommend";  //추천 적용 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CommentItemView", "커멘트아이템뷰에서 추천 서버에 요청함: " + response);

                        //받아온 JSON 데이터를 GSON을 이용해 파싱해서 처리한다.
                        processCommentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CommentItemView", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("review_id", Integer.toString(getId()));    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화의 한줄평정보를 나타낸다.
                params.put("writer", "domMorello");  //추천 작성자는 나의 아이디인 domMorello로 고정
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("CommentItemView", "커멘트아이템뷰에서 추천 정보 서버에 저장 요청함");
    }

    /* response로 status가 온다는데 그걸 확인해서 로그를 찍어보자!*/
    public void processCommentResponse(String response) {
        Gson gson = new Gson();

        WriteCommentResponse writeCommentResponse = gson.fromJson(response, WriteCommentResponse.class);
        if (writeCommentResponse.code == 200) {
            Log.d("ReadMoreActivity", "추천 서버에 보내기 성공: " + writeCommentResponse.message);
            Toast.makeText(getContext(), "추천을 적용했습니다.", Toast.LENGTH_SHORT).show();
            recommendationNum.setText(getRecommendation_num() + 1 + "");    //추천을 누르면 바로 서버에 저장된것처럼 보여주기 위해 하나 증가시킨 뷰를 보여줌
        } else {
            Toast.makeText(getContext(), "추천 에러 발생!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setCommentContent(String data) {
        commentContent.setText(data);
    }

    public void setCommentRatingBar(float data) {
        commentRatingBar.setRating(data);
    }

    public void setUserId(String data) {
        userId.setText(data);
    }

    public void setTime(String data) {
        time.setText(data);
    }

    public void setRecommendationNum(String data) {
        recommendationNum.setText(data);    //int로 받으면 오류나려나? 오류나더라. 그래서 String으로 바꿨다.
    }


}