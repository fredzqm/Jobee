package com.fredzqm.jobee.recruiter.ResumeList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Resume;
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

    private final List<Resume> mResumes;
    private final Callback mCallback;

    private DatabaseReference mRef;

    public ResumeListAdapter(Callback callback) {
        mResumes = new ArrayList<>();
        mCallback = callback;
        mRef = Resume.getReference();
        mRef.orderByChild(Resume.RECRUITER_KEY).equalTo(mCallback.getUserID()).addChildEventListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_resumelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mResume = mResumes.get(position);
        holder.updateView();
    }

    @Override
    public int getItemCount() {
        return mResumes.size();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Resume added = dataSnapshot.getValue(Resume.class);
        String key = dataSnapshot.getKey();
        added.setKey(key);
        mResumes.add(0, added);
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Resume changedTo = dataSnapshot.getValue(Resume.class);
        String key = dataSnapshot.getKey();
        for (int i = 0; i < mResumes.size(); i++) {
            if (key.equals( mResumes.get(i).getKey())) {
                mResumes.set(i, changedTo);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        for (int i = 0; i < mResumes.size(); i++) {
            if (key.equals(mResumes.get(i).getKey())) {
                mResumes.remove(i);
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
        public final TextView mNameTextView;
//        public final TextView mCityTextView;
        public final TextView mMajorTextView;

        public Resume mResume;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.re_resumelist_item_name);
            mMajorTextView = (TextView) view.findViewById(R.id.re_resumelist_item_major);
//            mCityTextView = (TextView) view.findViewById(R.id.re_resumelist_item_city);

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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }

    }
}
