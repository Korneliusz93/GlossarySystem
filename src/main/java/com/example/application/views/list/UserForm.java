package com.example.application.views.list;

import com.example.application.data.entity.User;
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

public class UserForm extends FormLayout {
    private User user;
    Binder<User> binder = new BeanValidationBinder<>(User.class);
    public void setUser(User user) {
        this.user = user;
        binder.readBean(user);
    }

    TextField userName = new TextField("User name");
    TextField password = new TextField("Password");
    TextField email = new TextField("E-mail");
    TextField fullName = new TextField("Full Name");
    ComboBox<String> roleComboBox = new ComboBox<>("Role");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    public UserForm() {
        addClassName("user-form");
        binder.bindInstanceFields(this);

        roleComboBox.setItems("ADMIN","STAFF","USER","GUEST");
        binder.bind(roleComboBox, User::getRole, User::setRole);

        add(userName, password, email, fullName, roleComboBox,
                createButtonsLayout());
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new UserForm.DeleteEvent(this, user)));
        close.addClickListener(event -> fireEvent(new UserForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            binder.writeBean(user);
            fireEvent(new UserForm.SaveEvent(this, user));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;
        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }
        public User getUser() {
            return user;
        }
    }
    public static class SaveEvent extends UserForm.UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }
    public static class DeleteEvent extends UserForm.UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }
    }
    public static class CloseEvent extends UserForm.UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
