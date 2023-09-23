package com.example.application.views.listCategory;

import com.example.application.data.entity.Category;
import com.example.application.data.service.CategoryService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

//@PermitAll
@Route(value="categories", layout = MainLayout.class)
@PageTitle("Categories | GlosSys")
@RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
public class CategoryView extends VerticalLayout {
    // omitted
    Grid<Category> grid = new Grid<>(Category.class);
    TextField filterText = new TextField();
    CategoryForm form;
    CategoryService service;

    public CategoryView(CategoryService service) {
        this.service = service;
        addClassName("category-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new CategoryForm();
        form.setWidth("25em");
        form.addListener(CategoryForm.SaveEvent.class, this::saveCategory);
        form.addListener(CategoryForm.DeleteEvent.class, this::deleteCategory);
        form.addListener(CategoryForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        service.saveCategory(event.getCategory());
        updateList();
        resetEditor();
    }

    private void deleteCategory(CategoryForm.DeleteEvent event) {
        service.deleteCategory(event.getCategory());
        updateList();
        resetEditor();
    }

    private void configureGrid() {
        grid.addClassNames("category-grid");
        grid.setSizeFull();
        grid.setColumns("subCategoryName","categoryName");
        //grid.addColumn(category -> category.getCategoryName()).setHeader("Category");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCategory(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by category name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCategoryButton = new Button("Add category");
        addCategoryButton.addClickListener(click -> addCategory());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCategoryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editCategory(Category category) {
        if (category == null) {
            closeEditor();
        } else {
            form.setCategory(category);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCategory(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void resetEditor(){
        form.setCategory(null);
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void updateList() {
        grid.setItems(service.findAllCategories(filterText.getValue()));
    }
}
