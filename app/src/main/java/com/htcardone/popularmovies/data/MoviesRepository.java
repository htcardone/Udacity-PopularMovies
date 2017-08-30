package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.List;

/**
 * Created by henrique.cardone on 30/08/2017.
 */

public class MoviesRepository implements MoviesDataSource {

    private static MoviesRepository INSTANCE;

    private final MoviesRemoteDataSource mMoviesRemoteDataSource;
    // TODO implement local repository and cache

    private MoviesRepository(MoviesRemoteDataSource moviesRemoteDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

    public static MoviesRepository getInstance(MoviesRemoteDataSource moviesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getPopularMovies(@NonNull LoadMoviesCallback callback) {
        getPopularMoviesFromRemoteDataSource(callback);
    }

    @Override
    public void getTopRatedMovies(@NonNull LoadMoviesCallback callback) {
        getTopRatedMoviesFromRemoteDataSource(callback);
    }

    private void getPopularMoviesFromRemoteDataSource(@NonNull final LoadMoviesCallback callback) {
        mMoviesRemoteDataSource.getPopularMovies(new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                callback.onMoviesLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getTopRatedMoviesFromRemoteDataSource(@NonNull final LoadMoviesCallback callback) {
        mMoviesRemoteDataSource.getTopRatedMovies(new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                callback.onMoviesLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
