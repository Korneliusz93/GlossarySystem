package com.example.application.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Category extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotBlank
    private String subCategoryName;
    @NotBlank
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() { return subCategoryName; }

    public void setSubCategoryName(String subCategoryName) { this.subCategoryName = subCategoryName; }
}
