package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.model.Review;
import com.htcardone.popularmovies.data.model.Video;

import java.util.List;

public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies, int sortType);
        void onDataNotAvailable();
    }

    interface LoadVideosCallback {
        void onVideosLoaded(List<Video> videos);
        void onDataNotAvailable();
    }

    interface LoadReviewsCallback {
        void onReviewsLoaded(List<Review> reviews);
        void onDataNotAvailable();
    }

    int TYPE_SORT_BY_POPULAR = 0; // mCaches pos 0
    int TYPE_SORT_BY_TOP_RATED = 1; // mCaches pos 1
    int TYPE_FAVORITES = 2; // mCaches pos 2

    void getMovies(int sortType, @NonNull LoadMoviesCallback callback);
    long saveMovie(Movie movie, int sortType);
    void deleteAllMovies(int sortType);
    void getVideos(int movieId, @NonNull LoadVideosCallback callback);
    void getReviews(int movieId, @NonNull LoadReviewsCallback callback);
}
