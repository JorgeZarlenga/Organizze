package com.jorge.organizze.activity.model;

public class Usuario {
    private String nome;
    private String email;
    private String senha;

    // Gerar construtor vazio para configurar os atributos diretamente no objeto, sem passar os
    // parâmetros para o construtor

    public Usuario() {
    }

    // Gerar getter and setter:


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
