package xyz.larkyy.aquaticlibrary.database.configuration;

import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;

import java.io.IOException;

public class DatabaseConfiguration {

    private SQLAdapter adapter = new SQLAdapter() {
        @Override
        public SQLDriver setup() throws IOException, ClassNotFoundException {
            return null;
        }
    };

    public void setAdapter(SQLAdapter adapter) {
        this.adapter = adapter;
    }

    public SQLAdapter getAdapter() {
        return adapter;
    }

    public SQLDriver setup() throws Exception {
        if (adapter == null) {
            throw new Exception("No SQL Adapter configured!");
        }
        return adapter.setup();
    }
}
