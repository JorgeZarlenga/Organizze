package com.jorge.organizze.activity.config;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao; // static define que o atributo será o mesmo
    // independente de quantas instâncias se crie da classe
    private static DatabaseReference firebase;

    // Método que retorna a instância do FirebaseDatabase:
    public static DatabaseReference getFirebaseDatabase()
    {
        if(firebase == null)
        {
            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return firebase;
    }

    // Método que retorna a instância do FirebaseAuth:
    public static FirebaseAuth getFirebaseAutenticacao()
    {
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;

    }
}
