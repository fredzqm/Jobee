package com.fredzqm.jobee.job_seeker.resume;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;


/**
 * Created by zhang on 7/13/2016.
 */
public class ResumeContent extends ArrayList<String> {
    private String type;

    public ResumeContent() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addContent(String str){
        this.add(str);
    }

    public static ResumeContent newInstance() {
        ResumeContent resumeContent = new ResumeContent();
        resumeContent.setType("type");
        resumeContent.addContent("Element 1");
        resumeContent.addContent("Element 2");
        return resumeContent;
    }
}
