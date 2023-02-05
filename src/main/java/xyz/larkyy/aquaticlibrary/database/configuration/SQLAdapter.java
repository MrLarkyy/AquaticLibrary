package xyz.larkyy.aquaticlibrary.database.configuration;

import xyz.larkyy.aquaticlibrary.database.Table;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLAdapter {

    private final List<Table> tables = new ArrayList<>();
    public abstract SQLDriver setup() throws IOException, ClassNotFoundException, SQLException;

    public SQLAdapter addTable(Table table) {
        this.tables.add(table);
        return this;
    }

    public List<Table> getTables() {
        return tables;
    }
}
