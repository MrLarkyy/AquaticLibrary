package xyz.larkyy.aquaticlibrary.item.factories;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import xyz.larkyy.aquaticlibrary.item.CustomItem;
import xyz.larkyy.aquaticlibrary.item.ItemFactory;
import xyz.larkyy.aquaticlibrary.item.UnknownCustomItemException;
import xyz.larkyy.aquaticlibrary.item.impl.MMOItem;

import java.util.List;
import java.util.Map;

public class MMOFactory implements ItemFactory {
    @Override
    public CustomItem create(String identifier, String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        String[] strs = identifier.split(":");
        return new MMOItem(strs[1],strs[0],name,description,amount,modeldata,enchantments,flags);
    }
}
