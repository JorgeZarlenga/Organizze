package com.jorge.organizze.activity.helper;

import android.util.Base64;

public class Base64Custom {

    // Criação de métodos estáticos para não ser preciso instanciá-los:
    public static String codificarBase64(String texto)
    {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", ""); //getBytes - Array de bytes, codificação DEFAULT .replaceAll (valor que se quer substituir, valor que será substituído)
                                                                                                                        // \\n é quebra de linha e \\n é retorno de carro (caracteres inválidos)
    }

    public static String decodificarBase64(String textoCodificado)
    {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT)); // Para converter para string
    }
}
