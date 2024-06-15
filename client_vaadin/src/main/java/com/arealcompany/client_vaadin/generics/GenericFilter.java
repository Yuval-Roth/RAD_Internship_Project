package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class GenericFilter<T> {

    private final GridListDataView<T> dataView;
    private final Class<T> clazz;
    private final BiFunction<T,T,Boolean> filter;
    private final Map<String, Field> fields;
    private final T entity;

    public GenericFilter(GridListDataView<T> dataView, Class<T> clazz, BiFunction<T,T,Boolean> filter, String ... fields){
        this.dataView = dataView;
        this.clazz = clazz;
        this.filter = filter;
        this.fields = new HashMap<>();
        try {
            entity = clazz.getConstructor().newInstance();
            for(String fieldName : fields){
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                this.fields.put(fieldName,field);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor found for class " + clazz.getName());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Could not instantiate class " + clazz.getName(),e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find field in class " + clazz.getName(),e);
        }
        dataView.setFilter(this::filter);
    }

    public Component getFilterComponent(String fieldName){
        if(fields.get(fieldName) == null){
            throw new RuntimeException("Field " + fieldName + " not found in class " + clazz.getName());
        }
        return createFilterComponent(value -> updateField(fieldName,value));
    }

    private boolean filter(T otherEntity){
        return filter.apply(this.entity,otherEntity);
    }

    private void updateField(String fieldName, Object value){
        Field field = fields.get(fieldName);
        if(field == null){
            throw new RuntimeException("Field " + fieldName + " not found in class " + clazz.getName());
        }
        try {
            assert value instanceof String;
            String str = (String) value;
            if(field.getType() == Integer.class){
                try{
                    value = Integer.parseInt(str);
                } catch (NumberFormatException e){
                    value = null;
                }
            } else if(field.getType() == Boolean.class){
                value = value.equals("true") ? Boolean.TRUE : value.equals("false") ? Boolean.FALSE : null;
            }
            field.set(entity,value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not set field " + fieldName + " in class " + clazz.getName(),e);
        }
        dataView.refreshAll();
    }

    private Component createFilterComponent(Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder("Filter");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }
}
