package org.WHDB;

import java.sql.Connection;

import org.WHDB.APIs.Produto;
import org.WHDB.Postgres.conexao;

public class Main {
    public static void main(String[] args) {

        //boolean sucessoInserir = Produto.inserirProduto("Batata",11,6.6);

        for (int i = 0; i != 5; i ++ ){
            boolean sucessoInserir = Produto.inserirProduto("Batata",11,6.6);
        }

        //boolean sucessoAPI = org.WHDB.APIs.Produto.inserirImagem(1, "C:\\Users\\joaop\\Downloads/d.jpg");
        //String sucesso = Produto.listarProdutoModel(1);
    }

}