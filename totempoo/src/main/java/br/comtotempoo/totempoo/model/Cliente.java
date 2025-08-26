package br.comtotempoo.totempoo.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;

    public Cliente() {}

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
