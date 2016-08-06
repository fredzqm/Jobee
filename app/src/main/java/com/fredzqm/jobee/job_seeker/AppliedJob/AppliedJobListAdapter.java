package com.fredzqm.jobee.job_seeker.AppliedJob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.AppliedJob.AppliedJobListFragment.Callback;
import com.fredzqm.jobee.model.Job;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Job} and makes a call to the
 * specified {@link Callback}.
 */
public class AppliedJobListAdapter extends RecyclerView.Adapter<AppliedJobListAdapter.ViewHolder> {
    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final List<Job> mValues;
    private final Callback mCallback;

    public AppliedJobListAdapter(List<Job> items, Callback callback) {
        mValues = items;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.js_joblist_item, parent, false);
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
        public final TextView mCompanyTextView;

        public Job mJob;

        public ViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.js_list_item_title);
            mCompanyTextView = (TextView) view.findViewById(R.id.js_list_item_company);
            mDateTextView = (TextView) view.findViewById(R.id.js_list_item_date);
            mCityTextView = (TextView) view.findViewById(R.id.js_list_item_city);

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
            mCompanyTextView.setText(mJob.getCompany());
            mDateTextView.setText(DATAFORMAT.format(mJob.getDate()));
            mCityTextView.setText(mJob.getCity());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleTextView.getText() + "'";
        }

    }
}
