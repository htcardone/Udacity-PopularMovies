package com.htcardone.popularmovies.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Henrique Cardone on 25/09/2017.
 */

public class FavoritesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = FavoritesProvider.class.getSimpleName();
    private MoviesDbHelper mDbHelper;

    // Codes for the UriMatcher
    private static final int FAVORITE = 100;
    private static final int FAVORITE_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.FavoriteEntry.TABLE_FAVORITE, FAVORITE);
        matcher.addURI(authority, MoviesContract.FavoriteEntry.TABLE_FAVORITE +
                "/#", FAVORITE_WITH_ID);

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
            case FAVORITE:
                return mDbHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteEntry.TABLE_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);

            case FAVORITE_WITH_ID:
                return mDbHelper.getReadableDatabase().query(
                        MoviesContract.FavoriteEntry.TABLE_FAVORITE,
                        projection,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FAVORITE: return MoviesContract.FavoriteEntry.CONTENT_DIR_TYPE;
            case FAVORITE_WITH_ID: return MoviesContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                long _id = db.insert(MoviesContract.FavoriteEntry.TABLE_FAVORITE, null, values);
                if (_id > 0) returnUri = MoviesContract.FavoriteEntry.buildFavoritesUri(_id);
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
            case FAVORITE:
                numDeleted = db.delete(
                        MoviesContract.FavoriteEntry.TABLE_FAVORITE, selection, selectionArgs);
                break;

            case FAVORITE_WITH_ID:
                numDeleted = db.delete(
                        MoviesContract.FavoriteEntry.TABLE_FAVORITE,
                        MoviesContract.FavoriteEntry.COLUMN_NAME_MOVIE_ID + "=?",
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
            case FAVORITE:
                numUpdated = db.update(MoviesContract.FavoriteEntry.TABLE_FAVORITE,
                        contentValues,selection, selectionArgs);
                break;

            case FAVORITE_WITH_ID:
                numUpdated = db.update(
                        MoviesContract.FavoriteEntry.TABLE_FAVORITE,
                        contentValues,
                        MoviesContract.FavoriteEntry.COLUMN_NAME_MOVIE_ID + "=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    /*@Override
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
    }*/
}
