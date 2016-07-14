package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fredzqm.jobee.R;

import java.util.List;
import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    private Context mContext;
    private Resume mResume;
    private RecyclerView mRecycleView;
    private boolean mEditing;

    private Random mRandom = new Random();

    public ResumeAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mEditing = false;
        mRecycleView = recyclerView;
        mResume = new Resume();
        for (int i = 0; i < 5; i++) {
            mResume.add(getRandomName());
        }
    }

    private ResumeContent getRandomName() {
        String[] names = new String[]{
                "Hannah", "Emily", "Sarah", "Madison", "Brianna",
                "Kaylee", "Kaitlyn", "Hailey", "Alexis", "Elizabeth",
                "Michael", "Jacob", "Matthew", "Nicholas", "Christopher",
                "Joseph", "Zachary", "Joshua", "Andrew", "William"
        };
        return new ResumeContent(names[mRandom.nextInt(names.length)]);
    }

    public void addName() {
        mResume.add(0, getRandomName());
        notifyDataSetChanged();
    }

    public void removeName(int position) {
        mResume.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.js_resume_item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mResumeContent = this.mResume.get(position);
        holder.updateUI();
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTypeTextView;
//        LinearLayout mLinearLayout;
        ListView mListView;
        ResumeContent mResumeContent;
        boolean setUp;

        public ViewHolder(View itemView) {
            super(itemView);
            mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
//            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.js_resume_item_content_detail_list);
            mListView = (ListView) itemView.findViewById(R.id.listviewTasks);

            mTypeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mEditing) {
                        mEditing = true;
                        final EditText editText = new EditText(mContext);
                        editText.setText(mResumeContent.getType());
                        new AlertDialog.Builder(mContext)
                                .setView(editText)
                                .setTitle("Edit")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mResumeContent.setType(editText.getText().toString());
                                        notifyDataSetChanged();
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
            });
        }

        public void updateUI() {
            Log.d("OK", "updateUI " + setUp);
            if (!setUp) {
                setUp = true;
                mTypeTextView.setText(mResumeContent.getType());
//                for (String str : mResumeContent) {
//                    TextView view = new TextView(mContext);
//                    view.setText(str);
////                    mLinearLayout.addView(view);
//                }

                ListAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mResumeContent);
                mListView.setAdapter(adapter);
            }
        }
    }
}
