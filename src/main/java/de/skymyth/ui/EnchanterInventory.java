package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.Pair;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class EnchanterInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    List<Integer> randomEnchantSlots = List.of(23, 24, 32, 33);
    List<Pair<Enchantment, Integer>> enchantsSelected = new ArrayList<>();
    List<Integer> selectedSlots = new ArrayList<>();
    ItemStack itemStack;

    public EnchanterInventory(SkyMythPlugin plugin) {
        super("Enchanter", 54);
        this.plugin = plugin;

        this.defaultInventory();

        setItem(20, new ItemStack(Material.AIR), event -> {
            if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                ItemStack cursor = event.getCursor();
                event.setCancelled(true);
                event.setCursor(new ItemStack(Material.AIR));
                update(cursor.clone());
            }
        });
        setItem(29, new ItemBuilder(Material.INK_SACK).setDataId(1).setName("§cKann nicht verzaubert werden"));

        for (int i = 0; i < 4; i++) {
            setItem(randomEnchantSlots.get(i), new ItemBuilder(Material.BARRIER).setName("§r"));
        }

    }

    public void update(ItemStack itemStack) {
        setItem(20, itemStack, event -> {
            event.setCursor(itemStack.clone());
            setItem(20, new ItemStack(Material.AIR));
            update(new ItemBuilder(Material.AIR));
        });
        this.itemStack = itemStack;
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            setItem(29, new ItemBuilder(Material.INK_SACK).setDataId(1).setName("§cKann nicht verzaubert werden"), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Tool in das obere Feld legen.");
            });
            return;
        } else {
            if (itemStack.getEnchantments().size() >= 4) {
                setItem(29, new ItemBuilder(Material.INK_SACK).setDataId(1).setName("§cMaximal 4 Verzauberungen erlaubt"), event -> {
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst maximal 4 Verzauberungen hinzufügen.");
                });
                return;
            }
            setItem(29, new ItemBuilder(Material.INK_SACK).setDataId(10).setName("§aVerzaubern").lore("§7§oDu hast §e§o" + enchantsSelected.size() + "§7§o von §e§o4§7§o Verzauberungen ausgewählt."), event -> {
                Player player = (Player) event.getWhoClicked();
                if (enchantsSelected.isEmpty()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst mindestens eine Verzauberung auswählen.");
                    return;
                }
                for (Pair<Enchantment, Integer> enchantment : enchantsSelected) {
                    Enchantment ench = enchantment.getKey();
                    int level = enchantment.getValue();
                    if (itemStack.getEnchantments().containsKey(ench)) {
                        int currentLevel = itemStack.getEnchantments().get(ench);
                        if (currentLevel >= level)
                            itemStack.addUnsafeEnchantment(ench, (currentLevel >= ench.getMaxLevel() ? ench.getMaxLevel() : currentLevel + 1));
                        else itemStack.addUnsafeEnchantment(ench, level);
                    } else {
                        itemStack.addUnsafeEnchantment(ench, level);
                    }
                }
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Item verzaubert.");
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende Verzauberungen wurden hinzugefügt:");
                int get = 0;
                for (Pair<Enchantment, Integer> enchantment : enchantsSelected) {
                    get++;
                    player.sendMessage(SkyMythPlugin.PREFIX + "§8- §e" + enchantment.getKey().getName() + " §7auf Level §e" + enchantment.getValue());
                    if (enchantsSelected.size() == get) {
                        break;
                    }
                }
                player.closeInventory();
            });
        }
        if (Util.isTool(itemStack.getType())) {
            //Can be enchanted
            Map<Enchantment, Integer> enchantments = new HashMap<>(itemStack.getEnchantments());
            for (int i = 0; i < 4; i++) {
                Pair<Enchantment, Integer> randomEnchantment = getRandomEnchantment(enchantments);
                if (randomEnchantment == null) {
                    setItem(randomEnchantSlots.get(i), new ItemBuilder(Material.BARRIER).setName("§r"));
                    continue;
                }
                int finalI = i;
                setItem(randomEnchantSlots.get(i), new ItemBuilder((selectedSlots.contains(randomEnchantSlots.get(i)) ? Material.ENCHANTED_BOOK : Material.BOOK)).setName("§a§kRandom Enchant§r §a§k" + randomEnchantment.getValue() + "§r").lore(selectedSlots.contains(randomEnchantSlots.get(i)) ? "§7§oAusgewählt" : "§7§oNicht ausgewählt"), event -> {
                    if (selectedSlots.contains(randomEnchantSlots.get(finalI))) {
                        enchantsSelected.remove(randomEnchantment);
                        selectedSlots.remove(randomEnchantSlots.get(finalI));
                        update(itemStack);
                        return;
                    }
                    selectedSlots.add(randomEnchantSlots.get(finalI));
                    enchantsSelected.add(randomEnchantment);
                    update(itemStack);
                });
            }
        }
    }

    public Pair<Enchantment, Integer> getRandomEnchantment(Map<Enchantment, Integer> alreadyEnchanted) {
        Map<Enchantment, Integer> allAvailableEnchantments = Arrays.stream(Enchantment.values()).map(enchantment -> {
                    if (!enchantment.canEnchantItem(itemStack)) return null;
                    if (alreadyEnchanted.containsKey(enchantment)) {
                        if (alreadyEnchanted.get(enchantment) >= enchantment.getMaxLevel()) return null;
                        if (enchantment.equals(Enchantment.SILK_TOUCH) && alreadyEnchanted.containsKey(Enchantment.LOOT_BONUS_BLOCKS))
                            return null;
                        if (enchantment.getItemTarget().includes(itemStack.getType())) return enchantment;
                    }
                    return enchantment;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(enchantment -> enchantment, enchantment -> (int) (Math.random() * enchantment.getMaxLevel()) + 1));

        if (allAvailableEnchantments.isEmpty()) return null;
        List<Enchantment> enchantments = new ArrayList<>(allAvailableEnchantments.keySet());
        Enchantment enchantment = enchantments.get((int) (Math.random() * enchantments.size()));
        if (enchantment == null) return null;
        if (alreadyEnchanted.get(enchantment) != null) {
            int enchantmentOnItem = alreadyEnchanted.get(enchantment);
            if (enchantmentOnItem >= enchantment.getMaxLevel()) return null;
            if (enchantment.equals(Enchantment.SILK_TOUCH) && alreadyEnchanted.containsKey(Enchantment.LOOT_BONUS_BLOCKS))
                enchantment = null;
            if (enchantment.getItemTarget().includes(itemStack.getType()))
                return new Pair<>(enchantment, enchantmentOnItem + 1);
        }
        return new Pair<>(enchantment, allAvailableEnchantments.get(enchantment));
    }

    @Override
    public void close(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.getInventory().addItem(itemStack.clone());
    }
}
