package com.htcardone.popularmovies.data.local;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.htcardone.popularmovies.data.MoviesDataSource;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.local.MoviesContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class MoviesLocalDataSource implements MoviesDataSource {

    private static final String LOG_TAG = MoviesLocalDataSource.class.getSimpleName();

    private static MoviesLocalDataSource INSTANCE;

    private Context mContext;
    private MoviesDbHelper mMoviesDbHelper;

    private MoviesLocalDataSource(Context context) {
        mContext = context;
        mMoviesDbHelper = new MoviesDbHelper(context);
    }

    public static MoviesLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMovies(final int sortType, @NonNull final LoadMoviesCallback callback) {
        Log.d(LOG_TAG, "getMovives sortType=" + sortType);
        final List<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        String selection = MovieEntry.COLUMN_SORT_TYPE + " =?";
        String[] selectionArgs = { String.valueOf(sortType) };

        Cursor c = db.query(MoviesContract.MovieEntry.TABLE_MOVIE,
                null, selection, selectionArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String overview =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_OVERVIEW));
                String originalTitle =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_ORIGINAL_TITLE));
                String title =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_TITLE));
                String posterPath =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_POSTER_PATH));
                String backdropPath =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_BACKDROP_PATH));
                String releaseDate =
                        c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_RELEASE_DATE));
                float voteAverage =
                        c.getFloat(c.getColumnIndexOrThrow(MovieEntry.COLUMN_VOTE_AVERAGE));
                int id =
                        c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_ID));

                Movie movie = new Movie(overview, originalTitle, title, posterPath, backdropPath,
                        releaseDate, voteAverage, id);

                movies.add(movie);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (movies.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onMoviesLoaded(movies, sortType);
        }
    }

    @Override
    public long saveMovie(Movie movie, int sortType) {
        checkNotNull(movie);
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieEntry.COLUMN_SORT_TYPE, sortType);

        long id = db.insert(MovieEntry.TABLE_MOVIE, null, values);

        db.close();

        return id;
    }

    @Override
    public void deleteAllMovies(int sortType) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        String selection = MovieEntry.COLUMN_SORT_TYPE + " =?";
        String[] selectionArgs = { String.valueOf(sortType) };

        db.delete(MovieEntry.TABLE_MOVIE, selection, selectionArgs);
        db.close();
    }

    public boolean isMovieFavorite(int movieId) {
        Cursor cursor = mContext.getContentResolver()
            .query(ContentUris.withAppendedId(MoviesContract.FavoriteEntry.CONTENT_URI, movieId),
                null,null, null, null);

        if (cursor == null) return false;

        cursor.close();
        return cursor.getCount() > 0;
    }

    public boolean setMovieAsFavorite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        Uri uri = mContext.getContentResolver()
                .insert(MoviesContract.FavoriteEntry.CONTENT_URI, values);

        return uri != null;
    }

    public boolean unsetMovieAsFavorite(int movieId) {
        int deleteRows = mContext.getContentResolver().delete(
                ContentUris.withAppendedId(MoviesContract.FavoriteEntry.CONTENT_URI, movieId),
                        null, null);

        return deleteRows > 0;
    }

    public List<Movie> getFavoriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver() .query(
                MoviesContract.FavoriteEntry.CONTENT_URI, null,null, null, null);

        if (cursor == null) return movies;

        while (cursor.moveToNext()) {
            movies.add(new Movie(
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)),
                    cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID))
            ));
        }

        cursor.close();
        return movies;
    }

    @Override
    public void getVideos(int movieId, @NonNull LoadVideosCallback callback) {
        // useless, videos won't be stored
    }

    @Override
    public void getReviews(int movieId, @NonNull LoadReviewsCallback callback) {
        // TODO implement Reviews db, or include the reviews to the respective Movie
    }
}
