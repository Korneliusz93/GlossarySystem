package com.example.application.views.listTerm;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.service.ProjectService;
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
public class TermForm extends FormLayout {

    @Autowired
    ProjectService projectService;

    private Term term;

    Binder<Term> binder = new BeanValidationBinder<>(Term.class);

    public void setTerm(Term term) {
        this.term = term;
        binder.readBean(term);
    }

    ComboBox<Project> projectComboBox = new ComboBox<>("Project");

    TextField sourceTerm = new TextField("Source Term");
    TextField sourceSynonym = new TextField("Source Synonym");
    TextField sourceAbbreviation = new TextField("Source Abbreviation");
    TextField sourcePartOfSpeech = new TextField("Source Part of speech");
    TextField sourceDefinition = new TextField("Source Definition");
    TextField sourceContext = new TextField("Source Context");
    TextField sourceNote = new TextField("Source Note");

    TextField targetTerm = new TextField("Target Term");
    TextField targetSynonym = new TextField("Target Synonym");
    TextField targetAbbreviation = new TextField("Target Abbreviation");
    TextField targetPartOfSpeech = new TextField("Target Part of speech");
    TextField targetDefinition = new TextField("Target Definition");
    TextField targetContext = new TextField("Target Context");
    TextField targeteNote = new TextField("Target Note");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    @Autowired
    public TermForm(ProjectService projectService){
        addClassName("term-from");
        binder.bindInstanceFields(this);

        projectComboBox.setItems(projectService.findAllProjects());
        projectComboBox.setItemLabelGenerator(Project::getProjectName);
        binder.bind(projectComboBox,Term::getProject,Term::setProject);

        add(projectComboBox, sourceTerm, sourceSynonym, sourceAbbreviation, sourcePartOfSpeech, sourceDefinition, sourceContext, sourceNote,
                targetTerm, targetSynonym, targetAbbreviation, targetPartOfSpeech, targetDefinition, targetContext, targeteNote,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new TermForm.DeleteEvent(this, term)));
        close.addClickListener(event -> fireEvent(new TermForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            binder.writeBean(term);
            fireEvent(new TermForm.SaveEvent(this, term));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class TermFormEvent extends ComponentEvent<TermForm> {
        private Term term;
        protected TermFormEvent(TermForm source, Term term) {
            super(source, false);
            this.term = term;
        }
        public Term getTerm() {
            return term;
        }
    }
    public static class SaveEvent extends TermForm.TermFormEvent {
        SaveEvent(TermForm source, Term term) {
            super(source, term);
        }
    }
    public static class DeleteEvent extends TermForm.TermFormEvent {
        DeleteEvent(TermForm source, Term term) {
            super(source, term);
        }
    }
    public static class CloseEvent extends TermForm.TermFormEvent {
        CloseEvent(TermForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
