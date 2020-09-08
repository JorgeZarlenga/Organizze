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

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    Double despesaTotal; // Despesa acumulada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValorReceita);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        // Preenche o campo data com a data atual:
        campoData.setText(DateCustom.dataAtual()); // dataAtual é um método estático, não sendo necessário instanciar
        recuperarDespesaTotal();
    }

    public void salvarDespesa(View view)
    {
        if(validarCamposDespesa())
        {
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            Double despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(data);
        }

    }

    public Boolean validarCamposDespesa()
    {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

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
                        Toast.makeText(DespesasActivity.this, "Descrição não foi preenchida", Toast.LENGTH_SHORT).show();
                        return false; // Caso contrário, retorna false
                    }
                }
                else
                {
                    Toast.makeText(DespesasActivity.this, "Categoria não foi preenchida", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(DespesasActivity.this, "Data não foi preenchida", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        else
        {
            Toast.makeText(DespesasActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarDespesaTotal()
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
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa)
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}
