package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fredzqm.jobee.R;
import com.fredzqm.jobee.model.Resume;
import com.fredzqm.jobee.model.ResumeCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> implements ValueEventListener {
    private static final String TAG = "ResumeAdapter";
    private Context mContext;
    private Resume mResume;
    private boolean mEditing;
    private DatabaseReference mRef;

    public ResumeAdapter(Context context) {
        mContext = context;
        mEditing = false;
        mResume = new Resume();
    }

    public void setResume(Resume resume) {
        mResume = resume;
        mRef = Resume.getReference().child(mResume.getKey());
        mRef.addValueEventListener(this);
    }

    public void addCategory(String category) {
        category = category.replace("\n", " ");
        mResume.add(ResumeCategory.newInstance(category));
        if (mRef != null)
            mRef.setValue(mResume);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mResume = dataSnapshot.getValue(Resume.class);
        notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled " + databaseError.getMessage());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.js_resume_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mResumeCategory = this.mResume.get(position);
        holder.updateUI();
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ResumeCategory mResumeCategory;

        TextView mTypeTextView;
        ImageButton mAddButton;
        LinearLayout mListView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mResumeCategory = new ResumeCategory();
            mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
            mTypeTextView.setOnClickListener(this);
            mAddButton = (ImageButton) itemView.findViewById(R.id.add_button);
            mAddButton.setOnClickListener(this);
            mListView = (LinearLayout) itemView.findViewById(R.id.listviewTasks);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ViewHolder.this.onClick(mTypeTextView);
                    return false;
                }
            });
        }

        public void updateUI() {
            mTypeTextView.setText(mResumeCategory.getType());
            mListView.removeAllViews();
            for (int i = 0; i < mResumeCategory.size(); i++) {
                String s = mResumeCategory.get(i);
                appendDetailToListView(s);
            }
        }

        private void appendDetailToListView(String s) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setId(mListView.getChildCount());
            tv.setTextSize(20);
            tv.setText(s);
            tv.setOnClickListener(this);
            mListView.addView(tv);
        }

        @Override
        public void onClick(final View view) {
            if (!mEditing) {
                mEditing = true;
                final EditText editText = new EditText(mContext);
                final int position = view.getId();
                String title;
                if (view == mTypeTextView) {
                    editText.setText(mResumeCategory.getType());
                    title = "Edit Category";
                } else if (view == mAddButton) {
                    editText.setHint(mResumeCategory.getType());
                    title = "Add " + mResumeCategory.getType();
                } else { // detail in the list
                    editText.setText(mResumeCategory.get(position));
                    title = "Edit";
                }
                new AlertDialog.Builder(mContext)
                        .setView(editText)
                        .setTitle(title)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = editText.getText().toString();
                                if (view == mTypeTextView) {
                                    mResumeCategory.setType(str);
                                } else if (view == mAddButton) {
                                    mResumeCategory.add(str);
                                } else { // detail in the list
                                    mResumeCategory.set(position, str);
                                }
                                mRef.setValue(mResume);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (view != mAddButton) {
                                    if (view == mTypeTextView) {
                                        mResume.remove(mResumeCategory);
                                    } else { // detail in the list
                                        mResumeCategory.remove(position);
                                    }
                                    mRef.setValue(mResume);
                                }
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                mEditing = false;
                            }
                        })
                        .show();
            }
        }

    }
}
