package de.amethyst.itemplugin.engine;

import de.amethyst.itemplugin.engine.history.UnorderedHistoryManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GUIHistoryManager extends UnorderedHistoryManager<UUID, GUIFrame> {
    private final Set<UUID> hasGUIOpen = new HashSet<>();
    private final Set<UUID> guiTransfer = new HashSet<>();

    public void addEntry(Player player, GUIFrame entry) {
        this.addEntry(player.getUniqueId(), entry);
    }

    public void pop(Player player) {
        this.pop(player.getUniqueId());
    }

    public void clearHistory(Player player) {
        this.clearHistory(player.getUniqueId());
    }

    public boolean hasHistory(Player player) {
        return this.hasHistory(player.getUniqueId());
    }

    public GUIFrame getLastEntry(Player player) {
        return this.getLastEntry(player.getUniqueId());
    }

    public int length(Player player) {
        return this.length(player.getUniqueId());
    }

    public GUIFrame getCurrent(Player player) {
        return this.getCurrent(player.getUniqueId());
    }

    public GUIFrame getCurrent(UUID uuid) {
        if (!this.hasGUIOpen(uuid)) {
            return null;
        }
        return this.getLastEntry(uuid);
    }

    public boolean hasPreviousGUI(Player player) {
        return this.hasPreviousGUI(player.getUniqueId());
    }

    public boolean hasPreviousGUI(UUID uuid) {
        if (!this.history.containsKey(uuid)) {
            return false;
        }
        return this.history.get(uuid).size() >= 2;
    }

    protected void setHasGUIOpen(Player player, boolean hasGUIOpen) {
        this.setHasGUIOpen(player.getUniqueId(), hasGUIOpen);
    }

    protected void setHasGUIOpen(UUID uuid, boolean hasGUIOpen) {
        if (hasGUIOpen) {
            this.hasGUIOpen.add(uuid);
        } else {
            this.hasGUIOpen.remove(uuid);
        }
    }

    protected void addGUITransfer(Player player) {
        this.addGUITransfer(player.getUniqueId());
    }

    protected void addGUITransfer(UUID uuid) {
        this.guiTransfer.add(uuid);
    }

    protected boolean removeGUITransfer(Player player) {
        return this.removeGUITransfer(player.getUniqueId());
    }

    protected boolean removeGUITransfer(UUID uuid) {
        return this.guiTransfer.remove(uuid);
    }

    protected boolean isGUITransfer(Player player) {
        return this.isGUITransfer(player.getUniqueId());
    }

    protected boolean isGUITransfer(UUID uuid) {
        return this.guiTransfer.contains(uuid);
    }

    public boolean hasGUIOpen(Player player) {
        return this.hasGUIOpen(player.getUniqueId());
    }

    public boolean hasGUIOpen(UUID uuid) {
        return this.hasGUIOpen.contains(uuid) && this.hasHistory(uuid);
    }
}
