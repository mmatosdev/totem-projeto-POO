package br.comtotempoo.totempoo.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String nome;
    private String tipo;
    private double preco;

    public Produto() {}

    public Produto(Long id, String nome, String tipo, double preco) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
