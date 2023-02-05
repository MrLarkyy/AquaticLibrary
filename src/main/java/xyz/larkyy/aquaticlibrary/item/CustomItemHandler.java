package xyz.larkyy.aquaticlibrary.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import xyz.larkyy.aquaticlibrary.item.factories.*;
import xyz.larkyy.aquaticlibrary.item.impl.VanillaItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemHandler {

    private final Map<String,ItemFactory> itemRegisters = new HashMap<String,ItemFactory>() {
        {
            put("itemsadder", new IAFactory());
            put("oraxen", new OraxenFactory());
            put("hdb", new HDBFactory());
            put("mythicitem", new MMFactory());
            put("mmoitem", new MMOFactory());
            put("executableblock", new EBFactory());
            put("executableitem",new EIFactory());
        }
    };

    public void addItemFactory(String id, ItemFactory factory) {
        itemRegisters.put(id,factory);
    }

    public CustomItem getCustomItem(String namespace, String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        String[] strs = namespace.split(":");
        String provider = strs[0].toLowerCase();

        ItemFactory factory = itemRegisters.get(provider);
        if (strs.length == 1 || factory == null) {
            return new VanillaItem(Material.valueOf(strs[0].toUpperCase()),name,description,amount,modeldata,enchantments,flags);
        }
        String identifier = namespace.substring(provider.length()+1);

        return factory.create(identifier,name,description,amount,modeldata,enchantments,flags);
    }
}
