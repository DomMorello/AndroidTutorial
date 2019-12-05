package com.example.moviefiendver2.ui.MovieAPI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moviefiendver2.R;

public class MovieAPIFragment extends Fragment {

    private MovieAPIViewModel movieAPIViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieAPIViewModel =
                ViewModelProviders.of(this).get(MovieAPIViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie_api, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        movieAPIViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}