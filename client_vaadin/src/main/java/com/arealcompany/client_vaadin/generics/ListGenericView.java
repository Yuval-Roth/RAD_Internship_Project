package com.arealcompany.client_vaadin.generics;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.Endpoints;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListGenericView<T> extends VerticalLayout {

    private final AppController appController;
    private final Map<String, Endpoints> endpoints;
    private final Class<T> clazz;
    private final Grid<T> grid;
    private final GenericForm<T> form;
    private final List<String> displayFields;
    private Consumer<String> errorHandler;

    public ListGenericView(AppController appController, Class<T> clazz, Map<String, Endpoints> endpoints , List<String> displayFields) {
        this.appController = appController;
        this.clazz = clazz;
        this.endpoints = endpoints;
        this.displayFields = displayFields;
        form = new GenericForm<>(clazz);
        grid = new Grid<>();
        configureGrid();
        configureForm();
        add(getContent());
        closeEditor();
        updateList(); // Initially populate the grid
    }

    private void configureGrid() {

        // Dynamically set columns based on class fields and the specified display fields
        for (Iterator<String> iterator = displayFields.iterator(); iterator.hasNext(); ) {
            String fieldName = iterator.next();
            Grid.Column<T> column = grid.addColumn(item -> {
                try {
                    Field f = item.getClass().getDeclaredField(fieldName);
                    f.setAccessible(true);
                    return f.get(item);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).setHeader(fieldName).setKey(fieldName);
            if(iterator.hasNext()){
                column.setAutoWidth(true).setFlexGrow(0);
            }
            column.setSortable(true);
        }

//        // Add filters
//        HeaderRow filterRow = grid.appendHeaderRow();
//        for (Field field : clazz.getDeclaredFields()) {
//            if (displayFields.contains(field.getName())) {
//                TextField filterField = new TextField();
//                filterFields.put(field.getName(), filterField);
//                addFilterToColumn(filterRow, grid.getColumnByKey(field.getName()), filterField);
//            }
//        }

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
    }

//    private void addFilterToColumn(HeaderRow filterRow, Grid.Column<T> column, TextField filterField) {
//        if (column != null) {
//            filterField.setPlaceholder("Filter...");
//            filterField.setClearButtonVisible(true);
//            filterField.setValueChangeMode(ValueChangeMode.LAZY);
//            filterField.addValueChangeListener(e -> updateList());
//            filterRow.getCell(column).setComponent(filterField);
//        }
//    }

    //TODO: this is extremely wasteful, we can filter the list without
    // fetching all the data all the time.
    // You can see how it's done in the views classes
    private void updateList() {
        try {
            List<T> items = appController.fetchByEndpoint(endpoints.get("fetch"));
//            List<T> filteredData = items.stream()
//                    .filter(entity -> filterFields.entrySet().stream().allMatch(entry -> {
//                        String fieldName = entry.getKey();
//                        String filterValue = entry.getValue().getValue();
//                        try {
//                            Field field = entity.getClass().getDeclaredField(fieldName);
//                            field.setAccessible(true);
//                            Object value = field.get(entity);
//                            return filterValue == null || filterValue.isEmpty() ||
//                                    (value != null
//                                            && value.toString().toLowerCase().contains(filterValue.toLowerCase()));
//                        } catch (Exception e) {
//                            return true;
//                        }
//                    }))
//                    .collect(Collectors.toList());
//            grid.setItems(filteredData);
            grid.setItems(items);
        } catch (ApplicationException e) {
            handleError(e.getMessage());
        }
    }

    public void setErrorHandler(Consumer<String> handler) {
        errorHandler = handler;
    }

    public void setFilter(BiFunction<T,T,Boolean> filterFunc){
        GenericFilter<T> filter = new GenericFilter<>(grid.getListDataView(), clazz, filterFunc, displayFields.toArray(new String[0]));
        HeaderRow hr = grid.appendHeaderRow();
        for(String field : displayFields){
            hr.getCell(grid.getColumnByKey(field)).setComponent(filter.getFilterComponent(field));
        }
    }

    private void handleError(String message) {
        if(errorHandler != null) {
            errorHandler.accept(message);
        }
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form.setWidth("25em");
        form.addSaveListener(this::updateEntity);
        form.addDeleteListener(this::deleteEntity);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateEntity(GenericForm.SaveEvent<T> event) {
        try {
            appController.postByEndpoint(endpoints.get("update"), event.getEntity());
            updateList();
            closeEditor();
        } catch (ApplicationException e) {
            handleError(e.getMessage());
        }
    }

    private void deleteEntity(GenericForm.DeleteEvent<T> event) {
        try {
            appController.postByEndpoint(endpoints.get("delete"), event.getEntity());
            updateList();
            closeEditor();
        } catch (ApplicationException e) {
            handleError(e.getMessage());
        }
    }

    private void closeEditor() {
        form.setVisible(false);
    }

    private Component createFilterHeader(Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder("Filter");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }
}
