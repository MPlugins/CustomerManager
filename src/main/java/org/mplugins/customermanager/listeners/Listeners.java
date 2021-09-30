package org.mplugins.customermanager.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mplugins.customermanager.utils.VerificationManager;

public class Listeners implements Listener
{
    private static final VerificationManager verificationManager = VerificationManager.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (verificationManager.isVerified(player.getUniqueId()))
            return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ())
            event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (!verificationManager.isVerified(player.getUniqueId()))
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE,
                    false, false, false));
            player.sendMessage("§cYou are not verified! Use /verify <code> to verify yourself.");
        }
        else
            player.sendMessage("§aYou are already verified and don't need to verify again!");

        event.joinMessage(null);
    }
}
