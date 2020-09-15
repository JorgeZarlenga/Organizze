package com.jorge.organizze.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.jorge.organizze.activity.helper.Base64Custom;
import com.jorge.organizze.activity.helper.DateCustom;
import com.jorge.organizze.activity.model.Movimentacao;
import com.jorge.organizze.activity.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoDataReceita, campoCategoriaReceita, campoDescricaoReceita;
    private EditText campoValorReceita;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    Double receitaTotal; // Receita acumulada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValorReceita = findViewById(R.id.editValorReceita);
        campoDataReceita = findViewById(R.id.editDataReceita);
        campoCategoriaReceita = findViewById(R.id.editCategoriaReceita);
        campoDescricaoReceita = findViewById(R.id.editDescricaoReceita);

        // Preenche o campo data com a data atual:
        campoDataReceita.setText(DateCustom.dataAtual()); // dataAtual é um método estático, não sendo necessário instanciar
        recuperarReceitaTotal();
    }

    public void salvarReceita(View view)
    {
        if(validarCamposReceita())
        {
            movimentacao = new Movimentacao();
            String data = campoDataReceita.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValorReceita.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoriaReceita.getText().toString());
            movimentacao.setDescricao(campoDescricaoReceita.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            finish(); // Fecha a tela
        }

    }

    public Boolean validarCamposReceita()
    {
        String textoValor = campoValorReceita.getText().toString();
        String textoData = campoDataReceita.getText().toString();
        String textoCategoria = campoCategoriaReceita.getText().toString();
        String textoDescricao = campoDescricaoReceita.getText().toString();

        if(!textoValor.isEmpty())
        {
            if(!textoData.isEmpty())
            {
                if(!textoCategoria.isEmpty())
                {
                    if(!textoDescricao.isEmpty())
                    {
                        return true; // Validando todas as condições, indica que todos os campos foram preenchidos
                    }
                    else
                    {
                        Toast.makeText(ReceitasActivity.this, "Descrição não foi preenchida", Toast.LENGTH_SHORT).show();
                        return false; // Caso contrário, retorna false
                    }
                }
                else
                {
                    Toast.makeText(ReceitasActivity.this, "Categoria não foi preenchida", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(ReceitasActivity.this, "Data não foi preenchida", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        else
        {
            Toast.makeText(ReceitasActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal()
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        // Adicionando ouvinte:

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Usuario usuario = dataSnapshot.getValue(Usuario.class); // Baseado na classe de modelo
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita)
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}

