package com.example.application.views.listLanguage;
import com.example.application.data.entity.Language;
import com.example.application.data.service.LanguageService;
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
@Route(value="languages", layout = MainLayout.class)
@PageTitle("Langauges | Gloss System")
@RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
public class LanguageView extends VerticalLayout {
    Grid<Language> grid = new Grid<>(Language.class);
    TextField filterText = new TextField();
    LanguageForm form;
    LanguageService service;
    public LanguageView(LanguageService service){
        this.service = service;
        addClassName("language-view");
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
        form = new LanguageForm();
        form.setWidth("25em");
        form.addListener(LanguageForm.SaveEvent.class, this::saveLanguage);
        form.addListener(LanguageForm.DeleteEvent.class, this::deleteLanguage);
        form.addListener(LanguageForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveLanguage(LanguageForm.SaveEvent event) {
        service.saveLanguage(event.getLanguage());
        updateList();
        resetEditor();
    }
    private void deleteLanguage(LanguageForm.DeleteEvent event) {
        service.deleteLanguage(event.getLanguage());
        updateList();
        resetEditor();
    }
    private void configureGrid() {
        grid.addClassNames("language-grid");
        grid.setSizeFull();
        grid.setColumns("languageCode","languageName");
        //grid.addColumn(language -> language.getLanguage()).setHeader("Language");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editLanguage(event.getValue()));
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by language or code...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addLanguageButton = new Button("Add language");
        addLanguageButton.addClickListener(click -> addLanguage());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addLanguageButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editLanguage(Language language) {
        if (language == null) {
            closeEditor();
        } else {
            form.setLanguage(language);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setLanguage(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void resetEditor(){
        form.setLanguage(null);
        grid.asSingleSelect().clear();
        editLanguage(new Language());
    }
    private void addLanguage() {
        grid.asSingleSelect().clear();
        editLanguage(new Language());
    }
    private void updateList() {
        grid.setItems(service.findAllLanguages(filterText.getValue()));
    }
}
