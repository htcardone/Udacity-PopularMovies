package com.htcardone.popularmovies.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Henrique Cardone on 25/09/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movies.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_MOVIES_TABLE =
            "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_MOVIE + " (" +
                    MoviesContract.MovieEntry._ID + INTEGER_TYPE + " PRIMARY KEY, " +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_ID + INTEGER_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_POSTER_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + FLOAT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_SORT_TYPE + INTEGER_TYPE +
                    " );";

    private static final String SQL_CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + MoviesContract.FavoriteEntry.TABLE_FAVORITE + " (" +
                    MoviesContract.FavoriteEntry._ID + INTEGER_TYPE + " PRIMARY KEY, " +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_ID + INTEGER_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_POSTER_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + FLOAT_TYPE + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_SORT_TYPE + INTEGER_TYPE +
                    " );";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Not required as at version 1
    }
}
