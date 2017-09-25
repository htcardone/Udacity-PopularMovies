package com.htcardone.popularmovies.moviedetail;

import com.htcardone.popularmovies.BasePresenter;
import com.htcardone.popularmovies.BaseView;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.model.Review;
import com.htcardone.popularmovies.data.model.Video;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void showMovieDetails(Movie movie);
        void showReviews(List<Review> reviewList);
        void showVideos(List<Video> videoList);
        void openYouTubeVideo(String youTubeKey);
        void openReviewUrl(String reviewUrl);
    }

    interface Presenter extends BasePresenter {
        void loadMovie(int movieId, int sortType);
    }
}
