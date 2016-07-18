package com.fredzqm.jobee.job_seeker.resume;

import java.util.ArrayList;

/**
 * Created by zhang on 7/13/2016.
 */
public class Resume extends ArrayList<ResumeCategory>{
    private String name;

    public Resume(String resumeName) {
        super();
        this.name = resumeName;
    }

    public static Resume newResume(String resumeName) {
        Resume resume = new Resume(resumeName);
        resume.add(ResumeCategory.newInstance("Skills"));
        resume.add(ResumeCategory.newInstance("Education"));
        resume.add(ResumeCategory.newInstance("Experience"));
        return resume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
