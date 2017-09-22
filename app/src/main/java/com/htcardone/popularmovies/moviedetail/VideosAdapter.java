package com.htcardone.popularmovies.moviedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.model.Video;
import com.htcardone.popularmovies.movies.MoviesAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by Henrique Cardone on 21/09/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoItemViewHolder> {
    private List<Video> mDataSet;
    private final Context mContext;
    private final ListItemClickListener mOnClickListener;

    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videos_thumbnail_iv)
        ImageView thumbnailImageView;
        @BindView(R.id.videos_title_tv)
        TextView titleTextView;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClick(mDataSet.get(getAdapterPosition()).getKey());
                }
            });
        }
    }

    public VideosAdapter(Context context, ListItemClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(String youtubeKey);
    }


    public void replaceData(List<Video> videos) {
        mDataSet = checkNotNull(videos);
        notifyDataSetChanged();
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.videos_list_item, parent, false);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
        //TODO check if video is really from YouTube
        Picasso.with(mContext)
                .load("https://img.youtube.com/vi/" + mDataSet.get(position).getKey() + "/0.jpg")
                .into(holder.thumbnailImageView);
        holder.titleTextView.setText(mDataSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) return 0;
        return mDataSet.size();
    }
}
