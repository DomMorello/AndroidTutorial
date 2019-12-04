package com.example.mypager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setOffscreenPageLimit(3);

        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());
        Fragment1 fragment1 = new Fragment1();
        adapter.addItem(fragment1);

        Fragment2 fragment2 = new Fragment2();
        adapter.addItem(fragment2);

        Fragment3 fragment3 = new Fragment3();
        adapter.addItem(fragment3);

        pager.setAdapter(adapter);
    }

    class MoviePagerAdapter extends FragmentStatePagerAdapter{

        ArrayList<Fragment> items = new ArrayList<>();

        public MoviePagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addItem(Fragment item){
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
