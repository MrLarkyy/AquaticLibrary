package xyz.larkyy.aquaticlibrary.database.configuration.adapters;

import xyz.larkyy.aquaticlibrary.database.configuration.SQLAdapter;
import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;
import xyz.larkyy.aquaticlibrary.database.driver.impl.MySQLDriver;
import java.sql.SQLException;

public class MySQLAdapter extends SQLAdapter {

    private final String user;
    private final String password;
    private final String ip;
    private final String port;
    private final String database;

    public MySQLAdapter(String user, String password, String ip, String port, String database) {
        this.user = user;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.database = database;
    }

    @Override
    public SQLDriver setup() throws SQLException {
        var driver = new MySQLDriver(user,password,ip,port,database);
        driver.setup();
        return driver;
    }
}
