package com.stormai.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class Main extends JavaPlugin implements Listener {
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        getLogger().info("Launching into the sky! Plugin enabled.");
        commandHandler = new CommandHandler(this);

        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        // Register commands
        getCommand("boom").setExecutor(commandHandler);
        getCommand("boom").setTabCompleter(commandHandler);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled. Safe landing.");
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}