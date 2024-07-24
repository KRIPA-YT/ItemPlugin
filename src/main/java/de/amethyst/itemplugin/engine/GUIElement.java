package de.amethyst.itemplugin.engine;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface GUIElement {
    ItemStack getIcon(Player player);
    void onClick(GUIElementClickEvent event);

    default void playDing(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1.0F, 2.0F);
    }

    default void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.1F);
    }
}
