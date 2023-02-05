package xyz.larkyy.aquaticlibrary.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import xyz.larkyy.aquaticlibrary.database.configuration.SQLAdapter;
import xyz.larkyy.aquaticlibrary.prop.PropContext2;
import xyz.larkyy.aquaticlibrary.prop.PropFactory;
import xyz.larkyy.aquaticlibrary.prop.PropManager2;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

import java.util.HashMap;
import java.util.Map;

public class BlockManager2 {

    private final Map<Location, CustomBlock> customBlocks = new HashMap<>();
    private final Map<Class<? extends CustomBlock>,BlockContext<? extends CustomBlock>> contexts = new HashMap<>();
    private final SQLAdapter sqlAdapter;

    public BlockManager2(SQLAdapter sqlAdapter) {
        this.sqlAdapter = sqlAdapter;
    }

    public CustomBlock getCustomBlock(Location location) {
        location = location.getBlock().getLocation();
        return customBlocks.get(location);
    }

    public CustomBlock getCustomBlock(Block block) {
        var location = block.getLocation();
        return getCustomBlock(location);
    }

    private PropManager2 getManager() {
        return ServiceManager.getService(PropManager2.class);
    }

    public <T extends CustomBlock> void registerBlock(T customBlock) {
        var context = getManager().getContext(customBlock.getClass());
        if (context == null) {
            return;
        }
        getManager().addProp(customBlock);
        context.addProp(customBlock);
    }
    public <T extends CustomBlock> void addFactory(Class<T> blockClass, PropFactory factory) throws Exception {
        if (contexts.containsKey(blockClass)) {
            return;
        }
        BlockContext<T> context = new BlockContext<>("AquaticLibrary_blocks_"+blockClass.getSimpleName(),factory, cfg -> {
            cfg.setAdapter(sqlAdapter);
        });
        context.setup();
        getManager().addContext(blockClass,context);
        contexts.put(blockClass,context);
    }

    public <T extends CustomBlock> void unregisterBlock(T customBlock) {
        var context = getManager().getContext(customBlock.getClass());
        if (context == null) {
            return;
        }
        getManager().removeProp(customBlock);
        context.deleteProp(customBlock);
    }

    public <T extends CustomBlock> void addCustomBlock(T customBlock) {
        customBlocks.put(customBlock.getBlock().getLocation(),customBlock);
        if (contexts.containsKey(customBlock.getClass())) {
            getManager().addProp(customBlock);
        }
    }

    public <T extends CustomBlock> void removeCustomBlock(T customBlock) {
        customBlocks.remove(customBlock.getBlock().getLocation());
        if (contexts.containsKey(customBlock.getClass())) {
            getManager().removeProp(customBlock);
        }
    }
}
