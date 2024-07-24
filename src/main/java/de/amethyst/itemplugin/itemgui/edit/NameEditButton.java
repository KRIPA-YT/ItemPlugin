package de.amethyst.itemplugin.itemgui.edit;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.ItemPlugin;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.GUIManager;
import de.amethyst.itemplugin.engine.templates.RotatingSelectButton;
import de.rapha149.signgui.SignGUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.amethyst.itemplugin.ItemPlugin.PREFIX;

class NameEditButton extends RotatingSelectButton implements Listener {
    private static final List<UUID> chatGUIOpen = new ArrayList<>();
    public NameEditButton() {
        super(new ItemBuilder(Material.ANVIL).setName("§aEdit Name").toItemStack(),
                "Anvil GUI",
                "Sign GUI",
                "Chat");
        this.instructions = new ArrayList<>(this.instructions.stream().map((instruction) -> {
            String bagBegin = instruction.substring(0, 2);
            String bagEnd = instruction.substring(2);
            return bagBegin + "Shift " + bagEnd;
        }).toList());
        this.instructions.add("§5Click to edit");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!chatGUIOpen.contains(player.getUniqueId())) {
            return;
        }

        this.updateName(player, event.getMessage());

        chatGUIOpen.remove(player.getUniqueId());
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        chatGUIOpen.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        if (event.getClickType().isShiftClick()) {
            super.onClick(event);
            return;
        }
        Player player = event.getPlayer();
        this.playDing(player);
        switch (this.getSelectedOption()) {
            case 0 -> this.anvilGUI(player);
            case 1 -> this.signGUI(player);
            case 2 -> this.chatGUI(player);
        }
    }

    private void chatGUI(Player player) {
        chatGUIOpen.add(player.getUniqueId());
        player.sendMessage(PREFIX + "Enter item name here:");
        GUIManager.getSingleton().openExternalInventory(player);
        player.closeInventory();

        PluginManager pm = Bukkit.getPluginManager();
        HandlerList.unregisterAll(this);
        pm.registerEvents(this, ItemPlugin.getSingleton());
    }

    private void signGUI(Player player) {
        SignGUI signGUI = SignGUI.builder()
                .setLines(ItemEditHistoryManager.getSingleton().getCurrentEntry(player.getUniqueId()).getItemMeta().getDisplayName(),
                        "^^^^^^^^^^^^^^^",
                        "Edit item",
                        "name here")
                .setType(Material.OAK_SIGN)
                .setHandler((signGUIPlayer, signGUIResult) -> {
                    this.updateName(signGUIPlayer, signGUIResult.getLineWithoutColor(0).replace(' ', '_'));
                    return Collections.emptyList();
                })
                .build();
        GUIManager.getSingleton().openExternalInventory(player);
        signGUI.open(player);
    }

    private void anvilGUI(Player player) {
        GUIManager.getSingleton().openExternalInventory(player);
        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> this.updateName(stateSnapshot.getPlayer(), stateSnapshot.getText()))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .title("Edit name")
                .itemLeft(ItemEditHistoryManager.getSingleton().getCurrentEntry(player.getUniqueId()).clone())
                .plugin(ItemPlugin.getSingleton())
                .open(player);
    }

    private void updateName(Player player, String name) {
        ItemStack edit = ItemEditHistoryManager.getSingleton().getCurrentEntry(player.getUniqueId()).clone();
        ItemMeta editMeta = edit.getItemMeta();
        String coloredName = ChatColor.translateAlternateColorCodes('&', name);
        editMeta.setDisplayName(coloredName);
        edit.setItemMeta(editMeta);
        ItemEditHistoryManager.getSingleton().editStaged(player.getUniqueId(), edit);
        ItemEditHistoryManager.getSingleton().push(player.getUniqueId());
        GUIManager.getSingleton().reopenCurrentGUI(player);
    }
}
