package xyz.larkyy.aquaticlibrary.block;

import xyz.larkyy.aquaticlibrary.prop.PropContext;
import xyz.larkyy.aquaticlibrary.prop.PropManager;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

public class BlockManager<T extends CustomBlock> extends PropManager<T> {

    public BlockManager(PropContext<T> context) {
        super(context);
    }

    public static <T extends CustomBlock> BlockManager<T> init(Class<T> blockClass, PropContext<T> context) {
        try {
            context.setup();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var manager = new BlockManager<>(context);
        ServiceManager.addGenericService(blockClass,manager);
        return manager;
    }

}
