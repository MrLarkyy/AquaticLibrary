package xyz.larkyy.aquaticlibrary.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Column {

    private String type = "varchar(2000)";
    private boolean nullable = true;
    private final String name;
    private boolean primary = false;

    private final Field field;

    private final List<String> additionals = new ArrayList<>();


    public List<String> getAdditionals() {
        return additionals;
    }

    public void addAdditional(String additional) {
        additionals.add(additional);
    }

    public Column(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Field getField() {
        return field;
    }

    public Object getFieldValue(Object instance) {
        if (!field.trySetAccessible()) {
            return null;
        }
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
