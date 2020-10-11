package com.jorge.organizze.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jorge.organizze.R;
import com.jorge.organizze.activity.model.Movimentacao;

import java.util.List;

public class MovimentoAdapter extends RecyclerView.Adapter<MovimentoAdapter.MyViewHolder> {

    private List<Movimentacao> listaMovimentacoes;
    Context context;

    // Construtor para a lista de movimentações:

    public MovimentoAdapter(List<Movimentacao> listaMovimentacoes, Context context) {
        this.listaMovimentacoes = listaMovimentacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movimentoLista = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(movimentoLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movimentacao movimentacao = listaMovimentacoes.get(position); // Captura a posição

        holder.textAdapterTitulo.setText(movimentacao.getDescricao());
        holder.textAdapterValor.setText(String.valueOf(movimentacao.getValor())); // Fazendo o casting de texto para String
        holder.textAdapterCategoria.setText(movimentacao.getCategoria());
        holder.textAdapterValor.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));

        if(movimentacao.getTipo().equals("d"))
        {
            holder.textAdapterValor.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.textAdapterValor.setText("-" + movimentacao.getValor());
        }
    }

    @Override
    public int getItemCount() {
        return this.listaMovimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        // Criação dos atributos:
        TextView textAdapterTitulo, textAdapterValor, textAdapterCategoria;

        // Construtor criado:
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Identificação dos itens em tela:
            textAdapterTitulo = itemView.findViewById(R.id.textAdapterTitulo);
            textAdapterValor = itemView.findViewById(R.id.textAdapterValor);
            textAdapterCategoria = itemView.findViewById(R.id.textAdapterCategoria);
        }
    }
}
