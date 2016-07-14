package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
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
    private RecyclerView mRecycleView;
    private boolean mEditing;

    private Random mRandom = new Random();

    public ResumeAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mEditing = false;
        mRecycleView = recyclerView;
        mResume = new Resume();
        for (int i = 0; i < 5; i++) {
            mResume.add(0, ResumeContent.newInstance());
        }
    }

    public void addName() {
        mResume.add(0, ResumeContent.newInstance());
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
        holder.mAdapter.notifyDataSetChanged();
//        setListViewHeightBasedOnChildren(holder.mListView);
        holder.mTypeTextView.setText(holder.mResumeContent.getType());
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return mResume.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener {
        TextView mTypeTextView;
        ListView mListView;
        ResumeContentListAdapter mAdapter;
        ResumeContent mResumeContent;

        public ViewHolder(final View itemView) {
            super(itemView);
            mAdapter = new ResumeContentListAdapter();
            mResumeContent = new ResumeContent();

            mTypeTextView = (TextView) itemView.findViewById(R.id.js_resume_item_content_title);
            mTypeTextView.setOnClickListener(this);
            mListView = (ListView) itemView.findViewById(R.id.listviewTasks);
            mListView.setTextFilterEnabled(true);
            mListView.setOnItemClickListener(this);
            mListView.setAdapter(mAdapter);
        }

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

        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long id) {
            if (!mEditing) {
                mEditing = true;
                final EditText editText = new EditText(mContext);
                editText.setText(mResumeContent.get(position));
                new AlertDialog.Builder(mContext)
                        .setView(editText)
                        .setTitle("Edit")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = editText.getText().toString();
                                mResumeContent.set(position, str);
                                mAdapter.notifyDataSetChanged();
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


        /**
         * Created by zhang on 7/14/2016.
         */
        public class ResumeContentListAdapter extends BaseAdapter {

            public ResumeContentListAdapter() {
            }

            @Override
            public int getCount() {
                return mResumeContent.size();
            }

            @Override
            public Object getItem(int i) {
                return mResumeContent.get(i);
            }

            @Override
            public long getItemId(int i) {
                return mResumeContent.get(i).hashCode();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.js_resume_item_content_item_view, parent, false);
                } else {
                    view = convertView;
                }
                String name = mResumeContent.get(position);
                TextView nameTextView = (TextView) view.findViewById(R.id.js_resume_item_content_detail);
                nameTextView.setText(name);
                return view;
            }
        }

    }

}
