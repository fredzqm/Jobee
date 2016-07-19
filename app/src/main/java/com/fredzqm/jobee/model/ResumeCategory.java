package com.fredzqm.jobee.model;

import java.util.ArrayList;


/**
 * Created by zhang on 7/13/2016.
 */
public class ResumeCategory extends ArrayList<String> {
    private String type;

    public ResumeCategory() {
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

    public static ResumeCategory newInstance(String type) {
        ResumeCategory resumeCategory = new ResumeCategory();
        resumeCategory.setType(type);
        resumeCategory.addContent("Element 1");
        resumeCategory.addContent("Element 2");
        return resumeCategory;
    }
}
