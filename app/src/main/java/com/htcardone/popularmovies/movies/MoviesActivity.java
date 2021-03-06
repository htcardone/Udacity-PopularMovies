package com.htcardone.popularmovies.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.local.MoviesLocalDataSource;
import com.htcardone.popularmovies.data.model.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;
import com.htcardone.popularmovies.moviedetail.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    private static final String LOG_TAG = "[MoviesActivity]";
    private static final String CURRENT_SORT_KEY = "sortKey";
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
        int nunColumns = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT ? 4 : 5;

        mLayoutManager = new GridLayoutManager(this, nunColumns);
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
                mMoviesPresenter.loadMovies(true, mMoviesPresenter.getMoviesSort());
            }
        });

        // Create the presenter
        mMoviesPresenter = new MoviesPresenter(MoviesRepository.getInstance(
                MoviesRemoteDataSource.getInstance(), MoviesLocalDataSource.getInstance(this)),
                this);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            int currentSort = savedInstanceState.getInt(CURRENT_SORT_KEY);
            mMoviesPresenter.setMoviesSort(currentSort);
            setViewTitle(currentSort);
        }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_SORT_KEY, mMoviesPresenter.getMoviesSort());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(mMoviesPresenter.getMoviesSort() + 1).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.movies_action_refresh) {
            mMoviesPresenter.loadMovies(true, mMoviesPresenter.getMoviesSort());
            return true;
        }

        if (id == R.id.movies_action_sort_by_popularity) {
            mMoviesPresenter.loadMovies(false, MoviesPresenter.TYPE_SORT_BY_POPULAR);
            item.setChecked(true);
            return true;
        }

        if (id == R.id.movies_action_sort_by_rating) {
            mMoviesPresenter.loadMovies(false, MoviesPresenter.TYPE_SORT_BY_TOP_RATED);
            item.setChecked(true);
            return true;
        }

        if (id == R.id.movies_action_show_favorites) {
            mMoviesPresenter.loadMovies(false, MoviesPresenter.TYPE_FAVORITES);
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
        Toast.makeText(this, getString(R.string.movies_load_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.replaceData(movies);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showMovieDetail(int movieId, int sortType) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId);
        intent.putExtra(MovieDetailActivity.EXTRA_SORT_TYPE, sortType);
        startActivity(intent);
    }

    @Override
    public void setViewTitle(int sortType) {
        if (sortType == MoviesPresenter.TYPE_SORT_BY_POPULAR) {
            setTitle(R.string.title_popular);
        } else if (sortType == MoviesPresenter.TYPE_SORT_BY_TOP_RATED) {
            setTitle(R.string.title_rated);
        } else if (sortType == MoviesPresenter.TYPE_FAVORITES) {
            setTitle(R.string.title_favorites);
        } else {
            setTitle(R.string.app_name);
        }
    }
}
