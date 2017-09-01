package com.htcardone.popularmovies.movies;

/**
 * Created by henrique.cardone on 29/08/2017.
 */

import com.htcardone.popularmovies.BasePresenter;
import com.htcardone.popularmovies.BaseView;
import com.htcardone.popularmovies.data.Movie;

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
        void loadMovies(boolean forceUpdate);
        void openMovieDetails(Movie movie);
        void setMoviesSort(int sortType);
        void onMovieClicked(int movieId);
        int getMoviesSort();
    }
}
