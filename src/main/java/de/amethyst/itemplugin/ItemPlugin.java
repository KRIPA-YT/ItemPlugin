package de.amethyst.itemplugin;

import de.amethyst.itemplugin.engine.GUIManager;
import de.amethyst.itemplugin.itemgui.ItemSelectFrame;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemPlugin extends JavaPlugin implements CommandExecutor {
    public static final String PREFIX = ChatColor.DARK_PURPLE + "[" + ChatColor.AQUA + "ItemPlugin" + ChatColor.DARK_PURPLE + "] " + ChatColor.WHITE;
    @Getter
    private static ItemPlugin singleton;

    @Override
    public void onEnable() {
        singleton = this;
        this.getCommand("i").setExecutor(this);
        GUIManager.init(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(PREFIX + "You can only use this command as a Player!");
            return true;
        }
        GUIManager.getSingleton().openGUI(p, new ItemSelectFrame(String.join("_", args)));
        return true;
    }
}
