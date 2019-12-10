package com.example.moviefiendver2.ui.movieList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.moviefiendver2.MainFragmentMovie;
import com.example.moviefiendver2.MovieData.MovieInfo;
import com.example.moviefiendver2.R;
import com.example.moviefiendver2.ZoomOutPageTransformer;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private MovieListViewModel movieListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieListViewModel =
                ViewModelProviders.of(this).get(MovieListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ViewPager pager = root.findViewById(R.id.pager); //홈화면 뷰페이저
        pager.setPageTransformer(true, new ZoomOutPageTransformer());   //페이지 슬라이드 시 축소되면서 넘어가는 애니메이션 효과
        pager.setOffscreenPageLimit(3);

        MoviePagerAdapter adapter = new MoviePagerAdapter(getFragmentManager());

        //MainActivity에서 보낸 서버로부터 받은 ArrayList 를 받아서 활용한다. !!! 이 부분이 잘 안 되는 것 같다! 
        Bundle passedBundle = getArguments();
        if (passedBundle != null) {
//            Bundle passedBundle = getArguments();
            ArrayList<MovieInfo> movieListFromServer = passedBundle.getParcelableArrayList("MovieList");
            Log.d("MovieListFragment", "메인액티비에서 받아온 리스트 사이즈: " + movieListFromServer.size());
            /* 서버에서 받아온 리스트를 Fragment로 만든 후 그 Fragment들을 여기 ViewPager adapter에 넣어야 한다 */
            for(int i=0; i < movieListFromServer.size(); i++){
                MainFragmentMovie mainFragmentMovie = new MainFragmentMovie();
                /* 반복문을 통해 MainFragmentMovie 객체를 생성하고 adapter에 추가하여 ViewPager로 보여준다 */
                mainFragmentMovie.getMainTitle().setText(movieListFromServer.get(0).title);
                adapter.addItem(mainFragmentMovie);
            }
        }



        //프래그먼트 객체 생성
        /*MainFragmentMovie mainFragmentDelta = new MainFragmentMovie();
        MainFragmentMovie mainFragmentGongjo = new MainFragmentMovie();
        MainFragmentMovie mainFragmentGundo = new MainFragmentMovie();
        MainFragmentMovie mainFragmentKing = new MainFragmentMovie();
        MainFragmentMovie mainFragmentEvil = new MainFragmentMovie();
        MainFragmentMovie mainFragmentLucky = new MainFragmentMovie();
        MainFragmentMovie mainFragmentAsura = new MainFragmentMovie();*/

        //프래그먼트를 어댑터 list에 추가
        /*adapter.addItem(mainFragmentDelta);
        adapter.addItem(mainFragmentGongjo);
        adapter.addItem(mainFragmentGundo);
        adapter.addItem(mainFragmentKing);
        adapter.addItem(mainFragmentEvil);
        adapter.addItem(mainFragmentLucky);
        adapter.addItem(mainFragmentAsura);*/

        pager.setAdapter(adapter);  //어댑터로 설정

        return root;
    }

    //뷰페이저를 위한 어댑터 정의
    //원래는 mainActivity에 했었는데 애초에 HomeFragment(MovieListFragment)가 메인에 연결이 default로 돼있기 때문에
    // HomeFragment 안에 뷰페이저를 넣기 위해 어댑터를 이 위치에 정의
    class MoviePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<>();

        public MoviePagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

    }
}