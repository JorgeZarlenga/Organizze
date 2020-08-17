package com.jorge.organizze.activity.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao; // static define que o atributo será o mesmo
    // independente de quantas instância se crie da classe

    // Retorna a instância do FirebaseAuth:
    public static FirebaseAuth getFirebaseAutenticacao()
    {
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;

    }
}
