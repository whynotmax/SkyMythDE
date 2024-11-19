package de.skymyth.auctionhouse;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.auctionhouse.model.category.AuctionHouseItemCategory;
import de.skymyth.auctionhouse.repository.AuctionHouseItemRepository;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

public class AuctionHouseManager {

    SkyMythPlugin plugin;
    AuctionHouseItemRepository repository;
    Map<UUID, List<AuctionHouseItem>> auctionHouseItems;

    public AuctionHouseManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(AuctionHouseItemRepository.class);
        this.auctionHouseItems = new HashMap<>(this.repository.findAll().stream().collect(Collectors.groupingBy(AuctionHouseItem::getSeller)));
    }

    public void addAuctionHouseItem(AuctionHouseItem auctionHouseItem) {
        this.repository.save(auctionHouseItem);
        List<AuctionHouseItem> items = this.auctionHouseItems.getOrDefault(auctionHouseItem.getSeller(), new ArrayList<>(this.repository.findManyBySeller(auctionHouseItem.getSeller())));
        items.add(auctionHouseItem);
        this.auctionHouseItems.put(auctionHouseItem.getSeller(), items);
    }

    public void removeAuctionHouseItem(AuctionHouseItem auctionHouseItem) {
        this.repository.delete(auctionHouseItem);
        List<AuctionHouseItem> items = this.auctionHouseItems.getOrDefault(auctionHouseItem.getSeller(), new ArrayList<>(this.repository.findManyBySeller(auctionHouseItem.getSeller())));
        items.remove(auctionHouseItem);
        this.auctionHouseItems.put(auctionHouseItem.getSeller(), items);
    }

    public List<AuctionHouseItem> getExpiredAuctionHouseItems(UUID seller) {
        return this.auctionHouseItems.getOrDefault(seller, new ArrayList<>()).stream().filter(AuctionHouseItem::isExpired).collect(Collectors.toList());
    }

    public List<AuctionHouseItem> getAuctionHouseItems(UUID seller) {
        return this.auctionHouseItems.getOrDefault(seller, new ArrayList<>());
    }

    public List<AuctionHouseItem> getAllAuctionHouseItems(AuctionHouseItemCategory category) {
        return this.auctionHouseItems.values().stream().flatMap(Collection::stream).filter(item -> item.getCategory().equals(category)).collect(Collectors.toList());
    }

    public List<AuctionHouseItem> getAuctionHouseItems() {
        return this.auctionHouseItems.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public AuctionHouseItemCategory determineCategory(AuctionHouseItem auctionHouseItem) {
        List<Material> pvpItems = List.of(Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.BOW, Material.ARROW, Material.GOLDEN_APPLE, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_HELMET, Material.GOLD_BOOTS, Material.GOLD_LEGGINGS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET);
        List<Material> toolsItems = List.of(Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_HOE, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_HOE, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_HOE, Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE);
        List<Material> foodItems = List.of(Material.APPLE, Material.BREAD, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.COOKED_RABBIT, Material.CAKE, Material.COOKIE, Material.CARROT, Material.POTATO, Material.BAKED_POTATO, Material.POISONOUS_POTATO, Material.PUMPKIN_PIE, Material.RABBIT_STEW, Material.GOLDEN_CARROT);
        boolean isBlock = auctionHouseItem.getItemStack().getType().isBlock();
        List<Material> redstoneItems = List.of(Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE, Material.REDSTONE_WIRE, Material.DISPENSER, Material.PISTON_BASE, Material.HOPPER, Material.DROPPER, Material.DAYLIGHT_DETECTOR, Material.LEVER, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL, Material.TRIPWIRE_HOOK, Material.TRIPWIRE, Material.TNT, Material.TRAPPED_CHEST, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE, Material.REDSTONE_WIRE);
        List<Material> miscItems = List.of(Material.CHEST, Material.FURNACE, Material.WORKBENCH, Material.BOOKSHELF, Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.BREWING_STAND, Material.CAULDRON, Material.BEACON, Material.JUKEBOX, Material.NOTE_BLOCK, Material.BED, Material.BANNER, Material.BARRIER, Material.BEACON, Material.BREWING_STAND, Material.DAYLIGHT_DETECTOR, Material.FLOWER_POT, Material.TRAPPED_CHEST, Material.WRITTEN_BOOK);
        if (pvpItems.contains(auctionHouseItem.getItemStack().getType())) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.PVP);
            return AuctionHouseItemCategory.PVP;
        } else if (toolsItems.contains(auctionHouseItem.getItemStack().getType())) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.TOOLS);
            return AuctionHouseItemCategory.TOOLS;
        } else if (foodItems.contains(auctionHouseItem.getItemStack().getType())) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.FOOD);
            return AuctionHouseItemCategory.FOOD;
        } else if (redstoneItems.contains(auctionHouseItem.getItemStack().getType())) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.REDSTONE);
            return AuctionHouseItemCategory.REDSTONE;
        } else if (miscItems.contains(auctionHouseItem.getItemStack().getType())) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.MISC);
            return AuctionHouseItemCategory.MISC;
        } else if (isBlock) {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.BLOCKS);
            return AuctionHouseItemCategory.BLOCKS;
        } else {
            auctionHouseItem.setCategory(AuctionHouseItemCategory.SPECIAL);
            return AuctionHouseItemCategory.SPECIAL;
        }
    }

}
