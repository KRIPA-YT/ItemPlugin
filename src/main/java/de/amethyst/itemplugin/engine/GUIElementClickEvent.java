package de.amethyst.itemplugin.engine;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Data
public class GUIElementClickEvent {
    private final Player player;
    private final ClickType clickType;
    private boolean refresh = true;
}
