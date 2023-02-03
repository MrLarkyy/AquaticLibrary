package xyz.larkyy.aquaticlibrary.database;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticlibrary.database.configuration.DatabaseConfiguration;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;
import xyz.larkyy.aquaticlibrary.reflection.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class DatabaseContext {
    private final DatabaseConfiguration cfg = new DatabaseConfiguration();
    private SQLDriver database;

    private final List<DbSet<?>> dbSets = new ArrayList<>();

    public void setup() throws Exception {
        onConfiguring(cfg);
        database = cfg.setup();
        prepareTables();
    }

    public void sql(String sql, Consumer<PreparedStatement> consumer) {
        database.sql(sql, consumer);
    }

    private void prepareTables() {
        for (var field : this.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(DbSet.class)) {
                field.setAccessible(true);
                try {
                    var o = field.get(this);
                    var dbSet = (DbSet<?>) o;

                    if (dbSet == null) {
                        continue;
                    }
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    Class<?> clazz = (Class<?>) type.getActualTypeArguments()[0];
                    dbSets.add(dbSet);

                    Table table = loadTable(dbSet,clazz,field.getName());
                    if (table == null) {
                        continue;
                    }
                    var f = dbSet.getClass().getDeclaredField("databaseContext");
                    if (f.trySetAccessible()) {
                        f.set(dbSet, this);
                    }

                    f = dbSet.getClass().getDeclaredField("genericClass");
                    if (f.trySetAccessible()) {
                        f.set(dbSet, clazz);
                    }

                    f = dbSet.getClass().getDeclaredField("table");
                    if (f.trySetAccessible()) {
                        f.set(dbSet, table);
                    }
                    //prepareTable(dbSet, clazz, field.getName());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private Table loadTable(DbSet<?> dbSet, Class<?> clazz, String tableName) {

        if (dbSet == null) {
            return null;
        }

        if (clazz.isAnnotationPresent(xyz.larkyy.aquaticlibrary.database.annotations.Table.class)) {
            var tableNameAnnotation = clazz.getAnnotation(xyz.larkyy.aquaticlibrary.database.annotations.Table.class);
            tableName = tableNameAnnotation.name();
        }

        List<Column> columns = new ArrayList<>();
        for (var field2 : clazz.getDeclaredFields()) {
            var column = loadColumn(field2);
            if (column != null) {
                columns.add(column);
            }
        }
        var table = new xyz.larkyy.aquaticlibrary.database.Table(tableName, columns);
        for (var column : columns) {
            if (column.isPrimary()) {
                ReflectionUtils.tryFieldSet(Table.class,"primaryColumn",table,column);
                break;
            }
        }
        cfg.getAdapter().addTable(table);
        return table;
    }

    private Column loadColumn(Field field) {
        if (!field.trySetAccessible()) {
            return null;
        }
        if (!field.isAnnotationPresent(xyz.larkyy.aquaticlibrary.database.annotations.Column.class)) {
            return null;
        }

        var clazz = field.getType();

        for (var f : this.getClass().getDeclaredFields()) {
            if (f.getType().isAssignableFrom(DbSet.class)) {
                f.setAccessible(true);
                try {
                    var o = f.get(this);
                    var dbSet = (DbSet<?>) o;

                    if (dbSet == null) {
                        continue;
                    }

                    for (var field2 : clazz.getDeclaredFields()) {

                    }


                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        var annotation = field.getAnnotation(xyz.larkyy.aquaticlibrary.database.annotations.Column.class);
        String name = annotation.name();
        boolean nullable = annotation.nullable();
        boolean primaryKey = annotation.primaryKey();
        String type = annotation.type();

        var column = new Column(name,field);
        column.setNullable(nullable);
        column.setType(type);
        column.setPrimary(primaryKey);
        return column;
    }

    public SQLDriver getDatabase() {
        return database;
    }

    public abstract void onConfiguring(DatabaseConfiguration configuration);
}
