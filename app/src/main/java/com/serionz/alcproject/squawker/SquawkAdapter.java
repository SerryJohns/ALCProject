package com.serionz.alcproject.squawker;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.serionz.alcproject.R;
import com.serionz.alcproject.squawker.provider.SquawkContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by johnpaulseremba on 06/04/2018.
 */

public class SquawkAdapter extends RecyclerView.Adapter<SquawkAdapter.SquawkViewHolder> {

    private Cursor mData;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");

    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @Override
    public SquawkAdapter.SquawkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_squawk_list, parent, false);
        SquawkViewHolder viewHolder = new SquawkViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SquawkViewHolder holder, int position) {
        mData.moveToPosition(position);

        String message = mData.getString(SquawkerMainActivity.COL_NUM_MESSAGE);
        String author = mData.getString(SquawkerMainActivity.COL_NUM_AUTHOR);
        String authorKey = mData.getString(SquawkerMainActivity.COL_NUM_AUTHOR_KEY);

        // Get date for displaying
        long dateMills = mData.getLong(SquawkerMainActivity.COL_NUM_DATE);
        String date = "";
        long now = System.currentTimeMillis();

        if (now - dateMills < (DAY_MILLIS)) {
            if (now - dateMills < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMills) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            } else {
                long minutes = Math.round((now - dateMills) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMills);
            date = sDateFormat.format(dateDate);
        }

        // Add a dot to the date string
        date = "\u2022 " + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);

        switch (authorKey) {
            case SquawkContract.ASSER_KEY:
                holder.authorImageView.setImageResource(R.drawable.asser);
                break;
            case SquawkContract.CEZANNE_KEY:
                holder.authorImageView.setImageResource(R.drawable.cezanne);
                break;
            case SquawkContract.JLIN_KEY:
                holder.authorImageView.setImageResource(R.drawable.jlin);
                break;
            case SquawkContract.LYLA_KEY:
                holder.authorImageView.setImageResource(R.drawable.lyla);
                break;
            case SquawkContract.NIKITA_KEY:
                holder.authorImageView.setImageResource(R.drawable.nikita);
                break;
            default:
                holder.authorImageView.setImageResource(R.drawable.test);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mData = newCursor;
        notifyDataSetChanged();
    }

    public class SquawkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author_text_view) TextView authorTextView;
        @BindView(R.id.message_text_view) TextView messageTextView;
        @BindView(R.id.date_text_view) TextView dateTextView;
        @BindView(R.id.author_image_view) ImageView authorImageView;

        public SquawkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            authorImageView.setImageResource(R.drawable.asser);
        }
    }

}
