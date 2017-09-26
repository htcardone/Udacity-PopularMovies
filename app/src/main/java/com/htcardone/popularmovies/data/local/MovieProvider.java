package com.htcardone.popularmovies.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Henrique Cardone on 25/09/2017.
 */

public class MovieProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private MoviesDbHelper mDbHelper;

    // Codes for the UriMatcher
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIE, MOVIE);
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIE + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        switch(sUriMatcher.match(uri)){
            case MOVIE:
                return mDbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);

            case MOVIE_WITH_ID:
                return mDbHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIE,
                        projection,
                        MoviesContract.MovieEntry.COLUMN_NAME_MOVIE_ID + "=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIE: return MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID: return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_MOVIE, null, values);
                if (_id > 0) returnUri = MoviesContract.MovieEntry.buildMoviesUri(_id);
                else throw new android.database.SQLException("Failed to insert row into: " + uri);
                break;

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numDeleted;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                numDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_MOVIE, selection, selectionArgs);
                break;

            case MOVIE_WITH_ID:
                numDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_MOVIE,
                        MoviesContract.MovieEntry.COLUMN_NAME_MOVIE_ID + "=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numUpdated;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_MOVIE,
                        contentValues,selection, selectionArgs);
                break;

            case MOVIE_WITH_ID:
                numUpdated = db.update(
                        MoviesContract.MovieEntry.TABLE_MOVIE,
                        contentValues,
                        MoviesContract.MovieEntry.COLUMN_NAME_MOVIE_ID + "=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                // allows for multiple transactions
                db.beginTransaction();
                // keep track of successful inserts
                int numInserted = 0;

                try {
                    for(ContentValues value : values) {
                        if (value == null)
                            throw new IllegalArgumentException("Cannot have null content values");

                        long _id = -1;

                        try {
                            _id = db.insertOrThrow(MoviesContract.MovieEntry.TABLE_MOVIE,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " + value.toString()
                                    + " but value is already in database.");
                        }

                        if (_id != -1) numInserted++;
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }

                if (numInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numInserted;

            default: return super.bulkInsert(uri, values);
        }
    }
}
