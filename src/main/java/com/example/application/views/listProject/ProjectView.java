package com.example.application.views.listProject;

import com.example.application.data.entity.Project;
import com.example.application.data.service.CategoryService;
import com.example.application.data.service.ClientService;
import com.example.application.data.service.LanguageService;
import com.example.application.data.service.ProjectService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PermitAll
@Route(value="projects", layout = MainLayout.class)
@PageTitle("Projects | Gloss system")
@RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
public class ProjectView extends VerticalLayout {
    Grid<Project> grid = new Grid<>(Project.class);
    TextField filterText = new TextField();
    ProjectForm form;
    ProjectService service;
    ClientService clientService;
    LanguageService languageService;
    CategoryService categoryService;
    public ProjectView(ProjectService service, ClientService clientService, CategoryService categoryService,
                        LanguageService languageService){
        this.service = service;
        this.clientService = clientService;
        this.categoryService = categoryService;
        this.languageService = languageService;
        addClassName("project-view");
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
        form = new ProjectForm(categoryService,languageService,clientService);
        form.setWidth("25em");
        form.addListener(ProjectForm.SaveEvent.class, this::saveProject);
        form.addListener(ProjectForm.DeleteEvent.class, this::deleteProject);
        form.addListener(ProjectForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveProject(ProjectForm.SaveEvent event) {
        service.saveProject(event.getProject());
        updateList();
        resetEditor();
    }
    private void deleteProject(ProjectForm.DeleteEvent event) {
        service.deleteProject(event.getProject());
        updateList();
        resetEditor();
    }
    private void configureGrid() {
        grid.addClassNames("project-grid");
        grid.setSizeFull();
        grid.setColumns("projectName");
        grid.addColumn(project -> project.getCategory().getSubCategoryName()).setHeader("Category");
        grid.addColumn(project -> project.getClient().getClientName()).setHeader("Client");
        grid.addColumn(project -> project.getSourceLanguage().getLanguageName()).setHeader("Source Language");
        grid.addColumn(project -> project.getTargetLanguage().getLanguageName()).setHeader("Target Language");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editProject(event.getValue()));
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by project...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addProjectButton = new Button("Add project");
        addProjectButton.addClickListener(click -> addProject());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editProject(Project project) {
        if (project == null) {
            closeEditor();
        } else {
            form.setProject(project);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setProject(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void resetEditor(){
        form.setProject(null);
        grid.asSingleSelect().clear();
        editProject(new Project());
    }
    private void addProject() {
        grid.asSingleSelect().clear();
        editProject(new Project());
    }
    private void updateList() {
        grid.setItems(service.findAllProjects(filterText.getValue()));
    }

}
