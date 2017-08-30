package com.htcardone.popularmovies.movies;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.Movie;
import com.htcardone.popularmovies.data.MoviesRepository;
import com.htcardone.popularmovies.data.remote.MoviesRemoteDataSource;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

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

        mAdapter = new MoviesAdapter();
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

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieItemViewHolder> {
        private List<Movie> mDataSet;

        public class MovieItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.movies_poster_iv)
            ImageView posterImageView;

            private MovieItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        private MoviesAdapter() {

        }

        private MoviesAdapter(List<Movie> dataSet) {
            dataSet = mDataSet;
        }

        public void replaceData(List<Movie> movies) {
            mDataSet = checkNotNull(movies);
            notifyDataSetChanged();
        }

        @Override
        public MoviesAdapter.MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.movies_list_item, parent, false);
            return new MovieItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieItemViewHolder holder, int position) {
            Picasso.with(MoviesActivity.this)
                    .load(mDataSet.get(position).getPosterPath())
                    .placeholder(R.drawable.poster_placeholder)
                    .into(holder.posterImageView);
        }

        @Override
        public int getItemCount() {
            if (mDataSet == null) return 0;
            return mDataSet.size();
        }
    }
}
