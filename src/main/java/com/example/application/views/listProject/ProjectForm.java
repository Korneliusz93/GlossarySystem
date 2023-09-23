package com.example.application.views.listProject;
import com.example.application.data.entity.Category;
import com.example.application.data.entity.Client;
import com.example.application.data.entity.Language;
import com.example.application.data.entity.Project;
import com.example.application.data.service.CategoryService;
import com.example.application.data.service.ClientService;
import com.example.application.data.service.LanguageService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class ProjectForm extends FormLayout {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ClientService clientService;
    private Project project;
    Binder<Project> binder = new BeanValidationBinder<>(Project.class);

    public void setProject(Project project) {
        this.project = project;
        binder.readBean(project);
    }

    TextField projectName = new TextField("Project name");
    ComboBox <Category> categoryComboBox = new ComboBox<>("Category");
    ComboBox <Client> clientComboBox = new ComboBox<>("Client");
    ComboBox <Language> srcLanguageComboBox = new ComboBox<>("Source Language");
    ComboBox <Language> trgLanguageComboBox = new ComboBox<>("Source Language");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    @Autowired
    public ProjectForm(CategoryService categoryService, LanguageService languageService,
                       ClientService clientService){
        addClassName("project-from");
        binder.bindInstanceFields(this);

        categoryComboBox.setItems(categoryService.findAllCategories());
        categoryComboBox.setItemLabelGenerator(Category::getSubCategoryName);
        binder.bind(categoryComboBox,Project::getCategory,Project::setCategory);

        clientComboBox.setItems(clientService.findAllClients());
        clientComboBox.setItemLabelGenerator(Client::getClientName);
        binder.bind(clientComboBox,Project::getClient,Project::setClient);

        srcLanguageComboBox.setItems(languageService.findAllLanguages());
        srcLanguageComboBox.setItemLabelGenerator(Language::getLanguageName);
        binder.bind(srcLanguageComboBox,Project::getSourceLanguage, Project::setSourceLanguage);

        trgLanguageComboBox.setItems(languageService.findAllLanguages());
        trgLanguageComboBox.setItemLabelGenerator(Language::getLanguageName);
        binder.bind(trgLanguageComboBox,Project::getTargetLanguage,Project::setTargetLanguage);

        add(projectName, categoryComboBox, clientComboBox, srcLanguageComboBox, trgLanguageComboBox,
            createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new ProjectForm.DeleteEvent(this, project)));
        close.addClickListener(event -> fireEvent(new ProjectForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            binder.writeBean(project);
            fireEvent(new ProjectForm.SaveEvent(this, project));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class ProjectFormEvent extends ComponentEvent<ProjectForm> {
        private Project project;
        protected ProjectFormEvent(ProjectForm source, Project project) {
            super(source, false);
            this.project = project;
        }
        public Project getProject() {
            return project;
        }
    }
    public static class SaveEvent extends ProjectForm.ProjectFormEvent {
        SaveEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }
    public static class DeleteEvent extends ProjectForm.ProjectFormEvent {
        DeleteEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }
    public static class CloseEvent extends ProjectForm.ProjectFormEvent {
        CloseEvent(ProjectForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
