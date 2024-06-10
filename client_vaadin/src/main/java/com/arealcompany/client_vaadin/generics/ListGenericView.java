package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListGenericView<T> extends VerticalLayout {

    // TODO: We need to remove the mongo repository and call the backend when we are
    // changing data

    private MongoRepository<T, String> repository;
    private Grid<T> grid = new Grid<>();
    private GenericForm<T> form;
    private String tableName;

    private Map<String, TextField> filterFields = new HashMap<>();
    private List<String> displayFields;

    public ListGenericView(MongoRepository<T, String> repository, Class<T> clazz, String tableName,
            List<String> displayFields) {
        this.repository = repository;
        this.tableName = tableName;
        this.displayFields = displayFields;
        addClassName("repositories-view");
        setSizeFull();
        configureGrid(clazz);
        configureForm(clazz);
        add(createTitleLayout(), getContent());
        closeEditor();
        // updateList(); // Initially populate the grid
    }

    private void configureGrid(Class<T> clazz) {
        grid.addClassNames("generic-grid");
        grid.setSizeFull();

        // Dynamically set columns based on class fields and the specified display
        // fields
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals("id") && displayFields.contains(field.getName())) {
                Grid.Column<T> column = grid.addColumn(item -> {
                    try {
                        Field f = item.getClass().getDeclaredField(field.getName());
                        f.setAccessible(true);
                        return f.get(item);
                    } catch (Exception e) {
                        return null;
                    }
                }).setHeader(field.getName());
                column.setKey(field.getName());
            }
        }

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Add filters
        HeaderRow filterRow = grid.appendHeaderRow();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals("id") && displayFields.contains(field.getName())) {
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

    private void updateList() {
        List<T> filteredData = repository.findAll().stream()
                .filter(entity -> filterFields.entrySet().stream().allMatch(entry -> {
                    String fieldName = entry.getKey();
                    String filterValue = entry.getValue().getValue();
                    try {
                        Field field = entity.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        return filterValue == null || filterValue.isEmpty() ||
                                (value != null && value.toString().toLowerCase().contains(filterValue.toLowerCase()));
                    } catch (Exception e) {
                        return true;
                    }
                }))
                .collect(Collectors.toList());
        grid.setItems(filteredData);
    }

    private HorizontalLayout createTitleLayout() {
        Button title1 = new Button(tableName, event -> showAllList());
        title1.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.MEDIUM);
        title1.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        HorizontalLayout titlesLayout = new HorizontalLayout(title1);
        titlesLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return titlesLayout;
    }

    private void showAllList() {
        filterFields.values().forEach(TextField::clear);
        grid.setItems(repository.findAll());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm(Class<T> clazz) {
        form = new GenericForm<>(clazz);
        form.setWidth("25em");
        form.addSaveListener(event -> saveEntity(event));
        form.addDeleteListener(event -> deleteEntity(event));
        form.addCloseListener(e -> closeEditor());
    }

    private void saveEntity(GenericForm.SaveEvent<T> event) {
        repository.save(event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteEntity(GenericForm.DeleteEvent<T> event) {
        repository.delete(event.getEntity());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setEntity(null);
        form.setVisible(false);
        removeClassName("editing");
    }
}
