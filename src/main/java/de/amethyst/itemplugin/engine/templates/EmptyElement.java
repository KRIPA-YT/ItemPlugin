package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class EmptyElement implements GUIElement {
    @NonNull
    private String color = "GRAY";

    public EmptyElement(String color) {
        if (Material.getMaterial(color.toUpperCase() + "_STAINED_GLASS_PANE") == null) {
            throw new IllegalArgumentException("color has to be a valid STAINED_GLASS_PANE color!");
        }
        this.color = color;
    }
    @Override
    public ItemStack getIcon(Player player) {
        ItemStack icon = new ItemStack(Objects.requireNonNull(Material.getMaterial(color.toUpperCase() + "_STAINED_GLASS_PANE")));
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName(ChatColor.GRAY + "");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        event.setRefresh(false);
    }
}
