package com.htcardone.popularmovies.moviedetail;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

public class MovieDetailView implements MovieDetailContract.View {
    private MovieDetailContract.Presenter mPresenter;

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        //TODO check not null
        mPresenter = presenter;
    }
}
