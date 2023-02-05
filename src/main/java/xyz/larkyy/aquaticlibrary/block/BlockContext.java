package xyz.larkyy.aquaticlibrary.block;

import xyz.larkyy.aquaticlibrary.database.configuration.DatabaseConfiguration;
import xyz.larkyy.aquaticlibrary.prop.PropContext2;
import xyz.larkyy.aquaticlibrary.prop.PropFactory;

import java.util.function.Consumer;

public class BlockContext<T extends CustomBlock> extends PropContext2<T> {
    public BlockContext(String tableName, PropFactory factory, Consumer<DatabaseConfiguration> onConfiguring) {
        super(tableName, factory, onConfiguring);
    }
}
