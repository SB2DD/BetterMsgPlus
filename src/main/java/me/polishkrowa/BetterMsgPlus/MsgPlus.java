package me.polishkrowa.BetterMsgPlus;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class MsgPlus extends JavaPlugin {

    //player, lastReceived
    public static HashMap<UUID, UUID> lastReceived = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("tell").setExecutor(new TellCommand());
        this.getCommand("tell").setTabCompleter(new TellCommand());

        this.getCommand("reply").setExecutor(new ReplyCommand());
        this.getCommand("reply").setTabCompleter(new ReplyCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
