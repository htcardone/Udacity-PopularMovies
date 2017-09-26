package com.htcardone.popularmovies.data.local;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.MoviesDataSource;

public class MoviesLocalDataSource implements MoviesDataSource {
    @Override
    public void getMovies(int sortType, @NonNull LoadMoviesCallback callback) {

    }

    @Override
    public void getVideos(int movieId, @NonNull LoadVideosCallback callback) {

    }

    @Override
    public void getReviews(int movieId, @NonNull LoadReviewsCallback callback) {

    }
}
