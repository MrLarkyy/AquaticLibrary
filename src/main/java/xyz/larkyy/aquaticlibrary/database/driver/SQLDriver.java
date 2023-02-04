package xyz.larkyy.aquaticlibrary.database.driver;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface SQLDriver {

    void sql(String sql, Consumer<PreparedStatement> consumer);

}
