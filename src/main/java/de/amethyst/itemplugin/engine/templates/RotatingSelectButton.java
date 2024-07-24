package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RotatingSelectButton implements GUIElement {
    private ItemStack icon;
    @Getter
    private String[] options;
    @Getter
    private int selectedOption;
    @Getter
    private ChatColor selectedColor;

    protected List<String> instructions = new ArrayList<>(List.of("§bRight click for previous", "§eLeft click for next"));

    public RotatingSelectButton(ItemStack icon, String... options) {
        this(icon, 0, ChatColor.AQUA, options);
    }

    public RotatingSelectButton(ItemStack icon, ChatColor selectedColor, String... options) {
        this(icon, 0, selectedColor, options);
    }

    public RotatingSelectButton(ItemStack icon, int selectedOption, String... options) {
        this(icon, selectedOption, ChatColor.AQUA, options);
    }

    public RotatingSelectButton(ItemStack icon, int selectedOption, ChatColor selectedColor, String... options) {
        if (options.length <= 0) {
            throw new IllegalArgumentException("Length of options should be more than 0");
        }

        this.icon = icon;
        this.options = options;
        this.selectedOption = selectedOption;
        this.selectedColor = selectedColor;
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        this.playDing(event.getPlayer());
        this.selectedOption = (this.selectedOption + (event.getClickType().isLeftClick() ? 1 : (this.options.length - 1))) % this.options.length;
    }

    @Override
    public ItemStack getIcon(Player player) {
        ItemBuilder iconBuilder = new ItemBuilder(this.icon.clone());
        for (int i = 0; i < this.options.length; i++) {
            String prefix = (i == this.selectedOption) ? this.selectedColor + "▶ " : "§8";
            iconBuilder.addLoreLine(prefix + this.options[i]);
        }
        iconBuilder.addLoreLine("");
        iconBuilder.addLoreLines(this.instructions.toArray(String[]::new));
        return iconBuilder.toItemStack();
    }
}