package xyz.larkyy.aquaticlibrary.database.driver.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;

import java.sql.*;
import java.util.function.Consumer;

public class MySQLDriver implements SQLDriver {


    private HikariConfig config;
    private HikariDataSource dataSource;
    private final String user;
    private final String password;
    private final String ip;
    private final String port;
    private final String database;
    private String poolName = "AquaticLibrary DB Pool";
    private int maximumPoolSize = 10;

    public MySQLDriver(String user, String password, String ip, String port, String database) {
        this.user = user;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.database = database;
    }

    /**
     * Setup MySQL connection to server.
     */
    public void setup() throws SQLException {
        this.config = new HikariConfig();
        // Construct configuration
        {

            config.setJdbcUrl(String.format(
                    "jdbc:mysql://%s:%s/%s", ip, port, database
            ));

            config.setMaximumPoolSize(maximumPoolSize); // a.k.a. max connections to server
            config.setPoolName(poolName);

            config.setUsername(user);
            config.setPassword(password);
        }

        // Construct data source
        {
            this.dataSource = new HikariDataSource(this.config);
        }
    }

    /**
     * @return Connection to the MySQL server.
     */
    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void sql(String sql, Consumer<PreparedStatement> consumer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                consumer.accept(ps);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
}