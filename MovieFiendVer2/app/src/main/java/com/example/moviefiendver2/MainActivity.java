package com.example.moviefiendver2;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviefiendver2.MovieData.MovieInfo;
import com.example.moviefiendver2.MovieData.MovieList;
import com.example.moviefiendver2.MovieData.ResponseInfo;
import com.example.moviefiendver2.helper.AppHelper;
import com.example.moviefiendver2.ui.movieList.MovieListFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements FragmentCallback{

    //영화별 상세화면 fragment 참조
    FragMovieInfo fragDeltaInfo;
    FragMovieInfo fragGongjoInfo;
    FragMovieInfo fragGundoInfo;
    FragMovieInfo fragKingInfo;
    FragMovieInfo fragEvilInfo;
    FragMovieInfo fragLuckyInfo;
    FragMovieInfo fragAsuraInfo;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_movielist, R.id.nav_movieapi, R.id.nav_book,
                R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //앱바 아래 그림자를 없애는 코드
        AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 0));
        appBarLayout.setStateListAnimator(stateListAnimator);

        //영화별 상세화면 fragment 객체 생성
        fragDeltaInfo = new FragMovieInfo();
        fragGongjoInfo = new FragMovieInfo();
        fragGundoInfo = new FragMovieInfo();
        fragKingInfo = new FragMovieInfo();
        fragEvilInfo = new FragMovieInfo();
        fragLuckyInfo = new FragMovieInfo();
        fragAsuraInfo = new FragMovieInfo();

        /* 1. activity_main 여기에 영화리스트 화면이 있어야 하는데, 다른 화면으로 갔다가 뒤로가기를 누르면 아무것도 없는 화면(content_main)이 나옴
           2. 새로운 activity인 한줄평 작성과 모두보기를 열 때 앱바의 기능을 구현하지 못함
           3. 액티비티가 종료되면서 onDestroy되면서 intent를 통해 정보를 전달했던 것들이 다 초기화됨.(근데 이 문제는 데이터베이스를 연동하지 않아서 그런거라고 생각함)
         */

        //RequestQueue 객체를 생성한다.
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        requestMovieList(); //서버에 영화 목록 데이터를 요청하는 메소드
    }

    //서버에 영화 목록 데이터를 요청하는 메소드
    public void requestMovieList(){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainActivity","응답 받음 -> " + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity","에러 발생: "+error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Log.d("MainActivity", "영화 목록 요청 보냄");
    }

    //받아온 response를 파싱해서 자바 객체로 만든다.
    public void processResponse(String response){
        Gson gson = new Gson();

        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if(info.code == 200){
            MovieList movieList = gson.fromJson(response, MovieList.class); //movieList에 서버 데이터들이 다 넘어왔다.
            Log.d("MainActivity",""+movieList.result.size());

            //서버에서 받아온 ArrayList를 Fragment(MovieListFragment)로 보내주기
            MovieListFragment movieListFragment = new MovieListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MovieList",movieList.result);
            movieListFragment.setArguments(bundle);
            Log.d("MainActivity","서버에서 받은 ArrayList를 MovieListFragment로 보냄");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //메인 뷰페이저에서 상세보기를 눌렀을 때 프래그먼트에서 실행되는 메소드(메인프래그먼트에서 다른 상세화면 프래그먼트를 실행)
    public void onFragmentChange(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("frag"); //backstack에 추가를 해줘야 프래그먼트에서 뒤로가기를 눌렀을 때
        //원래 상태로 돌아올 수 있다. 추가를 하지 않으면 바로 앱이 꺼진다.
        if(index == 0){
            fragmentTransaction.replace(R.id.movielist_container, fragDeltaInfo).commit();
            //movielist 내부 constraint안에 프래그먼트를 담는다.
        }else if(index == 1){
            fragmentTransaction.replace(R.id.movielist_container, fragGongjoInfo).commit();
        }else if(index == 2){
            fragmentTransaction.replace(R.id.movielist_container, fragGundoInfo).commit();
        }else if(index == 3){
            fragmentTransaction.replace(R.id.movielist_container, fragKingInfo).commit();
        }else if(index == 4){
            fragmentTransaction.replace(R.id.movielist_container, fragEvilInfo).commit();
        }else if(index == 5){
            fragmentTransaction.replace(R.id.movielist_container, fragLuckyInfo).commit();
        }else if(index == 6){
            fragmentTransaction.replace(R.id.movielist_container, fragAsuraInfo).commit();
        }
    }


}
