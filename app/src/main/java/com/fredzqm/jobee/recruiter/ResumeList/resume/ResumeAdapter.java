package com.fredzqm.jobee.recruiter.ResumeList.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {
    private Resume mResume;
    private Context mContext;

    public ResumeAdapter(Context context, Resume resume) {
        mContext = context;
        mResume = resume;
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

    public Resume getResume() {
        return mResume;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ResumeCategory mResumeCategory;

        TextView mTypeTextView;
        ImageButton mAddButton;
        LinearLayout mListView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mResumeCategory = new ResumeCategory();

            mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
            mAddButton = (ImageButton) itemView.findViewById(R.id.add_button);
            mListView = (LinearLayout) itemView.findViewById(R.id.listviewTasks);
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
            mListView.addView(tv);
        }
    }
}
