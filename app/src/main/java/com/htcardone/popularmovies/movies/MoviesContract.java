package com.htcardone.popularmovies.movies;

import com.htcardone.popularmovies.BasePresenter;
import com.htcardone.popularmovies.BaseView;
import com.htcardone.popularmovies.data.model.Movie;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MoviesContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean state);
        void showLoadingError();
        void showMovies(List<Movie> movies);
        void showMovieDetail(int movieId, int sortType);
        void setViewTitle(int sortType);
    }

    interface Presenter extends BasePresenter {
        void loadMovies(boolean forceUpdate, int sortType);
        void setMoviesSort(int sortType);
        void onMovieClicked(int movieId);
        int getMoviesSort();
    }
}
