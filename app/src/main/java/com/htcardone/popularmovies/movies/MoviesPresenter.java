package com.htcardone.popularmovies.movies;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.BuildConfig;
import com.htcardone.popularmovies.data.Movie;
import com.htcardone.popularmovies.data.MoviesDataSource;
import com.htcardone.popularmovies.data.MoviesRepository;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MoviesActivity}), retrieves the data and updates the
 * UI as required.
 */

public class MoviesPresenter implements MoviesContract.Presenter {
    private final String LOG_TAG = "[MoviesPresenter]";
    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository,
                           @NonNull MoviesContract.View moviesView) {

        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies();
    }

    @Override
    public void loadMovies() {
        Log.d(LOG_TAG, "loadMovies()");
        mMoviesView.setLoadingIndicator(true);

        mMoviesRepository.getPopularMovies(new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                if (BuildConfig.DEBUG) {
                    for (Movie movie : movies) {
                        Log.d(LOG_TAG, movie.toString());
                    }
                }
                mMoviesView.setLoadingIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                Log.i(LOG_TAG, "onDataNotAvailable");
                mMoviesView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void openMovieDetails() {

    }
}
