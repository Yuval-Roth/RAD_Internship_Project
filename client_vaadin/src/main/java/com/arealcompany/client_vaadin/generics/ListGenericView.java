package com.arealcompany.client_vaadin.generics;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.Endpoints;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListGenericView<T> extends VerticalLayout {

    private final AppController appController;
    private final Class<T> clazz;
    private final Map<String, Endpoints> endpoints;
    private final Grid<T> grid = new Grid<>();
    private GenericForm<T> form;
    private String tableName;
    private List<String> displayFields;
    private Map<String, TextField> filterFields = new HashMap<>();
    private T filterEntity;

    public ListGenericView(AppController appController, Class<T> clazz, Map<String, Endpoints> endpoints , List<String> displayFields) {
        this.appController = appController;
        this.clazz = clazz;
        this.endpoints = endpoints;
        this.tableName = tableName;
        this.displayFields = displayFields;
        addClassName("repositories-view");
//        setSizeFull();
        configureGrid();
        configureForm();
        add(getContent());
        closeEditor();
        updateList(); // Initially populate the grid
        tableName = clazz.getSimpleName();
        showAllList();
    }

    private void configureGrid() {
        grid.addClassNames("generic-grid");
//        grid.setSizeFull();

        // Dynamically set columns based on class fields and the specified display
        // fields
        for (Field field : clazz.getDeclaredFields()) {
            if (displayFields.contains(field.getName())) {
                Grid.Column<T> column = grid.addColumn(item -> {
                    try {
                        Field f = item.getClass().getDeclaredField(field.getName());
                        f.setAccessible(true);
                        return f.get(item);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).setHeader(field.getName());
                column.setKey(field.getName());
            }
        }

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Add filters
        HeaderRow filterRow = grid.appendHeaderRow();
        for (Field field : clazz.getDeclaredFields()) {
            if (displayFields.contains(field.getName())) {
                TextField filterField = new TextField();
                filterFields.put(field.getName(), filterField);
                addFilterToColumn(filterRow, grid.getColumnByKey(field.getName()), filterField);
            }
        }

        // Add the edit button column
        grid.addComponentColumn(this::createEditButton).setHeader("Actions").setFlexGrow(0);
    }

    private Button createEditButton(T entity) {
        Button editButton = new Button("Edit");
        editButton.addClickListener(event -> openEditForm(entity));
        return editButton;
    }

    private void openEditForm(T entity) {
        form.setEntity(entity);
        form.setVisible(true);
        addClassName("editing");
    }

    private void addFilterToColumn(HeaderRow filterRow, Grid.Column<T> column, TextField filterField) {
        if (column != null) {
            filterField.setPlaceholder("Filter...");
            filterField.setClearButtonVisible(true);
            filterField.setValueChangeMode(ValueChangeMode.LAZY);
            filterField.addValueChangeListener(e -> updateList());
            filterRow.getCell(column).setComponent(filterField);
        }
    }

    //TODO: this is extremely wasteful, we can filter the list without
    // fetching all the data all the time.
    // You can see how it's done in the views classes
    private void updateList() {
        try {
            List<T> items = appController.fetchByEndpoint(endpoints.get("fetch"));
            List<T> filteredData = items.stream()
                    .filter(entity -> filterFields.entrySet().stream().allMatch(entry -> {
                        String fieldName = entry.getKey();
                        String filterValue = entry.getValue().getValue();
                        try {
                            Field field = entity.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object value = field.get(entity);
                            return filterValue == null || filterValue.isEmpty() ||
                                    (value != null
                                            && value.toString().toLowerCase().contains(filterValue.toLowerCase()));
                        } catch (Exception e) {
                            return true;
                        }
                    }))
                    .collect(Collectors.toList());
            grid.setItems(filteredData);
        } catch (ApplicationException e) {
            // Handle exception (e.g., show a notification)
        }
    }

    public void showAllList() {
        filterFields.values().forEach(TextField::clear);
        updateList();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new GenericForm<>(clazz);
        form.setWidth("25em");
        form.addSaveListener(event -> updateEntity(event));
        form.addDeleteListener(event -> deleteEntity(event));
        form.addCloseListener(e -> closeEditor());
    }

    private void updateEntity(GenericForm.SaveEvent<T> event) {
        try {
            appController.postByEndpoint(endpoints.get("update"), event.getEntity());
            updateList();
            closeEditor();
        } catch (ApplicationException e) {
            // Handle exception (e.g., show a notification)
        }
    }

    private void deleteEntity(GenericForm.DeleteEvent<T> event) {
        try {
            appController.postByEndpoint(endpoints.get("delete"), event.getEntity());
            updateList();
            closeEditor();
        } catch (ApplicationException e) {
            // Handle exception (e.g., show a notification)
        }
    }

    private void closeEditor() {
//        form.setEntity(null);
        form.setVisible(false);
        removeClassName("editing");
    }
}
