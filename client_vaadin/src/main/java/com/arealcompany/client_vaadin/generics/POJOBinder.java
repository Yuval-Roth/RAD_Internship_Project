package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class POJOBinder<T> {

    private final Class<T> clazz;
    private final Map<String, Field> fields;
    private final Map<String, HasValue<?,String>> components;

    public POJOBinder(Class<T> clazz) {
        this.clazz = clazz;
        fields = new HashMap<>();
        components = new HashMap<>();
    }

    public void bind(HasValue<?,String> component, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            fields.put(fieldName, field);
            components.put(fieldName, component);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + fieldName + " not found in class " + clazz.getName());
        }
    }

    /**
     * Read the fields from the object and set the values to the components using {@link Objects#toString()}
     */
    public void readObject(T object) {
        fields.forEach((fieldName, field) -> {
            try {
                Object value = field.get(object);
                components.get(fieldName).setValue(Objects.toString(value, ""));
            } catch (IllegalAccessException ignored) {}
        });
    }

    /**
     * Create a new object of the class and set the values from the components to the fields
     * @throws IllegalArgumentException if the class does not have a no-args constructor
     * @throws RuntimeException if the constructor throws an exception, if the constructor is not accessible or if the class is abstract
     */
    public T writeObject() {
        T object;
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " does not have a no-args constructor");
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        writeFieldsToObject(object);
        return object;
    }

    /**
     * Update the object with the values from the components in-place
     */
    public void updateObject(T object) {
        writeFieldsToObject(object);
    }

    private void writeFieldsToObject(T object) {
        fields.forEach((fieldName, field) -> {
            HasValue<?,String> component = components.get(fieldName);
            Object value = component.getValue();
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
