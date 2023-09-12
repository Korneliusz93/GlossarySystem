package com.example.application.views.listClient;
import com.example.application.data.entity.Client;
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

public class ClientForm extends FormLayout {
    private Client client;
    Binder<Client> binder = new BeanValidationBinder<>(Client.class);
    public void setClient(Client client) {
        this.client = client;
        binder.readBean(client);
    }
    TextField clientName = new TextField("Client name");
    TextField customer = new TextField("Customer");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ClientForm() {
        addClassName("client-form");
        binder.bindInstanceFields(this);

        add(clientName, customer,
                createButtonsLayout());
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new ClientForm.DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new ClientForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }
    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new ClientForm.SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private Client client;
        protected ClientFormEvent(ClientForm source, Client client) {
            super(source, false);
            this.client = client;
        }
        public Client getClient() {
            return client;
        }
    }
    public static class SaveEvent extends ClientFormEvent {
        SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }
    public static class DeleteEvent extends ClientFormEvent {
        DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }
    public static class CloseEvent extends ClientFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
