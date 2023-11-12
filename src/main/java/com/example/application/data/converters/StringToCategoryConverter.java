package com.example.application.data.converters;

import com.example.application.data.entity.Category;
import com.example.application.data.service.CategoryService;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {
    @Autowired
    private CategoryService categoryService;
    @Override
    public Result<Category> convertToModel(String value, ValueContext context) {
        // Convert the selected category name to a Category object
        Category category = categoryService.getCategoryByName(value);
        return Result.ok(category);
    }

    @Override
    public String convertToPresentation(Category value, ValueContext context) {
        // Convert the Category object to its display name
        return value.getSubCategoryName();
    }
}
