package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AirElement implements GUIElement {

    @Override
    public ItemStack getIcon(Player player) {
        return new ItemStack(Material.AIR);
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        event.setRefresh(false);
    }
}
