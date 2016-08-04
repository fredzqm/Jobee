package com.fredzqm.jobee.recruiter.ResumeList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeListFragment.Callback;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Resume} and makes a call to the
 * specified {@link Callback}.
 */
public class ResumeListAdapter extends RecyclerView.Adapter<ResumeListAdapter.ViewHolder> {

    private final List<Resume> mValues;
    private final Callback mCallback;

    public ResumeListAdapter(List<Resume> items, Callback callback) {
        mValues = items;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_resumelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mResume = mValues.get(position);
        holder.updateView();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameTextView;
        public final TextView mCityTextView;
        public final TextView mMajorTextView;

        public Resume mResume;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.re_resumelist_item_name);
            mMajorTextView = (TextView) view.findViewById(R.id.re_resumelist_item_major);
            mCityTextView = (TextView) view.findViewById(R.id.re_resumelist_item_city);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        mCallback.showResumeDetail(mResume);
                    }
                }
            });
        }

        public void updateView() {
            mNameTextView.setText(mResume.getName());
            mMajorTextView.setText(mResume.getMajor());
            mCityTextView.setText(mResume.getCity());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }

    }
}
