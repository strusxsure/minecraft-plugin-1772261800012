package com.stormai.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class CommandHandler implements CommandExecutor, TabCompleter, Listener {
    private final Main plugin;
    private final Map<UUID, Long> immunePlayers;

    public CommandHandler(Main plugin) {
        this.plugin = plugin;
        this.immunePlayers = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("boom")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        long currentTime = System.currentTimeMillis();

        if (immunePlayers.containsKey(player.getUniqueId())) {
            long remaining = 5000 - (currentTime - immunePlayers.get(player.getUniqueId()));
            if (remaining > 0) {
                player.sendMessage("You must wait " + (remaining / 1000.0) + " seconds before using this command again.");
                return true;
            }
        }

        launchPlayer(player);
        logCommandUsage(player);
        return true;
    }

    private void launchPlayer(Player player) {
        // Apply upward velocity
        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(2));

        // Store immunity time
        long immunityTime = System.currentTimeMillis();
        immunePlayers.put(player.getUniqueId(), immunityTime);

        // Schedule removal of immunity after 5 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                immunePlayers.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 100); // 100 ticks = 5 seconds
    }

    private void logCommandUsage(Player player) {
        plugin.getLogger().info(player.getName() + " used /boom at " + new java.util.Date());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No tab completion needed
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && isImmune(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        immunePlayers.remove(event.getPlayer().getUniqueId());
    }

    private boolean isImmune(Player player) {
        Long immunityTime = immunePlayers.get(player.getUniqueId());
        if (immunityTime == null) return false;
        return System.currentTimeMillis() - immunityTime < 5000;
    }
}