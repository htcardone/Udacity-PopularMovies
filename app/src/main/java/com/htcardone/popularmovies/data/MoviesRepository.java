package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrique.cardone on 30/08/2017.
 */

public class MoviesRepository implements MoviesDataSource {

    private static String LOG_TAG = "[MoviesRepository]";

    private static MoviesRepository INSTANCE;

    public static final int TYPE_SORT_BY_POPULAR = 0; // mCaches pos 0
    public static final int TYPE_SORT_BY_TOP_RATED = 1; // mCaches pos 1

    private MoviesRemoteDataSource mMoviesRemoteDataSource;
    private List<List<Movie>> mCaches = new ArrayList<>(2);

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    private boolean mCacheIsDirty = true;

    private MoviesRepository(MoviesRemoteDataSource moviesRemoteDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mCaches.add(null);
        mCaches.add(null);
    }

    public static MoviesRepository getInstance(MoviesRemoteDataSource moviesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getPopularMovies(@NonNull LoadMoviesCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCaches.get(TYPE_SORT_BY_POPULAR) != null && !mCacheIsDirty) {
            Log.d(LOG_TAG, "using cached movies");
            callback.onMoviesLoaded(mCaches.get(TYPE_SORT_BY_POPULAR));
            return;
        }

        Log.d(LOG_TAG, "fetch new data from the network");

        // If the cache is dirty we need to fetch new data from the network.
        getPopularMoviesFromRemoteDataSource(callback);
    }

    @Override
    public void getTopRatedMovies(@NonNull LoadMoviesCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCaches.get(TYPE_SORT_BY_TOP_RATED) != null && !mCacheIsDirty) {
            Log.d(LOG_TAG, "using cached movies");
            callback.onMoviesLoaded(mCaches.get(TYPE_SORT_BY_TOP_RATED));
            return;
        }

        Log.d(LOG_TAG, "fetch new data from the network");

        // If the cache is dirty we need to fetch new data from the network.
        getTopRatedMoviesFromRemoteDataSource(callback);
    }

    private void getPopularMoviesFromRemoteDataSource(@NonNull final LoadMoviesCallback callback) {
        mMoviesRemoteDataSource.getPopularMovies(new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                callback.onMoviesLoaded(movies);
                refreshCache(movies, TYPE_SORT_BY_POPULAR);
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
                refreshCache(movies, TYPE_SORT_BY_TOP_RATED);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    public void refreshMovies() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<Movie> movies, int type) {
        Log.d(LOG_TAG, "type=" + type + " movies=" + movies);
        mCaches.set(type, movies);
        mCacheIsDirty = false;
    }

    public Movie getMovie(int id, int type) {
        return mCaches.get(type).get(id);
    }
}
