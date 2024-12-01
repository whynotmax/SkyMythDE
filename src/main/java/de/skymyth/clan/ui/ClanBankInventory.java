package de.skymyth.clan.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.clan.model.Clan;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ClanBankInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Clan clan;
    boolean isOwner;

    public ClanBankInventory(SkyMythPlugin plugin, Clan clan, boolean isOwner) {
        super("Clanbank: " + clan.getName(), 27);
        this.plugin = plugin;
        this.clan = clan;
        this.isOwner = isOwner;

        defaultInventory();

        setItems();

    }

    public void setItems() {
        ItemBuilder infoItem = new ItemBuilder(Material.SIGN).setName("§eClanbank Info").lore("§r", "§7Hier kann dein Clan Tokens sparen.", "§r", "§7Tokens: §e" + NumberFormat.getInstance(Locale.GERMAN).format(clan.getBank().getBalance()).replaceAll(",", "."));
        if (clan.getBank().getLastDeposit() == 0) {
            infoItem.addToLore("§7Letzte Einzahlung: §cnie");
        } else {
            infoItem.addToLore("§7Letzte Einzahlung: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(clan.getBank().getLastDeposit()));
        }
        if (isOwner) {
            if (clan.getBank().getLastWithdraw() == 0) {
                infoItem.addToLore("§7Letzte Auszahlung: §cnie");
            } else {
                infoItem.addToLore("§7Letzte Auszahlung: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(clan.getBank().getLastWithdraw()));
            }
        }
        infoItem.addToLore("§r");

        setItem(13, infoItem);

        setItem(11, new ItemBuilder(Material.EMERALD).setName("§aEinzahlen").lore("§7Klicke, um Geld einzuzahlen."), (event -> {
            event.setCancelled(true);
            //if (event.isRightClick()) {
                Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                createDepositConversation(player).begin();
            //}
        }));

        if (isOwner) {
            setItem(15, new ItemBuilder(Material.REDSTONE).setName("§cAuszahlen").lore("§7Klicke, um Geld auszuzahlen."), (event -> {
                event.setCancelled(true);
               // if (event.isLeftClick()) {
                    Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                    createWithdrawConversation(player).begin();
                //}
            }));
        } else {
            setItem(15, new ItemBuilder(Material.BARRIER).setName("§cAuszahlen").lore("§7Nur der Clanbesitzer kann Geld auszahlen"), (event -> {
                Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cNur der Clanbesitzer kann Geld auszahlen.");
            }));
        }
    }

    private Conversation createDepositConversation(Player player) {
        player.closeInventory();
        return new ConversationFactory(plugin)
                .withEscapeSequence("cancel")
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext conversationContext) {
                        return SkyMythPlugin.PREFIX + "§7Wie viel möchtest du einzahlen? (Abbrechen mit 'cancel')";
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext conversationContext, String s) {
                        try {
                            long amount = Long.parseLong(s);
                            if (amount <= 0) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Betrag.");
                                player.sendMessage(SkyMythPlugin.PREFIX + "§7Wie viel möchtest du einzahlen? (Abbrechen mit 'cancel')");
                                return this;
                            }
                            User user = plugin.getUserManager().getUser(player.getUniqueId());
                            if (user.getBalance() < amount) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Tokens.");
                                plugin.getInventoryManager().openInventory(player, new ClanBankInventory(plugin, clan, isOwner));
                                return Prompt.END_OF_CONVERSATION;
                            }
                            player.closeInventory();
                            user.removeBalance(amount);
                            plugin.getUserManager().saveUser(user);
                            clan.getBank().deposit(amount);
                            clan.getBank().setLastDeposit(System.currentTimeMillis());
                            plugin.getClanManager().saveClan(clan);
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replaceAll(",", ".") + " Tokens §7eingezahlt.");
                            plugin.getInventoryManager().openInventory(player, new ClanBankInventory(plugin, clan, isOwner));
                            return Prompt.END_OF_CONVERSATION;
                        } catch (NumberFormatException e) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Betrag.");
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Wie viel möchtest du einzahlen? (Abbrechen mit 'cancel')");
                            return this;
                        }
                    }
                })
                .withLocalEcho(false)
                .buildConversation(player);
    }

    private Conversation createWithdrawConversation(Player player) {
        player.closeInventory();
        return new ConversationFactory(plugin)
                .withEscapeSequence("cancel")
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext conversationContext) {
                        return SkyMythPlugin.PREFIX + "§7Wie viel möchtest du auszahlen? (Abbrechen mit 'cancel')";
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext conversationContext, String s) {
                        try {
                            long amount = Long.parseLong(s);
                            if (amount <= 0) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Betrag.");
                                player.sendMessage(SkyMythPlugin.PREFIX + "§7Wie viel möchtest du auszahlen? (Abbrechen mit 'cancel')");
                                return this;
                            }
                            User user = plugin.getUserManager().getUser(player.getUniqueId());
                            if (clan.getBank().getBalance() < amount) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Clan hat nicht genügend Tokens.");
                                plugin.getInventoryManager().openInventory(player, new ClanBankInventory(plugin, clan, isOwner));
                                return Prompt.END_OF_CONVERSATION;
                            }
                            player.closeInventory();
                            user.addBalance(amount);
                            plugin.getUserManager().saveUser(user);
                            clan.getBank().withdraw(amount);
                            clan.getBank().setLastWithdraw(System.currentTimeMillis());
                            plugin.getClanManager().saveClan(clan);
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replaceAll(",", ".") + " Tokens §7ausgezahlt.");
                            plugin.getInventoryManager().openInventory(player, new ClanBankInventory(plugin, clan, isOwner));
                            return Prompt.END_OF_CONVERSATION;
                        } catch (NumberFormatException e) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Betrag.");
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Wie viel möchtest du auszahlen? (Abbrechen mit 'cancel')");
                            return this;
                        }
                    }
                })
                .withLocalEcho(false)
                .buildConversation(player);
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
