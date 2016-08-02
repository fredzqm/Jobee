package com.fredzqm.jobee.recruiter.JobList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.recruiter.JobList.JobListFragment.Callback;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Job} and makes a call to the
 * specified {@link Callback}.
 */
public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {

    private final List<Job> mValues;
    private final Callback mCallback;

    public JobListAdapter(List<Job> items, Callback callback) {
        mValues = items;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_joblist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mJob = mValues.get(position);
        holder.updateView();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleTextView;
        public final TextView mCityTextView;
        public final TextView mDateTextView;

        public Job mJob;

        public ViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.re_list_item_title);
            mDateTextView = (TextView) view.findViewById(R.id.re_list_item_date);
            mCityTextView = (TextView) view.findViewById(R.id.re_list_item_city);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        mCallback.showJobDetail(mJob);
                    }
                }
            });
        }

        public void updateView() {
            mTitleTextView.setText(mJob.getTitle());
            mDateTextView.setText((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(mJob.getDate()));
            mCityTextView.setText(mJob.getCity());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleTextView.getText() + "'";
        }

    }
}
