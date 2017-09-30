package com.htcardone.popularmovies.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.local.MoviesLocalDataSource;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.util.Preconditions.checkNotNull;

@SuppressWarnings("CanBeFinal")
public class MoviesRepository implements MoviesDataSource {

    private static String LOG_TAG = "[MoviesRepository]";

    private static MoviesRepository INSTANCE;

    private MoviesRemoteDataSource mRemoteDataSource;
    private MoviesLocalDataSource mLocalDataSource;
    private List<Map<Integer, Movie>> mMoviesCaches = new ArrayList<>(2);

    private boolean mCacheIsDirty = false;

    private MoviesRepository(MoviesRemoteDataSource moviesRemoteDataSource,
                             MoviesLocalDataSource moviesLocalDataSource) {
        mRemoteDataSource = moviesRemoteDataSource;
        mLocalDataSource = moviesLocalDataSource;
        mMoviesCaches.add(null);
        mMoviesCaches.add(null);
    }

    public static MoviesRepository getInstance(MoviesRemoteDataSource moviesRemoteDataSource,
                                               MoviesLocalDataSource moviesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource, moviesLocalDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getMovies(final int sortType, @NonNull final LoadMoviesCallback callback) {

        // Respond immediately with cache if available and not dirty
        if (mMoviesCaches.get(sortType) != null && !mCacheIsDirty) {
            Log.d(LOG_TAG, "using cached movies sortType=" + sortType);
            callback.onMoviesLoaded(new ArrayList<>(mMoviesCaches.get(sortType).values()),
                    sortType);
            return;
        }

        // If the cache is dirty we need to fetch new data from the network.
        if (mCacheIsDirty) {
            Log.d(LOG_TAG, "fetch new data from the network sortType=" + sortType);
            getMoviesFromRemoteDataSoure(sortType, callback);
        } else {
            // Query the local storage if available. If not, query the network.
            Log.d(LOG_TAG, "get data from the local storage sortType=" + sortType);
            mLocalDataSource.getMovies(sortType, new LoadMoviesCallback() {

                @Override
                public void onMoviesLoaded(List<Movie> movies, int sortType) {
                    refreshCache(movies, sortType);
                    callback.onMoviesLoaded(movies, sortType);
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d(LOG_TAG, "couldn't get data from the local storage sortType=" + sortType);
                    getMoviesFromRemoteDataSoure(sortType, callback);
                }
            });
        }
    }

    @Override
    public long saveMovie(@NonNull Movie movie, int sortType) {
        checkNotNull(movie);
        long id = mLocalDataSource.saveMovie(movie, sortType);

        // Do in memory cache update to keep the app UI up to date
        Map<Integer, Movie> cache = mMoviesCaches.get(sortType);

        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        cache.put(movie.getId(), movie);

        mMoviesCaches.set(sortType, cache);
        return id;
    }

    private void getMoviesFromRemoteDataSoure(int sortType,
                                              @NonNull final LoadMoviesCallback callback) {
        mRemoteDataSource.getMovies(sortType, new LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies, int sortType) {
                refreshCache(movies, sortType);
                refreshLocalDataSource(movies, sortType);
                callback.onMoviesLoaded(movies, sortType);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteAllMovies(int sortType) {
        mLocalDataSource.deleteAllMovies(sortType);

        Map cache = mMoviesCaches.get(sortType);
        if (cache == null) cache = new LinkedHashMap<>();
        cache.clear();

        mMoviesCaches.set(sortType, cache);
    }

    private void refreshLocalDataSource(List<Movie> movies, int sortType) {
        Log.d(LOG_TAG, "refreshLocalDataSource()");
        mLocalDataSource.deleteAllMovies(sortType);
        for (Movie movie : movies) {
            mLocalDataSource.saveMovie(movie, sortType);
        }
    }

    @Override
    public void getVideos(int movieId, @NonNull LoadVideosCallback callback) {
        mRemoteDataSource.getVideos(movieId, callback);
    }

    @Override
    public void getReviews(int movieId, @NonNull LoadReviewsCallback callback) {
        mRemoteDataSource.getReviews(movieId, callback);
    }

    public void refreshMovies() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<Movie> movies, int type) {
        Map cache = mMoviesCaches.get(type);

        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        cache.clear();

        for (Movie movie : movies) {
            cache.put(movie.getId(), movie);
        }

        mMoviesCaches.set(type, cache);
        mCacheIsDirty = false;
    }

    public Movie getMovie(int id, int type) {
        return mMoviesCaches.get(type).get(id);
    }

    public boolean isMovieFavorite(int movieId) {
        return mLocalDataSource.isMovieFavorite(movieId);
    }

    public boolean setMovieAsFavorite(int movieId, String movieTitle) {
        return mLocalDataSource.setMovieAsFavorite(movieId, movieTitle);
    }

    public boolean unsetMovieAsFavorite(int movieId) {
        return mLocalDataSource.unsetMovieAsFavorite(movieId);
    }
}
