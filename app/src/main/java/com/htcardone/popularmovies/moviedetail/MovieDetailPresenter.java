package com.htcardone.popularmovies.moviedetail;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MovieDetailActivity}), retrieves the data and updates
 * the UI as required.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MoviesRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;

    public MovieDetailPresenter(@NonNull MovieDetailContract.View movieDetailView) {
        mMoviesRepository = MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance());
        mMovieDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");
        movieDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMovie(int movieId, int sortType) {
        Movie movie = mMoviesRepository.getMovie(movieId, sortType);
        mMovieDetailView.showMovieDetails(movie);
    }
}
