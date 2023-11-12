package com.example.application.data.service;

import com.example.application.data.entity.Category;
import com.example.application.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAllCategories(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.search(stringFilter);
        }
    }

    public Category getCategoryByName(String stringFilter){
        return categoryRepository.search(stringFilter).get(0);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
    public long countCategories() {
        return categoryRepository.count();
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void saveCategory(Category category) {
        if (category == null) {
            System.err.println("Category is null. Are you sure you have connected your form to the application?");
            return;
        }
        categoryRepository.save(category);
    }
}
