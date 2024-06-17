package com.mycompany.app.Repositories;

import java.sql.*;
import com.mycompany.app.Util.DotEnv;

public class BaseRepository 
{
    protected DotEnv dotEnv;
    protected Connection conn;

    public BaseRepository()
    {
        try {
            dotEnv = new DotEnv(".env");

            String dbUrl = String.format(
                "jdbc:mysql://%s:%s/%s",
                dotEnv.get("DB_HOST"),
                dotEnv.get("DB_PORT"),
                dotEnv.get("DB_NAME")
            );

            conn = DriverManager.getConnection(
                dbUrl,
                dotEnv.get("DB_USER"),
                dotEnv.get("DB_PASS")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
