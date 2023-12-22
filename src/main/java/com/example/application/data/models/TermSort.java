package com.example.application.data.models;

public class TermSort {

    private String propertyName;
    private boolean descending;

    public TermSort(String sorted, boolean equals) {
        this.propertyName = sorted;
        this.descending = equals;
    }

    public TermSort(){}

    public String getPropertyName(){
        return propertyName;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = this.propertyName;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }
}
