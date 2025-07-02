package org.WHDB.APIs;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "produto")
public class produtomodel {

    @Id
    private int id;

    private String nome;
    private double preco;
    private String descricao;

    @Lob
    @Column(name = "imagem", columnDefinition = "bytea")
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore // não envia o byte[] no JSON
    private byte[] imagem;

    public produtomodel() {}

    public produtomodel(int id, String nome, double preco, String descricao, byte[] imagem) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public byte[] getImagem() { return imagem; }
    public void setImagem(byte[] imagem) { this.imagem = imagem; }
}
