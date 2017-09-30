package com.htcardone.popularmovies.moviedetail;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.MoviesDataSource;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.model.Review;
import com.htcardone.popularmovies.data.model.Video;

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
    private Movie mMovie;
    private boolean mIsFavorite;

    public MovieDetailPresenter(@NonNull MoviesRepository moviesRepository,
            @NonNull MovieDetailContract.View movieDetailView) {
        mMoviesRepository = moviesRepository;
        mMovieDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");
        movieDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadMovie(int movieId, int sortType) {
        mMovie = mMoviesRepository.getMovie(movieId, sortType);
        mIsFavorite = mMoviesRepository.isMovieFavorite(movieId);

        mMovieDetailView.showMovieDetails(mMovie, mIsFavorite);

        loadReviews();
        loadVideos();
    }

    @Override
    public void onFavoriteButtonClicked() {
        if (mIsFavorite) {
            if (mMoviesRepository.unsetMovieAsFavorite(mMovie.getId())) {
                mMovieDetailView.updateFavoriteButton(false);
            }
        } else {
            if (mMoviesRepository.setMovieAsFavorite(mMovie.getId(), mMovie.getTitle())) {
                mMovieDetailView.updateFavoriteButton(true);
            }
        }
    }

    private void loadReviews() {
        // TODO implement loading indicator on UI
        mMoviesRepository.getReviews(mMovie.getId(), new MoviesDataSource.LoadReviewsCallback() {
            @Override
            public void onReviewsLoaded(List<Review> reviews) {
                mMovieDetailView.showReviews(reviews);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO show error message on UI
            }
        });
    }

    private void loadVideos() {
        // TODO implement loading indicator on UI
        mMoviesRepository.getVideos(mMovie.getId(), new MoviesDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(List<Video> videos) {
                mMovieDetailView.showVideos(videos);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO show error message on UI
            }
        });
    }
}
