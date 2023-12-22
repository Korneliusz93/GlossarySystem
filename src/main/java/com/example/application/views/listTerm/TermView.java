package com.example.application.views.listTerm;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.models.ProjectFilter;
import com.example.application.data.models.TermSort;
import com.example.application.data.repository.TermRepository;
import com.example.application.data.service.ProjectService;
import com.example.application.data.service.TermService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;
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

    //EXPERIMENTAL
    @Autowired
    TermRepository termRepository;

    GridContextMenu<Term> menu = grid.addContextMenu();

    public TermView(TermService service, ProjectService projectService){
        this.termService = service;
        this.projectService = projectService;
        addClassName("term-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateListUnfiltered();
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
        updateListFiltered();
        resetEditor();
    }
    private void deleteTerm(TermForm.DeleteEvent event) {
        termService.deleteTerm(event.getTerm());
        updateListFiltered();
    }
    private void deleteTerm(Term term) {
        termService.deleteTerm(term);
        updateListFiltered();
    }
    private static ComponentRenderer<TermDetailsFormLayout, Term> createTermDetailsRenderer() {
        return new ComponentRenderer<>(TermDetailsFormLayout::new,
                TermDetailsFormLayout::setTerm);
    }
    private void configureGrid() {
        grid.addClassNames("term-grid");
        grid.setSizeFull();
        grid.setColumns("sourceTerm", "targetTerm");
        /*grid.addColumn(term -> term.getTargetTerm())
                        .setHeader("Target Language")
                                .setSortProperty("targetTerm");*/

        grid.setItemDetailsRenderer(createTermDetailsRenderer());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

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
            if (event.getValue()!=null){
                selectedValue = event.getValue().getProjectName();
                projectFilter.setProjectName(selectedValue);
            } else {
                selectedValue = null;
                projectFilter.setProjectName(null);
            }
        });
        projectBox.addValueChangeListener(e -> updateListByProj(Optional.ofNullable(selectedValue)));

    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by term...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateListSearch(Optional.ofNullable(projectFilter.getProjectName())));

        configureProjectBox();

        Button addTermButton = new Button("Add term");
        addTermButton.addClickListener(click -> addProject());

        Button clearSelectionButton = new Button(VaadinIcon.CLOSE.create());
        clearSelectionButton.addClickListener(click -> {
                    projectFilter.setProjectName("");
                    projectBox.clear();
                    filterText.setValue("");
                    updateListUnfiltered();
                }
        );

        HorizontalLayout toolbar = new HorizontalLayout(filterText, projectBox, clearSelectionButton, addTermButton);
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
        deleteTerm(term);
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
    /*                                      ORIGINAL CODE
    private void updateListUnfiltered(){

        grid.setItems(termService.findAllTerms());
    }
    private void updateListFiltered() {
        if (projectFilter.getProjectName().isEmpty()){
            updateListUnfiltered();
        } else {
            updateListByProj(Optional.ofNullable(projectFilter.getProjectName()));
        }
    }
    private void updateListSearch(Optional<String> projName){
        if(projName.isEmpty()){
            updateListUnfiltered();
        }else if (projName.isPresent()){
            List<Term> terms = termService.findAllTermsByProject(projName.get());
            List <Term> filteredTerms =
                    terms.stream()
                        .filter(term -> term.getSourceTerm().contains(filterText.getValue()))
                        .collect(Collectors.toList());
            grid.setItems(filteredTerms);
        }
    }
    private void updateListByProj(Optional<String> projName) {
        if(projName.isEmpty()){
            updateListUnfiltered();
        }else{
            grid.setItems(termService.findAllTermsByProject(projName.get()));
        }
    }
    */
    // EXPERIMENTAL
    private void updateListUnfiltered(){
        grid.setItems( query -> {
            List<TermSort> sortOrders = query.getSortOrders().stream().map(
                    querySortOrder ->
                            new TermSort(querySortOrder.getSorted(),querySortOrder.getDirection().equals(
                                    SortDirection.ASCENDING
                            ))).collect(Collectors.toList());
            for (SortOrder<String> queryOrder :
                    query.getSortOrders()) {
                TermSort sort = termService
                        .createSort(
                                //name of sorted property
                                queryOrder.getSorted(),
                                //sort direction for property
                                queryOrder.getDirection() ==
                                        SortDirection.DESCENDING);
                sortOrders.add(sort);
            }
            return termService.fetchTerms(
                    query.getPage(),
                    query.getPageSize(),
                    sortOrders
            ).stream();
        });
    }
    private void updateListFiltered() {
        if (projectFilter.getProjectName().isEmpty()) {
            updateListUnfiltered();
        } else {
            Project likeFilter = projectService.findAllProjects(projectFilter.getProjectName()).get(0);
            grid.setItems( query -> {
                List<TermSort> sortOrders = query.getSortOrders().stream().map(
                        querySortOrder ->
                                new TermSort(querySortOrder.getSorted(),querySortOrder.getDirection().equals(
                                        SortDirection.ASCENDING
                                ))).collect(Collectors.toList());
                for (SortOrder<String> queryOrder :
                        query.getSortOrders()) {
                    TermSort sort = termService
                            .createSort(
                                    //name of sorted property
                                    queryOrder.getSorted(),
                                    //sort direction for property
                                    queryOrder.getDirection() ==
                                            SortDirection.DESCENDING);
                    sortOrders.add(sort);
                }
                return termService.fetchTerms(
                        likeFilter,
                        query.getPage(),
                        query.getPageSize(),
                        sortOrders
                ).stream();
            });
        }
    }
    private void updateListSearch(Optional<String> projName){
        if(projName.isEmpty()){
            updateListUnfiltered();
        }else if (projName.isPresent()){
            Project likeFilter = projectService.findAllProjects(projName.get()).get(0);
            grid.setItems( query -> {
                List<TermSort> sortOrders = query.getSortOrders().stream().map(
                        querySortOrder ->
                                new TermSort(querySortOrder.getSorted(),querySortOrder.getDirection().equals(
                                        SortDirection.ASCENDING
                                ))).collect(Collectors.toList());
                for (SortOrder<String> queryOrder :
                        query.getSortOrders()) {
                    TermSort sort = termService
                            .createSort(
                                    //name of sorted property
                                    queryOrder.getSorted(),
                                    //sort direction for property
                                    queryOrder.getDirection() ==
                                            SortDirection.DESCENDING);
                    sortOrders.add(sort);
                }
                return termService.fetchTerms(
                        likeFilter,
                        query.getPage(),
                        query.getPageSize(),
                        sortOrders
                ).stream()
                        .filter(term -> term.getSourceTerm().contains(filterText.getValue()));
            });
        }
    }
    private void updateListByProj(Optional<String> projName) {
        if(projName.isEmpty()){
            updateListUnfiltered();
        }else{
            Project likeFilter = projectService.findAllProjects(projName.get()).get(0);
            grid.setItems( query -> {
                List<TermSort> sortOrders = query.getSortOrders().stream().map(
                        querySortOrder ->
                                new TermSort(querySortOrder.getSorted(),querySortOrder.getDirection().equals(
                                        SortDirection.ASCENDING
                                ))).collect(Collectors.toList());
                for (SortOrder<String> queryOrder :
                        query.getSortOrders()) {
                    TermSort sort = termService
                            .createSort(
                                    //name of sorted property
                                    queryOrder.getSorted(),
                                    //sort direction for property
                                    queryOrder.getDirection() ==
                                            SortDirection.DESCENDING);
                    sortOrders.add(sort);
                }
                return termService.fetchTerms(
                        likeFilter,
                        query.getPage(),
                        query.getPageSize(),
                        sortOrders
                ).stream();
            });
        }
    }
}
