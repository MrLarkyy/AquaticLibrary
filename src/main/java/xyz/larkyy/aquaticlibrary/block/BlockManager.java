package xyz.larkyy.aquaticlibrary.block;

import xyz.larkyy.aquaticlibrary.prop.PropContext;
import xyz.larkyy.aquaticlibrary.prop.PropManager;

public class BlockManager<T extends CustomBlock> extends PropManager<T> {

    public BlockManager(PropContext<T> context) {
        super(context);
    }

}
