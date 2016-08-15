package com.fredzqm.jobee.job_seeker.AppliedJob;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.AppliedJob.AppliedJobListFragment.Callback;
import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.fredzqm.jobee.model.Job;
import com.fredzqm.jobee.model.Submission;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Job} and makes a call to the
 * specified {@link Callback}.
 */
public class AppliedJobListAdapter extends RecyclerView.Adapter<AppliedJobListAdapter.ViewHolder> implements ChildEventListener {
    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final List<Submission> mSubmissions;
    private String mSubmissionKey;

    private final Callback mCallback;
    private DatabaseReference mRef;

    public AppliedJobListAdapter(Callback callback) {
        mSubmissions = new ArrayList<>();
        mCallback = callback;
        mRef = Submission.getReference();
        mRef.orderByChild(Submission.JOBSEEKER_KEY).equalTo(mCallback.getUserID()).addChildEventListener(this);
    }

    public AppliedJobListAdapter(Callback callback, String submissionKey) {
        mSubmissions = new ArrayList<>();
        mCallback = callback;
        this.mSubmissionKey = submissionKey;
        mRef = Submission.getReference();
        mRef.orderByChild(Submission.JOBSEEKER_KEY).equalTo(mCallback.getUserID()).addChildEventListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.js_appliedjoblist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSubmission = mSubmissions.get(position);
        holder.updateView();
    }

    @Override
    public int getItemCount() {
        return mSubmissions.size();
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Submission added = dataSnapshot.getValue(Submission.class);
        String key = dataSnapshot.getKey();
        added.setKey(key);
        mSubmissions.add(0, added);
        notifyDataSetChanged();
        if (key.equals(mSubmissionKey)) {
            mCallback.showJobDetail(added);
            mSubmissionKey = null;
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Submission changedTo = dataSnapshot.getValue(Submission.class);
        String key = dataSnapshot.getKey();
        for (int i = 0; i < mSubmissions.size(); i++) {
            if (key.equals(mSubmissions.get(i).getKey())) {
                mSubmissions.set(i, changedTo);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        for (int i = 0; i < mSubmissions.size(); i++) {
            if (key.equals(mSubmissions.get(i).getKey())) {
                mSubmissions.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("Error", "onCancelled: " + databaseError.getMessage());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleTextView;
        public final TextView mCityTextView;
        public final TextView mStatusTextView;
        public final TextView mCompanyTextView;

        public Submission mSubmission;

        public ViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.js_appliedjoblist_item_title);
            mCompanyTextView = (TextView) view.findViewById(R.id.js_appliedjoblist_item_company);
            mStatusTextView = (TextView) view.findViewById(R.id.js_appliedjoblist_item_status);
            mCityTextView = (TextView) view.findViewById(R.id.js_appliedjoblist_item_city);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        mCallback.showJobDetail(mSubmission);
                    }
                }
            });
        }

        public void updateView() {
            Job mJob = mSubmission.getJob();
            mTitleTextView.setText(mJob.getTitle());
            mCompanyTextView.setText(mJob.getCompany());
            mStatusTextView.setText(mSubmission.getStatus());
            mCityTextView.setText(mJob.getCity());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleTextView.getText() + "'";
        }

    }
}
