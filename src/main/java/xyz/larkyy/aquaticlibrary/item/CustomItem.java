package xyz.larkyy.aquaticlibrary.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticlibrary.item.impl.VanillaItem;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;
import xyz.larkyy.aquaticlibrary.string.color.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomItem {

    private String name;
    private List<String> description;
    private int amount;
    private int modeldata;
    private Map<Enchantment,Integer> enchantments;
    private Map<PersistentDataType,Object> persistentData = new HashMap<>();
    private List<ItemFlag> flags;

    public CustomItem(String name, List<String> description, int amount, int modeldata, Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.modeldata = modeldata;
        this.enchantments = enchantments;
        this.flags = flags;
    }

    public void setPersistentData(Map<PersistentDataType, Object> persistentData) {
        this.persistentData = persistentData;
    }

    public Map<PersistentDataType, Object> getPersistentData() {
        return persistentData;
    }

    public void addPersistentData(PersistentDataType type, Object value) {
        persistentData.put(type,value);
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getModeldata() {
        return modeldata;
    }

    public List<ItemFlag> getFlags() {
        return flags;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public void setFlags(List<ItemFlag> flags) {
        this.flags = flags;
    }

    public void setModeldata(int modeldata) {
        this.modeldata = modeldata;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomItem() {
        name = null;
        description = null;
        amount = 1;
        modeldata = -1;
        enchantments = new HashMap<>();
        flags = new ArrayList<>();
    }

    public void giveItem(Player player) {
        giveItem(player,amount);
    }

    public void giveItem(Player player, int amount) {
        ItemStack is = getItem();
        is.setAmount(amount);

        player.getInventory().addItem(is);
    }

    public ItemStack getItem() {
        ItemStack is = getUnmodifiedItem();
        ItemMeta im = is.getItemMeta();

        if (im == null) {
            return is;
        }

        if (name != null) {
            im.setDisplayName(Colors.format(name));
        }

        if (description != null) {
            im.setLore(Colors.format(description));
        }

        if (modeldata > 0) {
            im.setCustomModelData(modeldata);
        }

        flags.forEach(im::addItemFlags);

        if (is.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
            enchantments.forEach((enchantment, integer) -> {
                esm.addStoredEnchant(enchantment,integer,true);
            });
            is.setItemMeta(esm);
        } else {
            is.setItemMeta(im);
            is.addUnsafeEnchantments(enchantments);
        }

        var key = new NamespacedKey(ServiceManager.getService(JavaPlugin.class),"Aquatic_Custom_Items");
        persistentData.forEach((type,value) -> im.getPersistentDataContainer().set(key,type,value));

        is.setAmount(amount);
        return is;
    }

    public abstract ItemStack getUnmodifiedItem();

    public static CustomItem create(String namespace, String name, List<String> description,int amount, int modeldata,Map<Enchantment,Integer> enchantments, List<ItemFlag> flags) throws UnknownCustomItemException {
        return ServiceManager.getService(CustomItemHandler.class).getCustomItem(namespace,name,description,amount,modeldata,enchantments,flags);
    }

    public static CustomItem create(Material material) {
        if (material == null) {
            material = Material.STONE;
        }
        return create(new ItemStack(material));
    }

    public static CustomItem create(ItemStack itemStack) {
        if (itemStack == null) {
            itemStack = new ItemStack(Material.STONE);
        }
        try {
            return new VanillaItem(itemStack);
        } catch (UnknownCustomItemException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomItem loadFromYaml(FileConfiguration cfg, String path) throws UnknownCustomItemException {
        if (!cfg.contains(path)) {
            return null;
        }

        List<String> lore = null;
        if (cfg.contains(path+".lore")) {
            lore = cfg.getStringList(path+".lore");
        }

        Map<Enchantment,Integer> enchantments = new HashMap<>();
        if (cfg.contains(path+".enchants")) {
            for (String str : cfg.getStringList(path+".enchants")) {
                String[] strs = str.split(":");
                if (strs.length < 2) {
                    continue;
                }
                Enchantment enchantment = getEnchantmentByString(strs[0]);
                int level = Integer.parseInt(strs[1]);
                enchantments.put(enchantment,level);
            }
        }

        List<ItemFlag> flags = new ArrayList<>();
        if (cfg.contains(path+".flags")) {
            for (String flag : cfg.getStringList(path+".flags")) {
                ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
                flags.add(itemFlag);
            }
        }

        return CustomItem.create(
                cfg.getString(path+".material","STONE"),
                cfg.getString(path+".display-name"),
                lore,
                cfg.getInt(path+".amount",1),
                cfg.getInt(path+".model-data"),
                enchantments,
                flags
        );
    }

    private static Enchantment getEnchantmentByString(String ench) {
        return Enchantment.getByKey(NamespacedKey.minecraft(ench.toLowerCase()));
    }
}
