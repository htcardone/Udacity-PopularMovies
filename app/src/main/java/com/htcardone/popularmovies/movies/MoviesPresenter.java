package com.htcardone.popularmovies.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.MoviesDataSource;
import com.htcardone.popularmovies.data.MoviesRepository;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MoviesActivity}), retrieves the data and updates the
 * UI as required.
 */

@SuppressWarnings("CanBeFinal")
public class MoviesPresenter implements MoviesContract.Presenter {
    private final String LOG_TAG = "[MoviesPresenter]";

    public final static int TYPE_SORT_BY_POPULAR = MoviesRepository.TYPE_SORT_BY_POPULAR;
    public final static int TYPE_SORT_BY_TOP_RATED = MoviesRepository.TYPE_SORT_BY_TOP_RATED;

    private int currentSort = TYPE_SORT_BY_POPULAR;

    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private final boolean mFirstLoad[] = {true, true};

    public MoviesPresenter(@NonNull MoviesRepository moviesRepository,
                           @NonNull MoviesContract.View moviesView) {

        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies(false, currentSort);
    }

    @Override
    public void loadMovies(boolean forceUpdate, int sortType) {
        Log.d(LOG_TAG, "loadMovies() forceUpdate=" + forceUpdate + " sortType=" + sortType + " mFirstLoad=" + mFirstLoad[sortType]);
        /*if (mFirstLoad[sortType]) {
            forceUpdate = true;
            mFirstLoad[sortType] = false;
        }*/

        if (forceUpdate) {
            mMoviesRepository.refreshMovies();
            mMoviesView.setLoadingIndicator(true);
        }

        mMoviesRepository.getMovies(sortType, loadMoviesCallback);
    }

    private MoviesDataSource.LoadMoviesCallback loadMoviesCallback =
            new MoviesDataSource.LoadMoviesCallback() {
        @Override
        public void onMoviesLoaded(List<Movie> movies, int sortType) {
            Log.d(LOG_TAG, "onMoviesLoaded");
            currentSort = sortType;
            mMoviesView.showMovies(movies);
            mMoviesView.setViewTitle(currentSort);
            mMoviesView.setLoadingIndicator(false);
        }

        @Override
        public void onDataNotAvailable() {
            Log.d(LOG_TAG, "onDataNotAvailable");
            mMoviesView.setLoadingIndicator(false);
            mMoviesView.showLoadingError();
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
