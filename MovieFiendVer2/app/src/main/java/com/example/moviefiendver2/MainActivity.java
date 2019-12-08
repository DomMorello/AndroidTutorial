package com.example.moviefiendver2;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements FragmentCallback{

    //영화별 상세화면 fragment 참조
    FragDeltaInfo fragDeltaInfo;
    FragGongjoInfo fragGongjoInfo;
    FragGundoInfo fragGundoInfo;
    FragKingInfo fragKingInfo;
    FragEvilInfo fragEvilInfo;
    FragLuckyInfo fragLuckyInfo;
    FragAsuraInfo fragAsuraInfo;

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
        fragDeltaInfo = new FragDeltaInfo();
        fragGongjoInfo = new FragGongjoInfo();
        fragGundoInfo = new FragGundoInfo();
        fragKingInfo = new FragKingInfo();
        fragEvilInfo = new FragEvilInfo();
        fragLuckyInfo = new FragLuckyInfo();
        fragAsuraInfo = new FragAsuraInfo();

        /* 1. activity_main 여기에 영화리스트 화면이 있어야 하는데, 다른 화면으로 갔다가 뒤로가기를 누르면 아무것도 없는 화면이 나옴
           2. 새로운 activity인 한줄평 작성과 모두보기를 열 때 앱바의 기능을 구현하지 못함
           3. 액티비티가 종료되면서 onDestroy되면서 intent를 통해 정보를 전달했던 것들이 다 초기화됨.(근데 이 문제는 데이터베이스를 연동하지 않아서 그런거라고 생각함)
         */
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //상세보기를 눌렀을 때 프래그먼트에서 실행되는 메소드(다른 상세화면 프래그먼트를 실행)
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
