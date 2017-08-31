package com.htcardone.popularmovies.movies;

import android.content.Intent;
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
import com.htcardone.popularmovies.moviedetail.MovieDetailActivity;

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
        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MoviesAdapter.ListItemClickListener listItemClickListener = new MoviesAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                mMoviesPresenter.onMovieClicked(clickedItemIndex);
            }
        };

        mAdapter = new MoviesAdapter(getApplicationContext(), listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMoviesPresenter.loadMovies(true);
            }
        });

        // Create the presenter
        mMoviesPresenter = new MoviesPresenter(MoviesRepository.getInstance(
                MoviesRemoteDataSource.getInstance()), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            mMoviesPresenter.loadMovies(true);
            return true;
        }

        if (id == R.id.movies_action_sort_by_popularity) {
            mMoviesPresenter.setMoviesSort(MoviesPresenter.TYPE_SORT_BY_POPULAR);
            mMoviesPresenter.loadMovies(false);
            item.setChecked(true);
            return true;
        }

        if (id == R.id.movies_action_sort_by_rating) {
            mMoviesPresenter.setMoviesSort(MoviesPresenter.TYPE_SORT_BY_TOP_RATED);
            mMoviesPresenter.loadMovies(false);
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
        //TODO showLoadingError
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.replaceData(movies);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showMovieDetail(String movieId) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        //intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void setViewTitle(int sortType) {
        if (sortType == MoviesPresenter.TYPE_SORT_BY_POPULAR) {
            setTitle(R.string.title_popular);
        } else if (sortType == MoviesPresenter.TYPE_SORT_BY_TOP_RATED) {
            setTitle(R.string.title_rated);
        } else {
            setTitle(R.string.app_name);
        }
    }
}
