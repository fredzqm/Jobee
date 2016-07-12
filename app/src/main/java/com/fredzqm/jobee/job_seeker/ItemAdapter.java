package com.fredzqm.jobee.job_seeker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> mStrings = new ArrayList<>();
    private Random mRandom = new Random();

    private RecyclerView mRecycleView;

    public ItemAdapter(Context context, RecyclerView recyclerView){
        mContext = context;
        mRecycleView = recyclerView;
        for (int i = 0 ; i < 5; i++){
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

    public void removeName(int position){
        mStrings.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (EditText) itemView.findViewById(R.id.item_view_item_text);
//            mTextView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    removeName(getAdapterPosition());
//                    return false;
//                }
//            });
        }
    }
}
