package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class MoviesRepository implements MoviesDataSource {

    private static String LOG_TAG = "[MoviesRepository]";

    private static MoviesRepository INSTANCE;

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
    public void getMovies(final int sortType, @NonNull final LoadMoviesCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCaches.get(sortType) != null && !mCacheIsDirty) {
            Log.d(LOG_TAG, "using cached movies sortType=" + sortType);
            callback.onMoviesLoaded(mCaches.get(sortType), sortType);
            return;
        }

        // If the cache is dirty we need to fetch new data from the network.
        Log.d(LOG_TAG, "fetch new data from the network sortType=" + sortType);
        mMoviesRemoteDataSource.getMovies(sortType, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies, int sortType) {
                callback.onMoviesLoaded(movies, sortType);
                refreshCache(movies, sortType);
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
        mCaches.set(type, movies);
        mCacheIsDirty = false;
    }

    public Movie getMovie(int id, int type) {
        return mCaches.get(type).get(id);
    }
}
