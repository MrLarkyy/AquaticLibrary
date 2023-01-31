package xyz.larkyy.aquaticlibrary.database.driver;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.Consumer;

public interface SQLDriver {

    void insert(List<Object> values);
    void remove(int id);
    void update(int id, List<Object> values);

    void sql(String sql, Consumer<PreparedStatement> consumer);

}
