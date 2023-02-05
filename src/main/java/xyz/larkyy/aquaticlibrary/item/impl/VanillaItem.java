package xyz.larkyy.aquaticlibrary.item.impl;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticlibrary.item.CustomItem;
import xyz.larkyy.aquaticlibrary.item.UnknownCustomItemException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaItem extends CustomItem {

    private final ItemStack itemStack;

    public VanillaItem(Material material, String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        super(name, description,amount,modeldata,enchantments,flags);
        this.itemStack = new ItemStack(material);

        if (getUnmodifiedItem() == null) {
            throw new UnknownCustomItemException();
        }
    }

    public VanillaItem(ItemStack itemStack) throws UnknownCustomItemException {
        super(null,null,itemStack.getAmount(), itemStack.getItemMeta() == null ? -1 :  itemStack.getItemMeta().getCustomModelData(),new HashMap<>(), new ArrayList<>());
        this.itemStack = itemStack;

        if (getUnmodifiedItem() == null) {
            throw new UnknownCustomItemException();
        }
    }

    public VanillaItem(Material material) throws UnknownCustomItemException {
        super(null,null,1, -1,new HashMap<>(), new ArrayList<>());
        this.itemStack = new ItemStack(material);

        if (getUnmodifiedItem() == null) {
            throw new UnknownCustomItemException();
        }
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return itemStack;
    }
}
