package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fredzqm.jobee.R;

import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    private Context mContext;
//    private ArrayList<String> mStrings = new ArrayList<>();
    private Random mRandom = new Random();
    private Resume mResume;
    private boolean mEditing;

    private RecyclerView mRecycleView;

    public ResumeAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mEditing = false;
        mRecycleView = recyclerView;
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
        holder.mResumeContent = mResume.get(position);
        holder.updateUI();
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mEditText;
        ResumeContent mResumeContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mEditText = (TextView) itemView.findViewById(R.id.item_view_item_text);
            mEditText.setOnClickListener(new View.OnClickListener() {
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
            mEditText.setText(mResumeContent.getType());
        }
    }
}
