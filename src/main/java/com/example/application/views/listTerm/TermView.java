package com.example.application.views.listTerm;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.models.ProjectFilter;
import com.example.application.data.service.ProjectService;
import com.example.application.data.service.TermService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@PermitAll
@Route(value="terms", layout = MainLayout.class)
@PageTitle("Terms | Gloss system")
@RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER", "ROLE_GUEST" })
public class TermView extends VerticalLayout {
    Grid<Term> grid = new Grid<>(Term.class);
    TextField filterText = new TextField();
    ComboBox<Project> projectBox = new ComboBox<Project>();
    @Autowired
    ProjectFilter projectFilter;
    TermForm form;
    String selectedValue;
    TermService termService;
    ProjectService projectService;

    GridContextMenu<Term> menu = grid.addContextMenu();


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
        //updateList();
        updateListFiltered();
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
        //grid.addColumn(term -> term.getSourceTerm()).setHeader("Source Language");
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
        //grid.asSingleSelect().addValueChangeListener(event ->
                //editTerm(event.getValue()));

        menu.addItem("Edit", event -> {
            editTerm(event.getItem().get());
        });
        menu.addItem("Delete", event -> {
            deleteTermContext(event.getItem().get());
        });
    }

    private void configureProjectBox(){
        projectBox.setItems(projectService.findAllProjects());
        projectBox.setItemLabelGenerator(Project::getProjectName);
        projectBox.setPlaceholder("Project...");
        //projectBox.setValue(null);

        projectBox.addValueChangeListener(event -> {
            // Get the selected value
            if (event.getValue()==null)
            {
                projectFilter.setProjectName("empty");
                selectedValue = "empty";
            } else {
                selectedValue = event.getValue().getProjectName();
            }
                projectFilter.setProjectName(selectedValue);
        });
        projectBox.addValueChangeListener(e -> updateListByProj(selectedValue));

    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by term...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateListSearch(projectFilter.getProjectName()));

        configureProjectBox();

        Button addTermButton = new Button("Add term");
        addTermButton.addClickListener(click -> addProject());

        Button clearSelectionButton = new Button("Clear selection");
        clearSelectionButton.addClickListener(click -> {
                    projectBox.setValue(null);
                    filterText.setValue("");
                }
        );

        HorizontalLayout toolbar = new HorizontalLayout(filterText, projectBox, addTermButton, clearSelectionButton);
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
    private void deleteTermContext(Term term) {
        termService.deleteTerm(term);
        updateList();
        //resetEditor();
    }
    public void viewTerm(Term term){
        //termDetailsFormLayout.setTerm(term);
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
    private void updateListFiltered() {
        if (projectFilter.getProjectName()==null || projectFilter.getProjectName().isEmpty()){
            updateList();
        } else {
            updateListByProj(projectFilter.getProjectName());
        }
    }

    private void updateListSearch(String projName){
        if(projName.equals("empty")){
            grid.setItems(termService.findAllTerms());
        }else{
            List<Term> terms = termService.findAllTermsByProject(projName);
            List <Term> filteredTerms =
                    terms.stream()
                        .filter(term -> term.getSourceTerm().contains(filterText.getValue()))
                        .collect(Collectors.toList());
            grid.setItems(filteredTerms);
        }
    }

    private void updateListByProj(String projName) {
        if(projName.equals("empty")){
            grid.setItems(termService.findAllTerms());
        }else{
            grid.setItems(termService.findAllTermsByProject(projName));
        }
    }
}
