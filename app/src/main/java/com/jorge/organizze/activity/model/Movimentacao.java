package com.jorge.organizze.activity.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.jorge.organizze.activity.helper.Base64Custom;
import com.jorge.organizze.activity.helper.DateCustom;

public class Movimentacao
{
    private String Data;
    private String Categoria;
    private String descricao;
    private String tipo;
    private double valor;

    // Construtor vazio:

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida)
    {
        // Recuperando o email do usuário:
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push() // Criação do id único do Firebase
                .setValue(this); // Salva todos os dados
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
