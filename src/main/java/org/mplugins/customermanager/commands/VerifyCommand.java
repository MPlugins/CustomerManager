package org.mplugins.customermanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.mplugins.customermanager.utils.VerificationManager;

public class VerifyCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cOnly players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        VerificationManager verificationManager = VerificationManager.getInstance();

        if (verificationManager.isVerified(player.getUniqueId()))
        {
            player.sendMessage("§7You are already §averified§7. No need to do this!");
            return true;
        }

        if (args.length < 1)
        {
            player.sendMessage("§cUsage: /verify <code>");
            return true;
        }

        String code = args[0];

        if (!verificationManager.isCodeValid(code))
        {
            player.sendMessage("§cThis code is not valid!");
            return true;
        }

        verificationManager.verify(player.getUniqueId(), code);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.sendMessage("§aYou are now verified!");

        return true;
    }
}
