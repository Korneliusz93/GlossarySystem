package com.example.application.views.listClient;
import com.example.application.data.entity.Client;
import com.example.application.data.service.ClientService;
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

@PermitAll
@Route(value="clients", layout = MainLayout.class)
@PageTitle("Clients | Gloss System")
public class ClientView extends VerticalLayout {
    Grid<Client> grid = new Grid<>(Client.class);
    TextField filterText = new TextField();
    ClientForm form;
    ClientService service;
    public ClientView(ClientService service) {
        this.service = service;
        addClassName("client-view");
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
        form = new ClientForm();
        form.setWidth("25em");
        form.addListener(ClientForm.SaveEvent.class, this::saveClient);
        form.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveClient(ClientForm.SaveEvent event) {
        service.saveClient(event.getClient());
        updateList();
        resetEditor();
    }
    private void deleteClient(ClientForm.DeleteEvent event) {
        service.deleteClient(event.getClient());
        updateList();
        resetEditor();
    }
    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setColumns("clientName","customer");
        //grid.addColumn(client -> client.getCustomer()).setHeader("Customer");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editClient(event.getValue()));
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by customer...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addCategoryButton = new Button("Add client");
        addCategoryButton.addClickListener(click -> addClient());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCategoryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void editClient(Client client) {
        if (client == null) {
            closeEditor();
        } else {
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        form.setClient(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void resetEditor(){
        form.setClient(null);
        grid.asSingleSelect().clear();
        editClient(new Client());
    }
    private void addClient() {
        grid.asSingleSelect().clear();
        editClient(new Client());
    }
    private void updateList() {
        grid.setItems(service.findAllClients(filterText.getValue()));
    }
}
