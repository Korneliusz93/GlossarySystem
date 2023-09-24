package com.example.application.views.listTerm;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.service.ProjectService;
import com.example.application.data.service.TermService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PermitAll
@Route(value="terms", layout = MainLayout.class)
@PageTitle("Terms | Gloss system")
@RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER", "ROLE_GUEST" })
public class TermView extends VerticalLayout {
    Grid<Term> grid = new Grid<>(Term.class);
    TextField filterText = new TextField();
    ComboBox<Project> projectBox = new ComboBox<Project>();
    TermForm form;
    String selectedValue;
    TermService termService;
    ProjectService projectService;
    public TermView(TermService service, ProjectService projectService){
        this.termService = service;
        this.projectService = projectService;
        addClassName("term-view");
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
        form = new TermForm(projectService);
        form.setWidth("25em");
        form.addListener(TermForm.SaveEvent.class, this::saveTerm);
        form.addListener(TermForm.DeleteEvent.class, this::deleteTerm);
        form.addListener(TermForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveTerm(TermForm.SaveEvent event) {
        termService.saveTerm(event.getTerm());
        updateList();
        resetEditor();
    }
    private void deleteTerm(TermForm.DeleteEvent event) {
        termService.deleteTerm(event.getTerm());
        updateList();
        resetEditor();
    }
    private static ComponentRenderer<TermDetailsFormLayout, Term> createTermDetailsRenderer() {
        return new ComponentRenderer<>(TermDetailsFormLayout::new,
                TermDetailsFormLayout::setTerm);
    }
    private void configureGrid() {
        grid.addClassNames("term-grid");

        grid.setSizeFull();
        grid.setColumns("sourceTerm");
        grid.addColumn(term -> term.getSourceTerm()).setHeader("Source Language");
        grid.addColumn(term -> term.getTargetTerm()).setHeader("Target Language");

        /*grid.setItemDetailsRenderer(
                new ComponentRenderer<>(term -> {
                    VerticalLayout layout = new VerticalLayout();
                    layout.add(new Label("Source abbreviation: " +
                            term.getSourceAbbreviation() + "Target abbreviation: " +
                            term.getTargetAbbreviation()));
                    layout.add(new Label("Source context: " +
                            term.getSourceContext() + "Target context: " +
                            term.getTargetContext()));
                    return layout;
                }));*/

        grid.setItemDetailsRenderer(createTermDetailsRenderer());

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editTerm(event.getValue()));
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by term...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        projectBox.setItems(projectService.findAllProjects());
        projectBox.setItemLabelGenerator(Project::getProjectName);
        projectBox.setPlaceholder("Project...");

        projectBox.addValueChangeListener(event -> {
            // Get the selected value
            selectedValue = event.getValue().getProjectName();


        });
        projectBox.addValueChangeListener(e -> updateListByProj(selectedValue));

        Button addTermButton = new Button("Add term");
        addTermButton.addClickListener(click -> addProject());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, projectBox, addTermButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editTerm(Term term) {
        if (term == null) {
            closeEditor();
        } else {
            form.setTerm(term);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setTerm(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void resetEditor(){
        form.setTerm(null);
        grid.asSingleSelect().clear();
        editTerm(new Term());
    }
    private void addProject() {
        grid.asSingleSelect().clear();
        editTerm(new Term());
    }
    private void updateList() {
        grid.setItems(termService.findAllTerms(filterText.getValue()));
    }

    private void updateListByProj(String projName) {
        grid.setItems(termService.findAllTermsByProject(projName));
    }

}
