package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.model.Movie;

import java.util.List;

public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies, int sortType);
        void onDataNotAvailable();
    }

    int TYPE_SORT_BY_POPULAR = 0; // mCaches pos 0
    int TYPE_SORT_BY_TOP_RATED = 1; // mCaches pos 1

    void getMovies(int sortType, @NonNull LoadMoviesCallback callback);
}
