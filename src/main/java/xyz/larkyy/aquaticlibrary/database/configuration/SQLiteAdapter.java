package xyz.larkyy.aquaticlibrary.database.configuration;

import xyz.larkyy.aquaticlibrary.database.driver.SQLDriver;
import xyz.larkyy.aquaticlibrary.database.driver.impl.SQLite;

import java.io.File;
import java.io.IOException;

public class SQLiteAdapter extends SQLAdapter {

    private final String fileName;
    private final File folder;

    public SQLiteAdapter(File folder, String fileName) {
        this.folder = folder;
        if (fileName.substring(fileName.length() - 3).equalsIgnoreCase(".db")) {
            this.fileName = fileName;
        } else {
            this.fileName = fileName+".db";
        }
    }

    @Override
    public SQLDriver setup() throws IOException, ClassNotFoundException {
        return new SQLite(folder,fileName);
    }
}
