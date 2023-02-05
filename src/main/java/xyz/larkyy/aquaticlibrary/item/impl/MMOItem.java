package xyz.larkyy.aquaticlibrary.item.impl;

import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticlibrary.item.CustomItem;
import xyz.larkyy.aquaticlibrary.item.UnknownCustomItemException;

import java.util.List;
import java.util.Map;

public class MMOItem extends CustomItem {
    private final String identifier;
    private final String type;

    public MMOItem(String identifier, String type, String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        super(name, description, amount, modeldata,enchantments,flags);
        this.identifier = identifier;
        this.type = type;

        if (getUnmodifiedItem() == null) {
            throw new UnknownCustomItemException();
        }
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return MMOItems.plugin.getItem(type,identifier);
    }
}
