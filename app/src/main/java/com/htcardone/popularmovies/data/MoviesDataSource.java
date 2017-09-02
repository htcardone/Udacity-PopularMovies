package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by henrique.cardone on 30/08/2017.
 */

public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies, int sortType);
        void onDataNotAvailable();
    }

    public static final int TYPE_SORT_BY_POPULAR = 0; // mCaches pos 0
    public static final int TYPE_SORT_BY_TOP_RATED = 1; // mCaches pos 1

    void getMovies(@NonNull int sortType, @NonNull LoadMoviesCallback callback);
}
