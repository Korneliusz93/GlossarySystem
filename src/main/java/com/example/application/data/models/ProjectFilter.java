package com.example.application.data.models;

import org.springframework.stereotype.Component;

@Component
public class ProjectFilter {
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
