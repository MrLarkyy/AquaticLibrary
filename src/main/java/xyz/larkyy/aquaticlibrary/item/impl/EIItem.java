package xyz.larkyy.aquaticlibrary.item.impl;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticlibrary.item.CustomItem;
import xyz.larkyy.aquaticlibrary.item.UnknownCustomItemException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EIItem extends CustomItem {
    private final String identifier;

    public EIItem(String identifier, String name, List<String> description, int amount, int modeldata, Map<Enchantment, Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        super(name, description, amount, modeldata, enchantments, flags);
        this.identifier = identifier;

        if (getUnmodifiedItem() == null) {
            throw new UnknownCustomItemException();
        }
    }

    @Override
    public ItemStack getUnmodifiedItem() {
        return ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(identifier).get().buildItem(1, Optional.empty());
    }
}
