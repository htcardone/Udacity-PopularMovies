package com.htcardone.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.model.Review;
import com.htcardone.popularmovies.data.model.Video;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    private static final String LOG_TAG = "[MovieDetailActivity]";
    public static final String EXTRA_MOVIE_ID = "movieId";
    public static final String EXTRA_SORT_TYPE = "sortType";

    @BindView(R.id.movie_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.movie_detail_backdrop_image)
    ImageView mBackdropImageView;
    @BindView(R.id.movie_detail_tv_poster_image)
    ImageView mPosterImageView;
    @BindView(R.id.movie_detail_tv_original_title_value)
    TextView mOriginalTitleTextView;
    @BindView(R.id.movie_detail_tv_release_date_value)
    TextView mReleaseDateTextView;
    @BindView(R.id.movie_detail_tv_overview_value)
    TextView mOverviewTextView;
    @BindView(R.id.movie_detail_rb_rating)
    RatingBar mRatingBar;
    @BindView(R.id.movie_detail_rv_videos)
    RecyclerView mVideosRecyclerView;

    private MovieDetailContract.Presenter mPresenter;
    private VideosAdapter mVideosAdapter;
    private RecyclerView.LayoutManager mVideosLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPresenter = new MovieDetailPresenter(this);

        Intent intentFromMovies = getIntent();
        if (intentFromMovies.hasExtra(EXTRA_MOVIE_ID) &&
                intentFromMovies.hasExtra(EXTRA_SORT_TYPE)) {

            int movieId = intentFromMovies.getExtras().getInt(EXTRA_MOVIE_ID);
            int sortType = intentFromMovies.getExtras().getInt(EXTRA_SORT_TYPE);
            mPresenter.loadMovie(movieId, sortType);
        }

        // Videos RecyclerView setup
        mVideosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mVideosRecyclerView.setLayoutManager(mVideosLayoutManager);
        mVideosAdapter = new VideosAdapter(this, new VideosAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(String youTubeKey) {
                openYouTubeVideo(youTubeKey);
            }
        });
        mVideosRecyclerView.setAdapter(mVideosAdapter);
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
    public void openYouTubeVideo(String youTubeKey) {
        Intent intent = new  Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + youTubeKey));
        startActivity(intent);
    }

    @Override
    public void showMovieDetails(Movie movie) {
        setTitle(movie.getTitle());
        mOriginalTitleTextView.setText(movie.getOriginalTitle());

        // TODO move this to a Utils class
        @SuppressLint("SimpleDateFormat") //using date format from TMDB API
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(movie.getReleaseDate());
            mReleaseDateTextView.setText(DateFormat.getDateFormat(this).format(date));
        } catch (ParseException e) {
            mReleaseDateTextView.setText(movie.getReleaseDate());
        }

        mOverviewTextView.setText(movie.getOverview());
        mRatingBar.setRating(movie.getVoteAverage() / 2);

        Picasso.with(this)
                .load(movie.getBackdropPath())
                .into(mBackdropImageView);

        Picasso.with(this)
                .load(movie.getPosterPath())
                .into(mPosterImageView);
    }

    @Override
    public void showReviews(List<Review> reviewList) {
        for (Review review : reviewList) {
            Log.d(LOG_TAG, review.toString());
        }
    }

    @Override
    public void showVideos(List<Video> videoList) {
        mVideosAdapter.replaceData(videoList);
    }
}
