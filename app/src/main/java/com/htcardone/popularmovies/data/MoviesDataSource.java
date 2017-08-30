package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by henrique.cardone on 30/08/2017.
 */

public interface MoviesDataSource {
    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    void getPopularMovies(@NonNull LoadMoviesCallback callback);
    void getTopRatedMovies(@NonNull LoadMoviesCallback callback);
}
