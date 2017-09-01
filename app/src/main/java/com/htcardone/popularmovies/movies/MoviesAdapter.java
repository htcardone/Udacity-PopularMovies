package com.htcardone.popularmovies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by henrique.cardone on 31/08/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieItemViewHolder> {
    private List<Movie> mDataSet;
    private final Context mContext;
    private final ListItemClickListener mOnClickListener;

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movies_poster_iv)
        ImageView posterImageView;

        private MovieItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(Context context, ListItemClickListener clickListener) {
        mContext = context;
        mOnClickListener = clickListener;
    }

    public MoviesAdapter(Context context, ListItemClickListener clickListener, List<Movie> dataSet) {
        mContext = context;
        mOnClickListener = clickListener;
        mDataSet = checkNotNull(dataSet);
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
        Picasso.with(mContext)
                .load(mDataSet.get(position).getPosterPath())
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) return 0;
        return mDataSet.size();
    }
}