package com.arealcompany.client_vaadin.generics;

import ch.qos.logback.core.spi.ContextAware;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class GenericForm<T> extends FormLayout {

    private POJOBinder<T> binder;
    private Class<T> entityClass;
    private Map<String, Component> fieldComponents = new HashMap<>();

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    public GenericForm(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.binder = new POJOBinder<>(entityClass);
        addClassName("generic-form");
        createFields();
        add(createButtonsLayout());
    }

    private void createFields() {
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                field.setAccessible(false);
            } else {
                Component fieldComponent = createFieldComponent(field);
                if (fieldComponent != null) {
                    fieldComponents.put(field.getName(), fieldComponent);
                    add(fieldComponent);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Component createFieldComponent(Field field) {
        try {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            Component component = null;

            if (fieldType == Integer.class || fieldType == int.class) {
                component = new IntegerField(field.getName());
                binder.bind((HasValue<?, Integer>) component, field.getName());
            }

            else if (fieldType == String.class) {
                TextField comp = new TextField(field.getName());
                String name = field.getName();
                binder.bind(comp, name);
                component = comp;
            }

            else if (fieldType == Double.class || fieldType == double.class ||
                    fieldType == Float.class || fieldType == float.class) {
                component = new TextField(field.getName());
                binder.bind((HasValue<?, Double>) component, field.getName());
            }

            else if (fieldType == Boolean.class || fieldType == boolean.class) {
                component = new Checkbox(field.getName());
                binder.bind((HasValue<?, Boolean>) component, field.getName());
            }

            else if (fieldType == Date.class) {
                component = new DatePicker(field.getName());
                binder.bind((HasValue<?, LocalDate>) component, field.getName());
            }
            // Add more types as needed

            field.setAccessible(false);
            return component;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent<>(this, binder.getObject())));
        close.addClickListener(event -> fireEvent(new CloseEvent<>(this)));

//        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
//        if (binder.isValid()) {
            fireEvent(new SaveEvent<>(this, binder.writeAndGetObject()));
//        }
    }

    public void setEntity(T entity) {
        binder.readObject(entity);
    }

    public static abstract class GenericFormEvent<T> extends ComponentEvent<GenericForm<T>> {
        private T entity;

        protected GenericFormEvent(GenericForm<T> source, T entity) {
            super(source, false);
            this.entity = entity;
        }

        public T getEntity() {
            return entity;
        }
    }

    public static class SaveEvent<T> extends GenericFormEvent<T> {
        SaveEvent(GenericForm<T> source, T entity) {
            super(source, entity);
        }
    }

    public static class DeleteEvent<T> extends GenericFormEvent<T> {
        DeleteEvent(GenericForm<T> source, T entity) {
            super(source, entity);
        }
    }

    public static class CloseEvent<T> extends GenericFormEvent<T> {
        CloseEvent(GenericForm<T> source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent<T>> listener) {
        return addListener(DeleteEvent.class, (ComponentEventListener) listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent<T>> listener) {
        return addListener(SaveEvent.class, (ComponentEventListener) listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent<T>> listener) {
        return addListener(CloseEvent.class, (ComponentEventListener) listener);
    }
}
