package com.fredzqm.jobee.job_seeker.resume;

import java.util.ArrayList;

/**
 * Created by zhang on 7/13/2016.
 */
public class Resume extends ArrayList<ResumeCategory>{

    public Resume() {
        super();
    }

    public static Resume newResume() {
        Resume resume = new Resume();
        resume.add(ResumeCategory.newInstance("Skills"));
        resume.add(ResumeCategory.newInstance("Education"));
        resume.add(ResumeCategory.newInstance("Experience"));
        return resume;
    }
}
