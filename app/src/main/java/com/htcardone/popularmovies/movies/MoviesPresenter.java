package com.htcardone.popularmovies.movies;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

import android.support.annotation.NonNull;
import android.util.Log;

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

    public final static int TYPE_SORT_BY_POPULAR = MoviesRepository.TYPE_SORT_BY_POPULAR;
    public final static int TYPE_SORT_BY_TOP_RATED = MoviesRepository.TYPE_SORT_BY_TOP_RATED;

    private int currentSort = TYPE_SORT_BY_POPULAR;

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
        loadMovies(true);
    }

    @Override
    public void loadMovies(boolean forceUpdate) {
        if (forceUpdate) {
            mMoviesRepository.refreshMovies();
            mMoviesView.setLoadingIndicator(true);
        }

        if (currentSort == TYPE_SORT_BY_POPULAR) {
            getPopularMovies();
        } else if (currentSort == TYPE_SORT_BY_TOP_RATED) {
            getTopRatedMovies();
        }
    }

    @Override
    public void openMovieDetails(Movie movie) {

    }

    private void getPopularMovies() {
        mMoviesRepository.getPopularMovies(loadMoviesCallback);
    }

    private void getTopRatedMovies() {
        mMoviesRepository.getTopRatedMovies(loadMoviesCallback);
    }

    private MoviesDataSource.LoadMoviesCallback loadMoviesCallback =
            new MoviesDataSource.LoadMoviesCallback() {
        @Override
        public void onMoviesLoaded(List<Movie> movies) {
            Log.d(LOG_TAG, "onMoviesLoaded");

            mMoviesView.showMovies(movies);
            mMoviesView.setViewTitle(currentSort);
            mMoviesView.setLoadingIndicator(false);
        }

        @Override
        public void onDataNotAvailable() {
            Log.d(LOG_TAG, "onDataNotAvailable");
            mMoviesView.setLoadingIndicator(false);
        }
    };

    @Override
    public void setMoviesSort(int type) {
        currentSort = type;
    }

    @Override
    public void onMovieClicked(int movieId) {
        Log.d(LOG_TAG, "movieId=" + mMoviesRepository.getMovie(movieId, currentSort));
        mMoviesView.showMovieDetail(movieId, currentSort);
    }

    @Override
    public int getMoviesSort() {
        return currentSort;
    }
}
