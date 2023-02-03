package xyz.larkyy.aquaticlibrary.database;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;
import xyz.larkyy.aquaticlibrary.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbSet<T> {

    private final DatabaseContext databaseContext = null;
    private final Table table = null;
    private final Class<T> genericClass = null;

    public void insert(T item) {
        if (table == null || getDatabase() == null) {
            return;
        }

        var sql = table.getInsertSql();
        getDatabase().sql(sql, pstmt -> {
            try {
                int i = 1;
                for (var column : table.getColumns()) {
                    if (column.isPrimary()) {
                        continue;
                    }
                    pstmt.setString(i, column.getFieldValue(item).toString());
                    i++;
                }
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    return;
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        var primaryColumn = table.getPrimaryColumn();
                        if (primaryColumn != null) {
                            Object key;
                            if (primaryColumn.getField().getType().equals(int.class)) {
                                key = Math.toIntExact(generatedKeys.getLong(1));
                            } else {
                                key = generatedKeys.getLong(1);
                            }
                            ReflectionUtils.tryFieldSet(primaryColumn.getField(),item,key);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void remove(T item) {
        if (table == null || getDatabase() == null) {
            return;
        }

        if (table.getPrimaryColumn() == null) {
            return;
        }

        var sql = table.getDeleteSql();
        getDatabase().sql(sql, pstmt -> {
            try {
                var value = table.getPrimaryColumn().getFieldValue(item);
                if (value instanceof Integer i) {
                    pstmt.setInt(1, i);
                } else {
                    pstmt.setLong(1, (Long) value);
                }

                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void update(T item) {
        if (table == null || getDatabase() == null) {
            return;
        }
        var sql = table.getUpdateSql();
        Bukkit.broadcastMessage(sql);

        getDatabase().sql(sql, pstmt -> {
            try {
                int i = 1;
                for (var column : table.getColumns()) {
                    if (column.isPrimary()) {
                        continue;
                    }
                    pstmt.setObject(i,column.getFieldValue(item));
                    i++;
                }
                pstmt.setObject(i,table.getPrimaryColumn().getFieldValue(item));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     *
     * @param sqlCondition
     * Example:
     * WHERE id = '1'
     * WHERE (id = '1' OR id = '2') AND anotherColumn = 'Test'
     * @return List<T> object
     */
    public List<T> find(String sqlCondition) {
        if (table == null || getDatabase() == null || genericClass == null) {
            return new ArrayList<>();
        }

        final List<T> instances = new ArrayList<>();

        final Constructor<T> constructor;
        try {
            constructor = genericClass.getConstructor();
        } catch (NoSuchMethodException e) {
            return new ArrayList<>();
        }
        if (constructor == null) {
            return new ArrayList<>();
        }

        var sql = table.getSelectSql();
        sql += sqlCondition;
        getDatabase().sql(sql, pstmt -> {
            try {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    var object = constructor.newInstance();
                    instances.add(object);
                    for (var column : table.getColumns()) {
                        column.getField().set(object,rs.getObject(column.getName()));
                    }
                }
            } catch (SQLException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
        return instances;
    }

    public List<T> toList() {
        return find("");
    }

    public SQLDriver getDatabase() {
        if (databaseContext != null) return databaseContext.getDatabase();
        return null;
    }

}
