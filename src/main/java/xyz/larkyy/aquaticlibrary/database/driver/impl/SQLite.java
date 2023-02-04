package xyz.larkyy.aquaticlibrary.database.driver.impl;

import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SQLite implements SQLDriver {

    private Connection activeConnection;
    private final File databaseFile;

    public SQLite(File folder, String fileName) throws ClassNotFoundException, IOException {
        folder.mkdirs();
        Class.forName("org.sqlite.JDBC");
        databaseFile = new File(folder,fileName);
        databaseFile.createNewFile();
        getConnection();
    }

    @Override
    public void sql(String sql, Consumer<PreparedStatement> consumer) {
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            consumer.accept(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        try {
            if (activeConnection != null && !activeConnection.isClosed()) return this.activeConnection;
            Class.forName("org.sqlite.SQLiteDataSource");
            this.activeConnection = DriverManager.getConnection("jdbc:sqlite:"+this.databaseFile.getPath());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this.activeConnection;
    }
}
