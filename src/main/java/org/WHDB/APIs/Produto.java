package org.WHDB.APIs;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.WHDB.Postgres.conexao;

public class Produto {

    public static boolean inserirProduto(String nome, int quantidade_estoque, double preco) {
        String sql = "insert into produto (nome, quantidade_estoque, preco) values (?, ?, ?)";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (con == null) {
                System.err.println("Erro: conexão nula");
                return false;
            }

            stmt.setString(1, nome);
            stmt.setInt(2, quantidade_estoque);
            stmt.setDouble(3, preco);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir item: " + e.getMessage());
            return false;
        }
    }

    public static boolean inserirImagem(int idProduto, String caminhoImagem) {
        String sql = "update produto set imagem = ? where id = ?}";

        File arquivo = new File(caminhoImagem);

        if (!arquivo.exists()) {
            System.err.println("Arquivo não encontrado: " + caminhoImagem);
            return false;
        }

        try (
                Connection con = conexao.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                FileInputStream fis = new FileInputStream(arquivo)
        ) {
            stmt.setBinaryStream(1, fis, (int) arquivo.length());
            stmt.setInt(2, idProduto);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("Erro ao inserir imagem: " + e.getMessage());
            return false;
        }
    }
    public static byte[] buscarImagemPorId(int idProduto) {
        String sql = "SELECT imagem FROM produto WHERE id = ?";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("imagem");
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar imagem: " + e.getMessage());
        }
        return null;
    }

    public static List<produtomodel> listarProdutos() {
        List<produtomodel> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, preco, descricao FROM produto";

        try (Connection con = conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produtomodel p = new produtomodel();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getDouble("preco"));
                p.setDescricao(rs.getString("descricao"));
                produtos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }
}

