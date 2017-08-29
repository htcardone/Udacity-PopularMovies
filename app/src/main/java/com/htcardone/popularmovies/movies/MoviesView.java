package com.htcardone.popularmovies.movies;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

public class MoviesView implements MoviesContract.View {
    private MoviesContract.Presenter mPresenter;

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        //TODO check not null
        mPresenter = presenter;
    }
}
