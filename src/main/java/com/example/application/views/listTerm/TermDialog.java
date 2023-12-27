package com.example.application.views.listTerm;

import com.example.application.data.entity.Project;
import com.example.application.data.entity.Term;
import com.example.application.data.service.ProjectService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

@Route("term-dialog")
public class TermDialog extends Div {

    @Autowired
    static
    ProjectService projectService;

    private Term term;

    ComboBox<Project> projectComboBox = new ComboBox<>("Project");

    Binder<Term> binder = new BeanValidationBinder<>(Term.class);

    Dialog dialog = new Dialog();

    Button saveButton = new Button("Add", e -> dialog.close());

    public void setTerm(Term term) {
        this.term = term;
        //binder.readBean(term);
    }

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

    public TermDialog(ProjectService projectService) {

        dialog.setHeaderTitle("New term");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();

        addClassName("term-dialog");

        projectComboBox.setItems(projectService.findAllProjects());
        projectComboBox.setItemLabelGenerator(Project::getProjectName);
        binder.bind(projectComboBox,Term::getProject,Term::setProject);

        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }

    private VerticalLayout createDialogLayout() {

        VerticalLayout dialogLayout = new VerticalLayout(projectComboBox, sourceTerm, targetTerm,
                sourceSynonym, targetSynonym,
                sourceAbbreviation, targetAbbreviation,
                sourcePartOfSpeech, targetPartOfSpeech,
                sourceDefinition, targetDefinition,
                sourceContext, targetContext,
                sourceNote, targeteNote);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private Button createSaveButton(Dialog dialog) {
        //Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);

        saveButton.addClickListener(event ->{
            if(this.term == null)
            {
                this.term = new Term();
            } else {
                binder.readBean(this.term);
            }
            binder.bindInstanceFields(this);
            validateAndSave();
        });
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return saveButton;
    }

    private void validateAndSave() {
        try {
            binder.writeBean(term);
            fireEvent(new TermDialog.SaveEvent(this, term));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class TermDialogEvent extends ComponentEvent<TermDialog> {
        private Term term;
        protected TermDialogEvent(TermDialog source, Term term) {
            super(source, false);
            this.term = term;
        }
        public Term getTerm() {
            return term;
        }
    }
    public static class SaveEvent extends TermDialog.TermDialogEvent {
        SaveEvent(TermDialog source, Term term) {
            super(source, term);
        }
    }
    public static class CloseEvent extends TermDialog.TermDialogEvent {
        CloseEvent(TermDialog source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
