package com.htcardone.popularmovies.moviedetail;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.htcardone.popularmovies.R;
import com.htcardone.popularmovies.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewItemViewHolder> {
    private List<Review> mDataSet;
    private final Context mContext;
    private final ListItemClickListener mOnClickListener;

    public class ReviewItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviews_content_tv)
        TextView contentTextView;
        @BindView(R.id.reviews_author_tv)
        TextView authorTextView;
        @BindView(R.id.reviews_open_review_bt)
        Button openReviewButton;

        public ReviewItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            openReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClick(mDataSet.get(getAdapterPosition()).getUrl());
                }
            });
        }
    }

    public ReviewsAdapter(Context context, ListItemClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(String reviewUrl);
    }


    public void replaceData(List<Review> videos) {
        mDataSet = checkNotNull(videos);
        notifyDataSetChanged();
    }

    @Override
    public ReviewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewItemViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.contentTextView.setText(Html.fromHtml(mDataSet.get(position).getContent(),
                    Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.contentTextView.setText(Html.fromHtml(mDataSet.get(position).getContent()));
        }
        holder.authorTextView.setText(mDataSet.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) return 0;
        return mDataSet.size();
    }
}
