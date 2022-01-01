package org.mplugins.customermanager;

import org.mplugins.MPlugins;
import org.mplugins.configuration.MySQL;
import org.mplugins.customermanager.commands.GenerateCodeCommand;
import org.mplugins.customermanager.commands.VerifyCommand;
import org.mplugins.customermanager.listeners.Listeners;

import java.sql.SQLException;
import java.sql.Statement;

public final class CustomerManager extends MPlugins
{
    private MySQL mysql;

    @Override
    public void onStart()
    {
        this.prepareMySQL();

        getCommand("verify").setExecutor(new VerifyCommand());
        getCommand("generatecode").setExecutor(new GenerateCodeCommand());
        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onStop()
    {
        mysql.disconnect();
    }

    private void prepareMySQL()
    {
        String username = getPluginOptions().getString("mysql.username");
        String password = getPluginOptions().getString("mysql.password");
        String database = getPluginOptions().getString("mysql.database");
        String host = getPluginOptions().getString("mysql.host");

        mysql = new MySQL(username, password, host, database);

        try (Statement statement = mysql.createStatement())
        {
            statement.execute("CREATE TABLE IF NOT EXISTS codes (id INT PRIMARY KEY AUTO_INCREMENT, code TEXT NOT NULL UNIQUE);");
            statement.execute("CREATE TABLE IF NOT EXISTS customers (id INT PRIMARY KEY AUTO_INCREMENT, uuid TEXT UNIQUE, codeId INT, CONSTRAINT FOREIGN KEY (codeId) REFERENCES codes(id));");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public MySQL getMysql()
    {
        return mysql;
    }

    public static CustomerManager getInstance()
    {
        return getPlugin(CustomerManager.class);
    }
}
