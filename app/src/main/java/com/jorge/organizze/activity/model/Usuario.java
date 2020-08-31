package com.jorge.organizze.activity.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;

public class Usuario {

    private String idUsuario; // Desconsidera para salvar no Firebase (Exclude)
    private String nome;
    private String email;
    private String senha; // Desconsidera para salvar no Firebase (Exclude)
    private Double receitaTotal = 0.00;
    private Double despesaTotal = 0.00;

    // Gerar construtor vazio para configurar os atributos diretamente no objeto, sem passar os
    // parâmetros para o construtor

    public Usuario() {
    }

    // Método que salva o usuário:
    public void salvar()
    {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase(); // Objeto que permite salvar os dados no Firebase

        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this); // This identifica o objeto Usuario
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }
// Gerar getter and setter:

    @Exclude // Anotação do Firebase
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    @Exclude // Anotação do Firebase
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
