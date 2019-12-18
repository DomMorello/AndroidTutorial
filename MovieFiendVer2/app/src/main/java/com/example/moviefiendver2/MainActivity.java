package com.example.moviefiendver2;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moviefiendver2.helper.AppHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements FragmentCallback {

    //영화 상세화면 fragment 참조
    FragMovieInfo fragMovieInfo;

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

        //영화 상세화면 fragment 객체 생성
        fragMovieInfo = new FragMovieInfo();

        /* 1. activity_main 여기에 영화리스트 화면이 있어야 하는데, 다른 화면으로 갔다가 뒤로가기를 누르면 아무것도 없는 화면(content_main)이 나옴
           2. 새로운 activity인 한줄평 작성과 모두보기를 열 때 앱바의 기능을 구현하지 못함
        */

        AppHelper.openDatabase(getApplicationContext(),"Dom's DB"); //앱이 실행되면 데이터베이스를 열어라.
        AppHelper.createTable("MainMovie"); //앱이 실행되면 MainMovie table을 생성
        AppHelper.selectMainMovieData("MainMovie");
        /* 인터넷 연결 유무에 따라 서버에서 정보를 받아와 최초에 insert를 하고 그 이후부터는 update를 한다. */

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //메인 뷰페이저에서 상세보기를 눌렀을 때 프래그먼트에서 실행되는 메소드(메인프래그먼트에서 다른 상세화면 프래그먼트를 실행)
    @Override
    public void onFragmentChange(Bundle bundle) {
        fragMovieInfo.setArguments(bundle); //position정보를 bundle에 담고 있기 때문에 이를 새로 띄우는 fragment에 전달한다.
        Log.d("MainActivity","상세보기 클릭시 MainActivity로 넘어온 position: " + bundle.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("frag"); //backstack에 추가를 해줘야 프래그먼트에서 뒤로가기를 눌렀을 때
        //원래 상태로 돌아올 수 있다. 추가를 하지 않으면 바로 앱이 꺼진다.
        fragmentTransaction.replace(R.id.movielist_container, fragMovieInfo).commit();
        //movielist 내부 constraint안에 프래그먼트를 담는다.
    }

}
