package de.amethyst.itemplugin.engine;

import de.amethyst.itemplugin.ItemPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static de.amethyst.itemplugin.ItemPlugin.PREFIX;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GUIManager implements Listener {
    @Getter
    private static final GUIManager singleton;
    @Getter
    private static final GUIHistoryManager historyManager;
    static {
        singleton = new GUIManager();
        historyManager = new GUIHistoryManager();
    }
    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(getSingleton(), plugin);
    }

    public void openGUI(Player player, GUIFrame frame) {
        try {
            historyManager.addEntry(player, frame);
            this.renderGUIFrame(player, frame);
            historyManager.setHasGUIOpen(player, true);
        } catch (Exception e) {
            player.sendMessage(PREFIX + "Something went wrong while opening GUI");
            ItemPlugin.getSingleton().getLogger().severe(e.getMessage());
        }
    }

    public void reopenCurrentGUI(Player player) {
        if (historyManager.getLastEntry(player) == null) {
            throw new IllegalArgumentException("Player %s didn't have a GUI open!".formatted(player.getDisplayName()));
        }
        try {
            this.renderGUIFrame(player, historyManager.getLastEntry(player));
            historyManager.setHasGUIOpen(player, true);
        } catch (Exception e) {
            player.sendMessage(PREFIX + ChatColor.RED + "Something went wrong while opening GUI");
            ItemPlugin.getSingleton().getLogger().severe(e.getMessage());
        }
    }

    public void nextGUI(Player player, GUIFrame frame) {
        if (!historyManager.hasGUIOpen(player)) {
            throw new IllegalArgumentException("Player %s didn't have a GUI open!".formatted(player.getDisplayName()));
        }
        try {
            this.openGUI(player, frame);
        } catch (Exception e) {
            player.sendMessage(PREFIX + "Something went wrong while opening GUI");
            ItemPlugin.getSingleton().getLogger().severe(e.getMessage());
        }
    }

    public void previousGUI(Player player) {
        if (!historyManager.hasGUIOpen(player)) {
            throw new IllegalArgumentException("Player %s didn't have a GUI open!".formatted(player.getDisplayName()));
        }
        if (!historyManager.hasPreviousGUI(player)) {
            throw new IllegalArgumentException("Player %s didn't have a previous GUI!".formatted(player.getDisplayName()));
        }
        try {
            historyManager.pop(player);
            this.renderGUIFrame(player, historyManager.getLastEntry(player));
            historyManager.setHasGUIOpen(player, true);
        } catch (Exception e) {
            player.sendMessage(PREFIX + ChatColor.RED + "Something went wrong while opening GUI");
            ItemPlugin.getSingleton().getLogger().severe(e.getMessage());
        }
    }

    public void openExternalInventory(Player player) {
        historyManager.setHasGUIOpen(player, false);
        historyManager.addGUITransfer(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!historyManager.hasGUIOpen(player)) {
            return;
        }

        if (e.getClickedInventory() != e.getView().getTopInventory()) {
            return;
        }

        GUIFrame current = historyManager.getCurrent(player);
        // If the title doesn't equal panic, should never happen
        if (!current.getTitle().equals(e.getView().getTitle())) {
            if (player.hasPermission("itemplugin.exception")) {
                player.sendMessage(PREFIX + ChatColor.RED + "Caused exception!");
            }
            throw new IllegalStateException("Title of current inventory doesn't match InventoryView");
        }

        int index = e.getSlot();
        // If clicked slot isn't in inventory
        if (index < 0 || index > current.getSize()) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        // If clicked slot GUIElement doesn't equal panic, should never happen
        ItemStack icon = current.getElement(index).getIcon(player);
        if (!icon.getType().equals(e.getCurrentItem().getType())) {
            if (player.hasPermission("itemplugin.exception")) {
                player.sendMessage(PREFIX + ChatColor.RED + "Caused exception!");
            }
            throw new IllegalStateException("Icon of current inventory doesn't match clicked item");
        }
        try {
            GUIElementClickEvent event = new GUIElementClickEvent(player, e.getClick());
            current.getElement(index).onClick(event);
            if (historyManager.hasGUIOpen(player) && current == historyManager.getCurrent(player) && event.isRefresh()) { // If Player has GUI open and is on the same GUI and needs a refresh
                this.renderGUIFrame(player, historyManager.getCurrent(player));
            }
        } catch (Exception exception) {
            player.sendMessage(PREFIX + ChatColor.RED + "Something went wrong while processing GUI click!");
            ItemPlugin.getSingleton().getLogger().severe(exception.getMessage());
        } finally {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (!historyManager.hasGUIOpen(player)) {
            return;
        }
        if (historyManager.isGUITransfer(player)) {
            historyManager.removeGUITransfer(player);
            return;
        }
        historyManager.setHasGUIOpen(player, false);
        historyManager.clearHistory(player);
    }

    private void renderGUIFrame(Player player, GUIFrame frame) {
        historyManager.addGUITransfer(player);
        try {
            Bukkit.getScheduler().runTask(ItemPlugin.getSingleton(), () -> {
                frame.render(player);
                historyManager.removeGUITransfer(player);
            });
        } catch (Exception e) {
            player.sendMessage(PREFIX + ChatColor.RED + "Something went wrong rendering GUI!");
            ItemPlugin.getSingleton().getLogger().severe(e.getMessage());
        }
    }
}
