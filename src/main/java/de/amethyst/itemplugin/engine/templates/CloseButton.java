package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CloseButton implements GUIElement {
    @Override
    public ItemStack getIcon(Player player) {
        return new ItemBuilder(Material.BARRIER)
                .setName("§aClose GUI")
                .addLoreLine("§7Close the menu")
                .toItemStack();
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        Player player = event.getPlayer();
        this.playDing(player);
        player.closeInventory();
    }
}
