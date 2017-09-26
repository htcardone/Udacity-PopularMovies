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
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_MOVIE + " (" +
                    MoviesContract.MovieEntry.COLUMN_NAME_MOVIE_ID + INTEGER_TYPE + " PRIMARY KEY," + COMMA_SEP +
                    MoviesContract.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    " )";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Not required as at version 1
    }
}
