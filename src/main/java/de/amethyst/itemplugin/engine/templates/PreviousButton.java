package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.GUIManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Setter @Getter
public class PreviousButton implements GUIElement, HasAlternate {
    private GUIElement alternate;
    public PreviousButton() {
        this("GRAY");
    }
    public PreviousButton(String color) {
        this(new EmptyElement(color));
    }

    public PreviousButton(GUIElement alternate) {
        this.alternate = alternate;
    }

    @Override
    public ItemStack getIcon(Player player) {
        if (!GUIManager.getHistoryManager().hasPreviousGUI(player)) {
            return this.alternate.getIcon(player);
        }
        ItemStack icon = new ItemStack(Material.ARROW);
        ItemMeta iconMeta = icon.getItemMeta();
        assert iconMeta != null;
        iconMeta.setDisplayName(ChatColor.GREEN + "Back");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        Player player = event.getPlayer();
        if (!GUIManager.getHistoryManager().hasPreviousGUI(player)) {
            this.alternate.onClick(event);
            return;
        }
        this.playDing(player);
        GUIManager.getSingleton().previousGUI(player);
    }
}
