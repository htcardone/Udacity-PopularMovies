package com.htcardone.popularmovies.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    private static final String LOG_TAG = "[MovieDetailActivity]";
    public static final String EXTRA_MOVIE_ID = "movieId";
    public static final String EXTRA_SORT_TYPE = "sortType";

    @BindView(R.id.movie_detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.movie_detail_backdrop_image) ImageView mBackdrdpImageView;
    @BindView(R.id.movie_detail_tv_poster_image) ImageView mPosterImageView;
    @BindView(R.id.movie_detail_tv_original_title_value) TextView mOriginalTitleTextView;
    @BindView(R.id.movie_detail_tv_release_date_value) TextView mReleaseDateTextView;
    @BindView(R.id.movie_detail_tv_user_rating_value) TextView mOverviewTextView;
    @BindView(R.id.movie_detail_rb_rating) RatingBar mRatingBar;

    private MovieDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new MovieDetailPresenter(this);

        Intent intentFromMovies = getIntent();
        if (intentFromMovies.hasExtra(EXTRA_MOVIE_ID) &&
                intentFromMovies.hasExtra(EXTRA_SORT_TYPE)) {

            int movieId = intentFromMovies.getExtras().getInt(EXTRA_MOVIE_ID);
            int sortType = intentFromMovies.getExtras().getInt(EXTRA_SORT_TYPE);
            mPresenter.loadMovie(movieId, sortType);
        }
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        // this will be useful when we move the View to a Fragment
        if (presenter == null) {
            throw new NullPointerException("MovieDetailContract.Presenter is null");
        }

        mPresenter = presenter;
    }

    @Override
    public void showMovieDetails(Movie movie) {
        setTitle(movie.getTitle());
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOverviewTextView.setText(movie.getOverview());
        mRatingBar.setRating(movie.getVoteAverage() / 2);

        Picasso.with(this)
                .load(movie.getBackdropPath())
                .into(mBackdrdpImageView);

        Picasso.with(this)
                .load(movie.getPosterPath())
                .into(mPosterImageView);
    }
}
