package com.example.moviefiendver2.ui.MovieAPI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieAPIViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MovieAPIViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}