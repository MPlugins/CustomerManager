package org.mplugins.customermanager.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mplugins.configuration.MySQL;
import org.mplugins.customermanager.CustomerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class GenerateCodeCommand implements CommandExecutor
{
    private static final Random random = new Random();
    private static final CustomerManager plugin = CustomerManager.getInstance();
    private static final MySQL mysql = plugin.getMysql();
    private static final int CODE_LENGTH = 5;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
        {
            String code = generateNewCode();

            while (doesCodeExist(code))
                code = generateNewCode();

            TextComponent codeComponent = new TextComponent("§7Generated new code: §e" + code);
            codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to copy to clipboard!")));
            codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code));

            sender.sendMessage(codeComponent);

            try (Statement statement = mysql.createStatement())
            {
                statement.execute("INSERT INTO codes (code) VALUES ('" + code + "');");
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });

        return true;
    }

    private String generateNewCode()
    {
        char[] validCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9'};
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++)
            stringBuilder.append(validCharacters[random.nextInt(validCharacters.length)]);

        return stringBuilder.toString();
    }

    private boolean doesCodeExist(String code)
    {
        String sql = "SELECT COUNT(*) count FROM codes WHERE code = '" + code + "';";

        try (Statement statement = mysql.createStatement())
        {
            ResultSet result = statement.executeQuery(sql);

            return result.first() && result.getInt("count") != 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
