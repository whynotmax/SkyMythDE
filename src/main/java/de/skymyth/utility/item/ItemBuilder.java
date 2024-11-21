package de.skymyth.utility.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemBuilder extends ItemStack {

    public ItemBuilder() {
        super(Material.AIR, 1);
    }

    public ItemBuilder(Material type) {
        super(type);
    }

    public ItemBuilder(Material type, int amount) {
        super(type, amount);
    }

    public ItemBuilder(Material type, int amount, short damage) {
        super(type, amount, damage);
    }

    public ItemBuilder(Material type, int amount, short damage, byte data) {
        super(type, amount, damage, data);
    }

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public ItemBuilder setMaterial(Material material) {
        setType(material);
        return this;
    }

    public ItemBuilder setDataId(int id) {
        setDurability((short) id);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int i) {
        this.setDurability((short) i);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Arrays.asList(lore));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addToLore(String... lore) {
        ItemMeta meta = getItemMeta();
        List<String> loreList = (meta.getLore() == null || meta.getLore().isEmpty() ? new ArrayList<>() : meta.getLore());
        Collections.addAll(loreList, lore);
        meta.setLore(loreList);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addToLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        List<String> loreList = meta.getLore();
        loreList.addAll((lore));
        meta.setLore(loreList);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder glow(final boolean glowing) {
        final ItemMeta itemMeta = this.getItemMeta();
        assert itemMeta != null;
        if (glowing) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            itemMeta.removeEnchant(Enchantment.LUCK);
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        this.setItemMeta(itemMeta);
        return this;
    }
    public ItemBuilder glow() {
        final ItemMeta itemMeta = this.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, false);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.removeEnchant(enchantment);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.removeItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(String playerName) {
        setDataId(3);
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwner(playerName);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addSkullValue(String value) {
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        Bukkit.getUnsafe().modifyItemStack(this, "{display:SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}");
        return this;
    }

    public boolean isUnbreakable() {
        return getItemMeta().spigot().isUnbreakable();
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        setItemMeta(meta);
        return this;
    }


    public NBTTagCompound getNBTTagCompound() {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        return itemStack.getTag() == null ? new NBTTagCompound() : itemStack.getTag();
    }

    public ItemBuilder setNBTTagCompound(NBTTagCompound nbtTagCompound) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this);
        itemStack.setTag(nbtTagCompound);
        return new ItemBuilder(CraftItemStack.asBukkitCopy(itemStack));
    }

    public boolean hasNBTTag(String key) {
        return getNBTTagCompound().hasKey(key);
    }

    public String getNBTTagValue(String key) {
        return getNBTTagCompound().getString(key);
    }

}
