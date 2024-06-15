package com.arealcompany.client_vaadin.generics;

import com.vaadin.flow.component.HasValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapBinder {

    private final Map<String, HasValue<?,Object>> components;
    private Map<String,Object> map;

    public MapBinder() {
        components = new HashMap<>();
    }

    public void bind(HasValue<?,?> component, String key) {
        components.put(key, (HasValue<?, Object>) component);
    }

    public void readMap(Map<String,?> map) {
        this.map = (Map<String, Object>) map;
        components.forEach((key, component) -> component.setValue(map.get(key)));
    }

    public Map<String,?> writeAndGetMap() {
        components.forEach((key, component) -> map.put(key, component.getValue()));
        return map;
    }

    public Map<String,?> getMap() {
        return map;
    }
}
