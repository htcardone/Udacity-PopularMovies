package com.htcardone.popularmovies.movies;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    private static final String LOG_TAG = "[MoviesActivity]";
    private MoviesContract.Presenter mMoviesPresenter;
    private MoviesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.movies_sr)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.movies_rv)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        // RecyclerView setup
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMoviesPresenter.loadMovies();
            }
        });

        // Create the presenter
        mMoviesPresenter = new MoviesPresenter(MoviesRepository.getInstance(
                MoviesRemoteDataSource.getInstance()), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoviesPresenter.start();
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        if (presenter == null) {
            throw new NullPointerException("MoviesContract.Presenter is null");
        }

        mMoviesPresenter = presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.movies_action_refresh) {
            mMoviesPresenter.loadMovies();
            return true;
        }

        if (id == R.id.movies_action_sort_by_popularity) {
            mMoviesPresenter.setMoviesSort(MoviesPresenter.SORT_BY_POPULARITY);
            mMoviesPresenter.loadMovies();
            item.setChecked(true);
            return true;
        }

        if (id == R.id.movies_action_sort_by_rating) {
            mMoviesPresenter.setMoviesSort(MoviesPresenter.SORT_BY_RATING);
            mMoviesPresenter.loadMovies();
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingIndicator(boolean state) {
        mRefreshLayout.setRefreshing(state);
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.replaceData(movies);
    }

    @Override
    public void setViewTitle(int sortType) {
        if (sortType == MoviesPresenter.SORT_BY_POPULARITY) {
            setTitle(R.string.title_popular);
        } else if (sortType == MoviesPresenter.SORT_BY_RATING) {
            setTitle(R.string.title_rated);
        } else {
            setTitle(R.string.app_name);
        }
    }
}
