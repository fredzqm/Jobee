package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.sax.RootElement;
import android.widget.ArrayAdapter;

import com.fredzqm.jobee.R;

/**
 * Created by zhang on 7/18/2016.
 */
public class ResumeSwitchListAdapter extends ArrayAdapter<Resume> {
    public ResumeSwitchListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    public int getCount(){
        return super.getCount() + 1;
    }

    public Resume getItem(int position){
        if (position == super.getCount()){
            return Resume.createResumeStub;
        }
        return super.getItem(position);
    }
}
