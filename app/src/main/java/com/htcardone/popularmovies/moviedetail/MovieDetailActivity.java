package com.htcardone.popularmovies.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.Movie;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    private static final String LOG_TAG = "[MovieDetailActivity]";
    public static final String EXTRA_MOVIE_ID = "movieId";
    public static final String EXTRA_SORT_TYPE = "sortType";

    private MovieDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mPresenter = new MovieDetailPresenter(this);

        Intent intentFromMovies = getIntent();
        if (intentFromMovies.hasExtra(EXTRA_MOVIE_ID) &&
                intentFromMovies.hasExtra(EXTRA_SORT_TYPE)) {

            int movieId = intentFromMovies.getExtras().getInt(EXTRA_MOVIE_ID);
            int sortType = intentFromMovies.getExtras().getInt(EXTRA_SORT_TYPE);
            mPresenter.loadMovie(movieId, sortType);
        }
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        // TODO Check if this method is needed
        if (presenter == null) {
            throw new NullPointerException("MovieDetailContract.Presenter is null");
        }

        mPresenter = presenter;
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Log.d(LOG_TAG, movie.toString());
    }
}
