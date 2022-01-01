package org.mplugins.customermanager.utils;

import org.mplugins.configuration.MySQL;
import org.mplugins.customermanager.CustomerManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class VerificationManager
{
    private static final HashMap<UUID, Boolean> verifiedCache = new HashMap<>();
    private static final MySQL mysql = CustomerManager.getInstance().getMysql();
    private static VerificationManager instance;

    public void verify(UUID uuid, String code)
    {
        String sql = "INSERT INTO customers (uuid, codeId) VALUES ('" + uuid.toString() + "', (SELECT id FROM codes WHERE code = '" + code + "'));";

        try (Statement statement = mysql.createStatement())
        {
            statement.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        verifiedCache.put(uuid, true);
    }

    public boolean isCodeValid(String code)
    {
        String sql = "SELECT COUNT(*) count FROM codes WHERE code = '" + code + "' AND code NOT IN (SELECT code FROM customers NATURAL JOIN codes);";

        try (Statement statement = mysql.createStatement();
             ResultSet result = statement.executeQuery(sql))
        {
            result.next();

            return result.getInt("count") != 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isVerified(UUID uuid)
    {
        if (verifiedCache.containsKey(uuid))
            return verifiedCache.get(uuid);

        String sql = "SELECT COUNT(*) count FROM customers WHERE uuid = '" + uuid.toString() + "';";

        try (Statement statement = mysql.createStatement();
             ResultSet result = statement.executeQuery(sql))
        {
            boolean verified = result.next() && result.getInt("count") != 0;
            verifiedCache.put(uuid, verified);

            return verified;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private VerificationManager()
    {
    }

    public static VerificationManager getInstance()
    {
        if (instance == null)
            instance = new VerificationManager();

        return instance;
    }
}
