package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.user.model.setting.Setting;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class SettingsInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    User user;

    public SettingsInventory(SkyMythPlugin plugin, User user) {
        super("Einstellungen", 54);
        this.plugin = plugin;
        this.user = user;

        setItems();

    }

    private void setItems() {

        defaultInventory();

        int slot = 10;

        for (Setting setting : Setting.VALUES) {
            if (slot == 17) {
                slot = 28;
            }

            ItemBuilder display = new ItemBuilder(setting.getDisplay());
            display.setName("§8» §e" + setting.getName());
            setItem(slot, display);

            ItemBuilder toggle = new ItemBuilder(Material.SIGN);
            toggle.setName("§8» " + setting.getValueNames().get(user.getSetting(setting)));
            toggle.lore(
                    "§r",
                    "§7Mögliche Werte:"
            );
            for (int i = 0; i < setting.getAvailableValues().size(); i++) {
                toggle.addToLore(
                        "§8» §7" + setting.getValueNames().get(i)
                );
            }
            toggle.addToLore("§r");

            setItem(slot + 9, toggle, event -> {
                Player player = (Player) event.getWhoClicked();
                int currentValue = user.getSetting(setting);
                int nextValue = currentValue + 1;
                if (nextValue >= setting.getAvailableValues().size()) {
                    nextValue = 0;
                }
                user.setSetting(setting, nextValue);
                plugin.getUserManager().saveUser(user);
                setItems();
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);

                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast die Einstellung §e" + setting.getName() + "§7 auf §8'§r" + setting.getValueNames().get(nextValue) + "§8'§7 gesetzt.");
            });
            slot++;
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
