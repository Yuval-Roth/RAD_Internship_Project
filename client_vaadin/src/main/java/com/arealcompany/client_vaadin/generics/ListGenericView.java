package com.arealcompany.client_vaadin.generics;

import com.arealcompany.client_vaadin.Business.AppController;
import com.arealcompany.client_vaadin.Business.Endpoints;
import com.arealcompany.client_vaadin.exceptions.ApplicationException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListGenericView<T> extends VerticalLayout {

    private final AppController appController;
    private final Map<String, Endpoints> endpoints;
    private final Grid<T> grid;
    private final GenericForm<T> form;
    private final List<String> displayFields;
    private final Map<String,Function<Object,String>> fieldConverters;
    private Consumer<String> errorHandler;

    public ListGenericView(AppController appController, Class<T> clazz, Map<String, Endpoints> endpoints , List<String> displayFields) {
        this.appController = appController;
        this.endpoints = endpoints;
        this.displayFields = displayFields;
        form = new GenericForm<>(clazz);
        grid = new Grid<>();
        fieldConverters = new HashMap<>();
        configureGrid();
        configureEditor();
        add(getContent());
        closeEditor();
        populateGrid();
    }

    public ListGenericView<T> setErrorHandler(Consumer<String> handler) {
        errorHandler = handler;
        return this;
    }

    public ListGenericView<T> setFilter(BiFunction<Function<String,String>,T,Boolean> filterFunc, List<String> filterFields){
        GenericFilter<T> filter = new GenericFilter<>(grid.getListDataView(), filterFunc);
        HeaderRow hr = grid.appendHeaderRow();
        for(String field : filterFields){
            hr.getCell(grid.getColumnByKey(field)).setComponent(filter.getFilterComponent(field));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K> ListGenericView<T> setFieldConverter(String fieldName, Function<K,String> converter){
        fieldConverters.put(fieldName,(Function<Object,String>)converter);
        return this;
    }

    private void configureGrid() {
        // Dynamically set columns based on class fields and the specified display fields
        for (Iterator<String> iterator = displayFields.iterator(); iterator.hasNext(); ) {
            String fieldName = iterator.next();
            Grid.Column<T> column = grid.addColumn(item -> {
                try {
                    Field f = item.getClass().getDeclaredField(fieldName);
                    f.setAccessible(true);
                    if(fieldConverters.containsKey(fieldName)){
                        return fieldConverters.get(fieldName).apply(f.get(item));
                    } else {
                        return f.get(item);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).setHeader(fieldName).setKey(fieldName);
            if(iterator.hasNext()){
                column.setAutoWidth(true).setFlexGrow(0).setResizable(true);
            }
            column.setSortable(true);
        }
        grid.addComponentColumn(this::createEditButton).setHeader("Actions").setKey("actions").setFlexGrow(0);
    }

    private Button createEditButton(T entity) {
        Button editButton = new Button("Edit");
        editButton.addClickListener(event -> {
            openEditForm(entity);
        });
        return editButton;
    }

    private void openEditForm(T entity) {
        form.setEntity(entity);
        form.setVisible(true);
    }

    private void handleError(String message) {
        if(errorHandler != null) {
            errorHandler.accept(message);
        }
    }

    private void populateGrid() {
        try {
            grid.setItems(appController.fetchByEndpoint(endpoints.get("fetch")));
        } catch (ApplicationException e) {
            handleError(e.getMessage());
        }
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setSizeFull();
        return content;
    }

    private void configureEditor() {
        form.setWidth("25em");
        form.addSaveListener(this::updateEntity);
        form.addDeleteListener(this::deleteEntity);
        form.addCloseListener(e -> closeEditor());
    }

    private void updateEntity(GenericForm.SaveEvent<T> event) {
        try {
            T entity = event.getEntity();
            appController.postByEndpoint(endpoints.get("update"), entity);
            refreshPage();
        } catch (ApplicationException e) {
            handleError(e.getMessage());
        }
    }

    private void deleteEntity(GenericForm.DeleteEvent<T> event) {
        handleError("Delete functionality not implemented");
//        try {
//            T entity = event.getEntity();
//            appController.postByEndpoint(endpoints.get("delete"), entity);
//            grid.getListDataView().removeItem(entity);
//            closeEditor();
//        } catch (ApplicationException e) {
//            handleError(e.getMessage());
//        }
    }

    private void refreshPage() {
        UI.getCurrent().getPage().reload();
    }

    private void closeEditor() {
        form.setEntity(null);
        form.setVisible(false);
    }

    public Grid<T> getGrid() {
        return grid;
    }
}
