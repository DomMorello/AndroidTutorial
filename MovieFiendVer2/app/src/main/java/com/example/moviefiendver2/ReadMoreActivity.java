package com.example.moviefiendver2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefiendver2.MovieData.CommentItem;
import com.example.moviefiendver2.MovieData.CommentResponse;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.helper.NetworkStatus;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadMoreActivity extends AppCompatActivity {

    InCommentAdapter inCommentAdapter;  //내부클래스 어댑터를 사용하기 위해 이 위치에 선언
    ArrayList<CommentItem> inCommentItems = new ArrayList<>();  //다른 메서드에서도 사용하기 위해 여기에 선언
    Button writeCommentButton;  //작성하기 버튼
    TextView title; //모두보기 화면에서 영화 제목뷰
    String name;    //받아온 부가data로 영화뷰에 표시하기 위한 영화제목 String변수
    ImageView rated;    //몇세 관람가 이미지뷰
    RatingBar ratingBar;
    TextView ratingNum;
    TextView participants;
    byte[] byteArray;   //이미지 받아오기 위해 있음
    int position;   //서버에서의 영화 인덱스

    CommentResponse commentResponse;    //다른 액티비티에 넘겨주려면 다른 메소드에서도 사용해야 하므로 여기에 선언

    //onResume에 한 이유: 한줄평을 작성하고 나서 프래그먼트로 다시 돌아왔을 때 작성한 최신화된 한줄평을 보여주기 위해서는
    //프래그먼트가 화면에 보여질 때마다 최신화된 서버정보를 가져오기 위해서 onResume에 작성함.
    @Override
    protected void onResume() {
        super.onResume();

        //인터넷이 있을 때만 서버에 요청해라
        //커멘트 정보를 서버에서 얻어오는 메소드
        if(NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_WIFI || NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_MOBILE){
            requestCommentList();

            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
        }else{
            /* 인터넷이 없을 때 데이터베이스 활용해서 보여주는 코드 */

            //이 아래 코드는 getCommentFromDatabase메소드에서 해당 영화에 해당하는 모든 한줄평 정보를 list에 담아서 갖고 온다.
            //그 이후에 여기 onResume에서 어댑터에 추가해서 getView메소드를 통해 데이터베이스에서 가져온 정보를
            //인터넷이 없을 때 보여주는 역할을 하기 위해서 짜여진 코드이다. 지렸다. -> 메모리 측면에서 좋은 방법인지는 모르겠다.
            ArrayList list = AppHelper.getCommentFromDatabase(position); //데이터베이스에 있는 movieId가 ?인 데이터를 내림차순으로 조회한것 불러옴
            Log.d("FragMovieInfo", "인터넷없을 때 AppHelper에 담은 comment list내용: " + list.toString()); //값이 넘어옴
            Iterator it = list.iterator();
            //commentItems에 계속 item들이 쌓이기 때문에 매번 프래그먼트를 실행할 때마다 원래 데이터가 있으면 clear 해주고 다시 넣어야 한다.
            if (inCommentItems.size() > 0) {
                Log.d("FragMovieInfo","commentItems 안에 데이터가 있어서 클리어 실시함!!!");
                inCommentItems.clear();
            }
            while (it.hasNext()) {
                CommentItem item = (CommentItem) it.next();
                inCommentAdapter.addItem(item);
            }
            Log.d("FragMovieInfo", "!!! 어댑터 내부에 있는 정보 개수: " + inCommentItems.size());

            //인터넷이 없을 때도 몇세관람가 아이콘 이미지는 전달해야 하기 때문에 여기서 해준다.
            //이미지를 전달하기 위해 코드 작성(이미지 축소)
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable)rated.getDrawable()).getBitmap();
            float scale = (float) (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        title = findViewById(R.id.read_more_title);
        rated = findViewById(R.id.read_more_rated);
        ratingBar = findViewById(R.id.read_more_rating_bar);
        ratingNum = findViewById(R.id.read_more_rating_num);
        participants = findViewById(R.id.read_more_participants);


        Intent passedIntent = getIntent();  //전달받은 인텐트를
        if(passedIntent != null){
            name = passedIntent.getStringExtra("title");
            title.setText(name);    //받아온 데이터로 영화제목을 뷰에 표시한다.
            float rating = passedIntent.getFloatExtra("rating",0.0f);
            ratingBar.setRating(rating/2);  //평점바 나누기2를 해야 별에 수치만큼 채워짐
            ratingNum.setText(rating+"");   //평점
            int totalCount = passedIntent.getIntExtra("totalCount",0);  //평점 참여 인원
            participants.setText("("+totalCount+"명 참여)");
//            inCommentItems = (ArrayList<CommentItem>) passedIntent.getSerializableExtra("list"); //CommentItem을 Parcelable 구현해서 받아냄
            //-> 주석처리 이유: 서버에서 받아올 것이기 때문에 따로 받아 올 필요가 없다.

            position = passedIntent.getIntExtra("position",0);   //영화 상세화면에서 받아온 position을 이 activity에서 사용한다.
            Log.d("ReadMoreActivity","모두보기로 넘어온 position값: " + position);

            //이미지 받아오기
            Bundle extras = getIntent().getExtras();
            int i = extras.getInt("integer");
            double d = extras.getDouble("double");
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            rated.setImageBitmap(bitmap);   //받아온 이미지로 세팅

        }

        inCommentAdapter = new InCommentAdapter();  //이게 없어서 안됐었다. 어댑터를 쓰려면 어댑터 객체가 있어야지 당연히!
        ListView commentListView = findViewById(R.id.comment_listview);
        commentListView.setAdapter(inCommentAdapter);   //리스트뷰를 어댑터에 연결해준다.

        //작성하기를 눌렀을 때
        writeCommentButton = findViewById(R.id.writeCommentButton);
        writeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //인터넷이 있을 때만 작성하기 기능을 실행한다.
                if(NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_WIFI){
                    Intent writeCommentIntent = new Intent(getApplicationContext(), WriteCommentActivity.class);

                    writeCommentIntent.putExtra("title", name);    //작성하기 activity에 영화제목을 넘겨줌
                    writeCommentIntent.putExtra("position",position);   //영화 인덱스인 position을 넘겨준다.

                    //이미지를 전달하기 위해 코드 작성(이미지 축소)
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable)rated.getDrawable()).getBitmap();
                    float scale = (float) (1024/(float)bitmap.getWidth());
                    int image_w = (int) (bitmap.getWidth() * scale);
                    int image_h = (int) (bitmap.getHeight() * scale);
                    Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                    resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    //이미지 보내주기
                    writeCommentIntent.putExtra("integer", 300);
                    writeCommentIntent.putExtra("double", 3.141592 );
                    writeCommentIntent.putExtra("image", byteArray);

//                startActivityForResult(writeCommentIntent, 103); //작성하기 activity실행 -> forResult로 할 필요가 없을 것 같아서 일단 지운다.
                    startActivity(writeCommentIntent);
                    //작성하기 액티비티를 실행시키면서 결과를 기대한다.
                }else{
                    Toast.makeText(ReadMoreActivity.this, "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //서버에 한줄평 데이터를 요청하는 메소드
    public void requestCommentList() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";  //한줄평 서버 url

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ReadMoreActivity", "모두보기에서 한줄평 정보 받아옴: " + response);

                        //받아온 JSON데이터를 GSON을 이용해 파싱해서 처리한다.
                        processCommentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ReadMoreActivity", "에러 발생! " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = Integer.toString(position);
                params.put("id", id);    //id값을 서버에 전달하면 ?id=1 이런 식으로 서버에 대입이 돼서 해당 id를 가진 영화의 한줄평정보가 넘어온다.
                params.put("limit",Integer.toString(50));   //전체는 너무 많으니까 50개만 보여줘라
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("ReadMoreActivity", "모두보기에서 한줄평 정보 서버에 요청함");

    }


    public void processCommentResponse(String response) {
        Gson gson = new Gson();

        CommentResponse info = gson.fromJson(response, CommentResponse.class);
        if (info.message.equals("movie readCommentList 성공")) {
            commentResponse = gson.fromJson(response, CommentResponse.class);
            Log.d("ReadMoreActivity", "테스트중: " + commentResponse.result.size()); //서버상 list가 여러개이므로 전부(10개로 세팅) 다 온다.

            //서버에 default로 지정된 최근 10개 한줄평을 다 얻어와서 commentItem에 세팅한 후 10개를 전부 어댑터 내부에 있는 commentItems List에 add한다.
            if (inCommentItems.size() == 0) {
                for (int i = 0; i < commentResponse.result.size(); i++) {
                    CommentItem commentItem = new CommentItem();    //CommentItem객체를 생성해서
                    //아이템의 각 필드에 서버에서 얻어온 데이터를 set한다.
                    commentItem.setWriter(commentResponse.result.get(i).writer);
                    commentItem.setTime(commentResponse.result.get(i).time);
                    commentItem.setRating(commentResponse.result.get(i).rating);
                    commentItem.setContents(commentResponse.result.get(i).contents);
                    commentItem.setRecommend(commentResponse.result.get(i).recommend);
                    commentItem.setId(commentResponse.result.get(i).id);    //id값을 얻어와야 추천할 때 사용할 수 있다.
                    inCommentAdapter.addItem(commentItem);  //어댑터에 들어갈 items ArrayList에 추가한다.
                }
            }
            inCommentAdapter.notifyDataSetChanged();
            Log.d("ReadMoreActivity", "어댑터 리스트에 추가: " + inCommentItems.size());

        }
    }

    //작성하기 액티비티에서 받은 결과로 list에 추가한다. -> 서버에서 받아오면 되니까 일단 지운다.
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        if (requestCode == 103) {
//            float rating = intent.getFloatExtra("rating", 0.0f);
//            String comment = intent.getStringExtra("comment");
//            if (rating > 0.0 && comment.length() > 0) {
//                inCommentAdapter.addItem(new CommentItem("hkkim93", comment, rating)); //한줄평 리스트에 추가하기
//                inCommentAdapter.notifyDataSetChanged();  //변화가 있으면 갱신해라.
//                Snackbar.make(writeCommentButton, "한줄평이 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
//                //추가를 해서 모두보기 화면에서 갱신된 list가 보이도록 한 이후에
//                //뒤로가기 버튼을 누르면 메인 액티비티로 result를 설정해서 list를 부가데이터로 보내준다.
//            }
//        }
//    }

    //뒤로가기 버튼을 눌렀을 때 액티비티 종료 오버라이드한 이유? ForResult라서 Result값을 세팅해주기 위해 -> 서버에서 받으면 되니까 일단 지운다.
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();    //이것을 없앤 이유는 super를 통해 액티비티를 종료해버리기 때문에 setResult가 효과가 없어짐. (내 생각)
//        //뒤로가기 버튼을 누르면 메인 액티비티로 result를 설정해서 list를 부가데이터로 보내준다.
//        //뒤로가기 버튼을 누를 때 list에 변화가 있던 없던 모두보기에 있는 리스트를 다시 메인으로 보내준다.
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("list",inCommentItems);
//        setResult(Activity.RESULT_OK, intent);  //startActivityForResult로 불러냈기 때문에 결과를 세팅해줘야 한다.
//        finish();
//    }

    //모두 보기 클릭시 리스트뷰에 있는 것을 전부 보여주기 위한 어댑터
    class InCommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return inCommentItems.size();
        }

        public void addItem(CommentItem commentItem) {
            inCommentItems.add(commentItem);  //ArrayList에 추가한다.
        }

        @Override
        public Object getItem(int i) {
            return inCommentItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            CommentItemView commentItemView;
            //convertView가 있으면 그 자원을 다시 써서 메모리관리를 효과적으로 한다
            if (convertView == null) {
                commentItemView = new CommentItemView(getApplicationContext());
            } else {
                commentItemView = (CommentItemView) convertView;
            }

            CommentItem commentItem = inCommentItems.get(i);    //서버 연결 이후에는 이렇게 해야 순서대로 나온다.
            // CommentItem commentItem = inCommentItems.get(inCommentItems.size()-1-i); //순서대로 나오지 않고 역순으로 나오게 하려고 사이즈-1에서 i를 뺌
            //이렇게 하면 새로 등록한 한줄평이 제일 위로 나올 수 있게 됨.
            //이거 지렸다...

            if(NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_MOBILE || NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_WIFI){

                //-> 여기다가 하니까 다른 영화 상세화면에 들어갈 때도 잘 되는구만.
                //서버에 정보를 요청할 때마다 Comment data를 받아오는데 조건이 있다.
                //comment 고유 id와 movieId가 동일한 데이터가 데이터베이스에 이미 있다면 update를 해라.
                //근데 이거는 인터넷이 연결돼있을 때만 해라.
                if (AppHelper.isDataExsist(AppHelper.COMMENT, commentResponse.result.get(i).id)) {  //database에 이미 한줄평 id값이 서버에서 넘어오는 id값과 동일한 것이 존재하면(즉, 중복되게 저장되는 것을 피하기 위해)
                    //insert를 해서 중복되게 record를 삽입하지 말고 원래 있던 record를 서버에서 오는 새로운 정보로 update해라
                    AppHelper.updateCommentData(commentResponse.result.get(i).id, commentResponse.result.get(i).writer, commentResponse.result.get(i).movieId, commentResponse.result.get(i).writer_image, commentResponse.result.get(i).time, commentResponse.result.get(i).timestamp, commentResponse.result.get(i).rating, commentResponse.result.get(i).contents, commentResponse.result.get(i).recommend);
                    AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
                } else {
                    //최초로 서버에서 받아오는 거면(즉, 한줄평 id값과 movieId값이 database에 없으면) 새로 record를 만들어서 insert 삽입해라.
                    AppHelper.insertCommentData(commentResponse.result.get(i).id, commentResponse.result.get(i).writer, commentResponse.result.get(i).movieId, commentResponse.result.get(i).writer_image, commentResponse.result.get(i).time, commentResponse.result.get(i).timestamp, commentResponse.result.get(i).rating, commentResponse.result.get(i).contents, commentResponse.result.get(i).recommend);
                    AppHelper.selectData(AppHelper.COMMENT);    //로그찍기
                }

                //CommentItem에 서버로부터 정보를 받아와서 그 정보를 listView에 보일 뷰들에 세팅한다.
                //위에 int i 가 position의 역할을 하므로 i값을 얻어오면 commentItems list에 있는 인덱스에 적용돼서 잘 된다.
                commentItemView.setUserId(commentResponse.result.get(i).writer);
                commentItemView.setCommentContent(commentResponse.result.get(i).contents);
                commentItemView.setCommentRatingBar(commentResponse.result.get(i).rating/2);
                commentItemView.setTime(commentResponse.result.get(i).time);
                commentItemView.setRecommendationNum(commentResponse.result.get(i).recommend + "");
                Log.d("ReadMoreActivity", "CommentItemView에서 세팅한 것 TEST: " + commentResponse.result.get(i).contents);

                //이 메소드 내부적으로 순서대로 item들에 아래 값들을 적용 시키는 것 같다.
                //->이렇게 하면 작성한 이후에 서버에 요청한 정보가 들어오는 것이 아니고 기존에 저장돼있는 정보가 넘어와서 바로 최신화된 정보가 보이지 않는다.
//            commentItemView.setUserId(commentItem.writer);
//            commentItemView.setCommentContent(commentItem.contents);
//            commentItemView.setCommentRatingBar(commentItem.rating);
//            commentItemView.setTime(commentItem.time);
//            commentItemView.setRecommendationNum(commentItem.recommend+"");


                Log.d("ReadMoreActivity","한줄평의 고유 id값: "+ commentResponse.result.get(i).id);
                commentItemView.setId(commentResponse.result.get(i).id);    //각 한줄평 리스트 아이템들에 고유 id값을 서버에서 받아와 적용시킨다.
                commentItemView.setRecommendation_num(commentResponse.result.get(i).recommend); //원래 서버에 저장된 값을 commentItemView에 저장한다.
                //그 후에 추천을 누르면 거기서 1 증가한 수를 즉각적으로 보여주기 위해 저장함.
            }else{
                //데이터베이스에서 가져와서 보여줘야 된다.
                Log.d("FragMovieInfo", "인터넷이 연결돼있지 않아 데이터베이스에서 정보를 가져와 뷰에 보여줌.");

                if (AppHelper.selectData(AppHelper.COMMENT, position, commentItem.getId())) {  //getId 위에 onResume에서 인터넷없을 때 넣어준 id값들을 차례대로 가져오기 때문에 한줄평들을 각각 보여줄 수 있다.
                    commentItemView.setUserId(AppHelper.com_writer);
                    commentItemView.setCommentContent(AppHelper.com_contents);
                    commentItemView.setCommentRatingBar(AppHelper.com_rating / 2);    //나누기2를 하는 이유는 곱하기 2를 해서 서버에 저장하기 때문에 다시 리스트에 보여질 때는 자신이 입력한 별점만큼 보여야 하기 때문이다.
                    commentItemView.setTime(AppHelper.com_time);
                    commentItemView.setRecommendationNum(AppHelper.com_recommend + "");
                    commentItemView.setId(AppHelper.com_id);
                    commentItemView.setRecommendation_num(AppHelper.com_recommend);
                }
            }

            return commentItemView;
        }
    }

}


