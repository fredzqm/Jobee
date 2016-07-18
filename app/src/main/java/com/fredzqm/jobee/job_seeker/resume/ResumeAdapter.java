package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fredzqm.jobee.R;

import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    private Context mContext;
    private Resume mResume;
    private boolean mEditing;

    private Random mRandom = new Random();

    public ResumeAdapter(Context context) {
        mContext = context;
        mEditing = false;
    }

    public void addCategory(String category) {
        category = category.replace("\n", " ");
        mResume.add(ResumeCategory.newInstance(category));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.js_resume_item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updateUI(this.mResume.get(position));
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public void setResume(Resume resume) {
        mResume = resume;
        notifyDataSetChanged();
    }

    public Resume getResume(){
        return mResume;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ResumeCategory mResumeCategory;

        TextView mTypeTextView;
        ImageButton mEditButton;
        LinearLayout mListView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mResumeCategory = new ResumeCategory();

            mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
            mTypeTextView.setOnClickListener(this);
            mEditButton = (ImageButton) itemView.findViewById(R.id.edit_button);
            mEditButton.setOnClickListener(this);
            mListView = (LinearLayout) itemView.findViewById(R.id.listviewTasks);
        }

        public void updateUI(ResumeCategory resumeCategory) {
            this.mResumeCategory = resumeCategory;
            mTypeTextView.setText(mResumeCategory.getType());
            mListView.removeAllViews();
            for (int i = 0 ; i < mResumeCategory.size(); i++) {
                String s = mResumeCategory.get(i);
                TextView tv = new TextView(mContext);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setId(i);
                tv.setTextSize(20);
                tv.setText(s);
                tv.setOnClickListener(this);
                mListView.addView(tv);
            }
        }

        @Override
        public void onClick(View view) {
            if (!mEditing) {
                mEditing = true;
                if (view == mTypeTextView || view == mEditButton) {
                    final EditText editText = new EditText(mContext);
                    editText.setText(mResumeCategory.getType());
                    new AlertDialog.Builder(mContext)
                            .setView(editText)
                            .setTitle("Edit Category")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mResumeCategory.setType(editText.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mResume.remove(mResumeCategory);
                                    notifyDataSetChanged();
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    mEditing = false;
                                }
                            })
                            .show();
                }else{
                    final int position = view.getId();
                    final EditText editText = new EditText(mContext);
                    editText.setText(mResumeCategory.get(position));
                    new AlertDialog.Builder(mContext)
                            .setView(editText)
                            .setTitle("Edit")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = editText.getText().toString();
                                    mResumeCategory.set(position, str);
                                    TextView textView = (TextView) mListView.getChildAt(position);
                                    textView.setText(str);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
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
}
