package com.htcardone.popularmovies.moviedetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.htcardone.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {

    }
}
