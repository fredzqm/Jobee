package com.fredzqm.jobee.job_seeker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fredzqm.jobee.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhang on 5/29/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mStrings = new ArrayList<>();
    private Random mRandom = new Random();
    private boolean mEditing;

    private RecyclerView mRecycleView;

    public ItemAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mEditing = false;
        mRecycleView = recyclerView;
        for (int i = 0; i < 5; i++) {
            mStrings.add(getRandomName());
        }
    }

    private String getRandomName() {
        String[] names = new String[]{
                "Hannah", "Emily", "Sarah", "Madison", "Brianna",
                "Kaylee", "Kaitlyn", "Hailey", "Alexis", "Elizabeth",
                "Michael", "Jacob", "Matthew", "Nicholas", "Christopher",
                "Joseph", "Zachary", "Joshua", "Andrew", "William"
        };
        return names[mRandom.nextInt(names.length)];
    }

    public void addName() {
        mStrings.add(0, getRandomName());
        notifyDataSetChanged();
    }

    public void removeName(int position) {
        mStrings.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mEditText.setText(mStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            mEditText = (TextView) itemView.findViewById(R.id.item_view_item_text);
            mEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mEditing) {
                        mEditing = true;
                        final EditText editText = new EditText(mContext);
                        editText.setText(mStrings.get(getAdapterPosition()));
                        new AlertDialog.Builder(mContext)
                                .setView(editText)
                                .setTitle("Edit")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mStrings.set(getAdapterPosition(), editText.getText().toString());
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
    }
}
