package com.example.moviefiendver2;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        //openDatabase메서드에서 두번째 parameter databaseName 에 ' 이거 하나 넣었다고 DB조회가 안 됐었다.
        //이걸로 5시간 정도를 날렸다. 파일이름에는 웬만하면 공백, 특수문자 이런거 절대 넣지말자 이제는...
        AppHelper.openDatabase(getApplicationContext(), "DomsDB");  //앱이 실행되면 데이터베이스를 열어라.
        AppHelper.createTable(AppHelper.MAIN_MOVIE);    //앱이 실행되면 MainMovie table을 생성
        AppHelper.createTable(AppHelper.MOVIE_INFO);    //앱이 실행되면 MovieInfo table을 생성
        AppHelper.createTable(AppHelper.COMMENT);    //앱이 실행되면 Comment table을 생성
        AppHelper.createTable(AppHelper.TOTAL_COUNT);   //앱이 실행되면 TotalCount table을 생성

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* 실제 영화 순서 정렬하는 기능은 구현하지 않음. */
        /* 애니메이션 효과 구현하지 않음. */
        int id = item.getItemId();

        switch (id){
            case R.id.menu1:
                Toast.makeText(this, "예매율순 정렬", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu2:
                Toast.makeText(this, "큐레이션 정렬", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu3:
                Toast.makeText(this, "상영예정 정렬", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
