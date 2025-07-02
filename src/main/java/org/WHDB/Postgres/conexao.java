package org.WHDB.Postgres;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class conexao {
    private static final String URL = "jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres";
    private static final String USUARIO = "postgres.gfuwwgxtdewrxrpmvpvj";
    private static final String SENHA = "Warehouse23102005cuidado";

    public static Connection conectar() {
        try {
            return (Connection) DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
}