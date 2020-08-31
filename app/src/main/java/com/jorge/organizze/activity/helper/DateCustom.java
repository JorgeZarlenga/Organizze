package com.jorge.organizze.activity.helper;

import java.text.SimpleDateFormat;

public class DateCustom
{
    public static String dataAtual()
    {
        long data = System.currentTimeMillis(); // Data do dispositivo
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy"); // Padrão personalizado com caracteres coringas (M = Meses, m = minutos)
        String dataString = simpleDateFormat.format(data); // Aplica o padrão
        return dataString;
    }
}
