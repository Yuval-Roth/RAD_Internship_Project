package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericFilter<T> {

    private final GridListDataView<T> dataView;
    private final BiFunction<Function<String,String>,T,Boolean> filter;
    private final Map<String,String> fieldsToValues;

    public GenericFilter(GridListDataView<T> dataView, BiFunction<Function<String,String>,T,Boolean> filter){
        this.dataView = dataView;
        this.filter = filter;
        fieldsToValues = new HashMap<>();
        dataView.setFilter(this::filter);
    }

    public Component getFilterComponent(String fieldName){
        return createFilterComponent(value -> updateField(fieldName,value));
    }

    private boolean filter(T otherEntity){
        return filter.apply(fieldsToValues::get,otherEntity);
    }

    private void updateField(String fieldName, String value){
        if(value == null || value.isBlank()){
            fieldsToValues.remove(fieldName);
        } else {
            fieldsToValues.put(fieldName, value);
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
