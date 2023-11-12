package com.example.application.views.listLanguage;
import com.example.application.data.entity.Language;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class LanguageForm extends FormLayout {
    private Language language;
    Binder<Language> binder = new BeanValidationBinder<>(Language.class);
    public void setLanguage(Language language) {
        this.language = language;
        binder.readBean(language);
    }
    TextField languageCode = new TextField("Language code");
    TextField languageName = new TextField("Language");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public LanguageForm() {
        addClassName("language-form");
        binder.bindInstanceFields(this);

        add(languageCode, languageName,
                createButtonsLayout());
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new LanguageForm.DeleteEvent(this, language)));
        close.addClickListener(event -> fireEvent(new LanguageForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            binder.writeBean(language);
            fireEvent(new LanguageForm.SaveEvent(this, language));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class LanguageFormEvent extends ComponentEvent<LanguageForm> {
        private Language language;
        protected LanguageFormEvent(LanguageForm source, Language language) {
            super(source, false);
            this.language = language;
        }
        public Language getLanguage() {
            return language;
        }
    }
    public static class SaveEvent extends LanguageFormEvent {
        SaveEvent(LanguageForm source, Language language) {
            super(source, language);
        }
    }
    public static class DeleteEvent extends LanguageFormEvent {
        DeleteEvent(LanguageForm source, Language language) {
            super(source, language);
        }
    }
    public static class CloseEvent extends LanguageFormEvent {
        CloseEvent(LanguageForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
