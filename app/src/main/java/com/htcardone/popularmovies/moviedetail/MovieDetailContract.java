package com.htcardone.popularmovies.moviedetail;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

import com.htcardone.popularmovies.BasePresenter;
import com.htcardone.popularmovies.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
