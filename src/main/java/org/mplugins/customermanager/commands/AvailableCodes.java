package org.mplugins.customermanager.commands;

import com.google.common.base.Joiner;
import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mplugins.customermanager.utils.VerificationManager;

import java.util.List;

public class AvailableCodes implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        VerificationManager verificationManager = VerificationManager.getInstance();
        List<String> codes = verificationManager.getAvailableCodes();
        ComponentBuilder builder = new ComponentBuilder("§7Available codes: ");

        for (String code : codes)
        {
            TextComponent codeComponent = new TextComponent("§e" + code);
            codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy to clipboard!")));
            codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code));
            builder.append(codeComponent).append(" ");
        }

        sender.sendMessage(builder.create());
        return true;
    }
}
