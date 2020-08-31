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

    // Método que transforma a data somente em números (mês e ano):

    public static String mesAnoDataEscolhida(String data)
    {
        String retornoData[] = data.split("/"); // Separa os valores e os coloca em índices de array
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano; // Concatenando o mês e ano
        return mesAno;
    }
}
