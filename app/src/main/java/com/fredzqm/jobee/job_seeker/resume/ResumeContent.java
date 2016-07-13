package com.fredzqm.jobee.job_seeker.resume;

import android.widget.ArrayAdapter;

import java.util.ArrayList;


/**
 * Created by zhang on 7/13/2016.
 */
public class ResumeContent extends ArrayList<String> {
    private String type;

    public ResumeContent(String type) {
        super();
        this.type = type;
        this.add("Element 1");
        this.add("Element 2");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
