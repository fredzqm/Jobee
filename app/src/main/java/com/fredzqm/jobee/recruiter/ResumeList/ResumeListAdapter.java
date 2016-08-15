package com.fredzqm.jobee.recruiter.ResumeList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.model.Submission;
import com.fredzqm.jobee.recruiter.ResumeList.ResumeListFragment.Callback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Resume} and makes a call to the
 * specified {@link Callback}.
 */
public class ResumeListAdapter extends RecyclerView.Adapter<ResumeListAdapter.ViewHolder> implements ChildEventListener {

    private final List<Submission> mSubmissions;
    private int mLastShownResumeIndex;
    private String mSubmissionKey;

    private final Callback mCallback;
    private DatabaseReference mRef;

    public ResumeListAdapter(Callback callback, String submissionKey) {
        mSubmissions = new ArrayList<>();
        mCallback = callback;
        this.mSubmissionKey = submissionKey;
        mRef = Submission.getReference();
        mRef.orderByChild(Submission.RECRUITER_KEY).equalTo(mCallback.getUserID()).addChildEventListener(this);
    }

    public ResumeListAdapter(Callback callback) {
        mSubmissions = new ArrayList<>();
        mCallback = callback;
        mRef = Submission.getReference();
        mRef.orderByChild(Submission.RECRUITER_KEY).equalTo(mCallback.getUserID()).addChildEventListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_resumelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Resume resume = mSubmissions.get(position).getResume();
        if (resume != null) {
            holder.mSubmission = mSubmissions.get(position);
            holder.mStatus = mSubmissions.get(position).getStatus();
            holder.updateView();
        }
    }

    @Override
    public int getItemCount() {
        return mSubmissions.size();
    }

    public void showNext() {
        mLastShownResumeIndex++;
        if (mLastShownResumeIndex < mSubmissions.size())
            mCallback.showResumeDetail(mSubmissions.get(mLastShownResumeIndex));
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        final Submission added = dataSnapshot.getValue(Submission.class);
        String key = dataSnapshot.getKey();
        added.setKey(key);
        mSubmissions.add(0, added);
        notifyDataSetChanged();
        if (key.equals(mSubmissionKey)){
            mCallback.showResumeDetail(added);
            mSubmissionKey = null;
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Submission changedTo = dataSnapshot.getValue(Submission.class);
        String key = dataSnapshot.getKey();
        changedTo.setKey(key);
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
        public final TextView mNameTextView;
        //        public final TextView mCityTextView;
        public final TextView mMajorTextView;

        public Submission mSubmission;
        public String mStatus;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.re_resumelist_item_name);
            mMajorTextView = (TextView) view.findViewById(R.id.re_resumelist_item_major);
//            mCityTextView = (TextView) view.findViewById(R.id.re_resumelist_item_city);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mCallback) {
                        mLastShownResumeIndex = mSubmissions.indexOf(mSubmission);
                        mCallback.showResumeDetail(mSubmission);
                    }
                }
            });
        }

        public void updateView() {
            mNameTextView.setText(mSubmission.getResume().getName());
            mMajorTextView.setText(mSubmission.getResume().getMajor());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }

    }
}
