package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class POJOBinder<T> {

    private final Class<T> clazz;
    private final Map<String, Field> fields;
    private final Map<String, HasValue<?,Object>> components;
    private T object;

    public POJOBinder(Class<T> clazz) {
        this.clazz = clazz;
        fields = new HashMap<>();
        components = new HashMap<>();
    }

    public void bind(HasValue<?,?> component, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            fields.put(fieldName, field);
            components.put(fieldName, (HasValue<?, Object>) component);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " not found in class " + clazz.getName());
        }
    }

    /**
     * Read the fields from the object and set the values to the components using {@link Objects#toString()}
     */
    public void readObject(T object) {
        this.object = object;
        fields.forEach((fieldName, field) -> {
            try {
                Object value = field.get(object);
                components.get(fieldName).setValue(Objects.toString( value, ""));
            } catch (IllegalAccessException ignored) {}
        });
    }

    /**
     * Write the values from the components to the object
     */
    public T writeAndGetObject() {
        fields.forEach((fieldName, field) -> {
            HasValue<?,Object> component = components.get(fieldName);
            Object value = component.getValue();
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return object;
    }

    public T getObject() {
        return object;
    }
}
