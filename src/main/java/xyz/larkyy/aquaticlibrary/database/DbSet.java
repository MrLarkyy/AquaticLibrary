package xyz.larkyy.aquaticlibrary.database;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;
import xyz.larkyy.aquaticlibrary.reflection.ReflectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbSet<T> {

    private final DatabaseContext databaseContext = null;
    private final Table table = null;

    public void insert(T item) {
        if (table == null || getDatabase() == null) {
            return;
        }

        var sql = table.getInsertSql();
        Bukkit.broadcastMessage(sql);
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
    }

    public void update(T item) {
        if (table == null || getDatabase() == null) {
            return;
        }
    }

    public T find() {
        return null;
    }

    public List<T> toList() {
        if (table == null || getDatabase() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public SQLDriver getDatabase() {
        if (databaseContext != null) return databaseContext.getDatabase();
        return null;
    }

}
