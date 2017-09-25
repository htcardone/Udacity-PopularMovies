package com.htcardone.popularmovies.moviedetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.MoviesDataSource;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.model.Review;
import com.htcardone.popularmovies.data.model.Video;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MovieDetailActivity}), retrieves the data and updates
 * the UI as required.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private static final String LOG_TAG = MovieDetailPresenter.class.getSimpleName();
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

        loadReviews(movie.getId());
        loadVideos(movie.getId());
    }

    private void loadReviews(int movieId) {
        // TODO implement loading indicator on UI
        mMoviesRepository.getReviews(movieId, new MoviesDataSource.LoadReviewsCallback() {
            @Override
            public void onReviewsLoaded(List<Review> reviews) {
                mMovieDetailView.showReviews(reviews);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO show error message
            }
        });
    }

    private void loadVideos(int movieId) {
        // TODO implement loading indicator on UI
        mMoviesRepository.getVideos(movieId, new MoviesDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(List<Video> videos) {
                mMovieDetailView.showVideos(videos);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO show error message
            }
        });
    }
}
