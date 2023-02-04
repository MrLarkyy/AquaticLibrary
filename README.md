<h1 align="center">AquaticLibrary</h1>
<p align="center">Make your coding easier!</p>

## Features

- Custom Blocks (Persistent and Non-Persistent)
- Database Handling
- Event Handling
- Service Manager

### Custom Blocks
Usage:
```java
AquaticLibrary.init(pluginInstance);
/*
    Context is a table in your database. It handles inserting, updating, deleting and selecting.
    1st parameter - Name of a table in your database that was made for your block.
    2nd paramater - Function that basically makes an instance of your block.
    3rd parameter - "onConfiguring" consumer, adapter is required to make this work.
*/
PropContext<TestBlock> context = new PropContext<>("aquatic_testblocks", (entry) -> {
    var location = entry.getKey();
    var data = entry.getValue();
    return new TestBlock(location, data);
}, cfg -> cfg.setAdapter(new SQLiteAdapter(AquaticLibrary.getPlugin().getDataFolder(), "database")));

var manager = BlockManager.init(TestBlock.class, context);
manager.loadProps(); // Loads all props from the database
```
TestBlock is a custom object that extends the CustomBlock class. When is a new instance of CustomBlock created, it is automatically cached. If you want to save the Block into database, you must use the BlockManager#registerProp(Prop) method.

Creating a custom block:
```java
var block = new TestBlock(e.getPlayer().getLocation(), new PropData(), Material.DIAMOND_BLOCK);
/*
    This makes it so, the block cannot be broken.
    Other methods:
    - #setExplodable(Boolean)
    - #setInteractable(Boolean)
    - #setPushable(Boolean)
*/
block.setBreakable(false); 
block.getPropData().addData("Owner", player.getUniqueId().toString());
block.spawn();

/*
    When you use the BlockManager.init() method, it is automatically saved into the ServiceManager, so you
    can get the instance of your block manager. To get the correct instance, you must use the ServiceManager.getGenericService() method.
*/
var manager = (BlockManager<TestBlock>) ServiceManager.getGenericService(BlockManager.class, TestBlock.class);
Bukkit.broadcastMessage("Registering the block to the database!");
manager.registerProp(block); // Register the block in the database
```
