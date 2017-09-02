package com.htcardone.popularmovies.moviedetail;

import com.htcardone.popularmovies.BasePresenter;
import com.htcardone.popularmovies.BaseView;
import com.htcardone.popularmovies.data.Movie;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void showMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void loadMovie(int movieId, int sortType);
    }
}
