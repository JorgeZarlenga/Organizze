package com.jorge.organizze.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.jorge.organizze.activity.helper.Base64Custom;
import com.jorge.organizze.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button buttonCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validações:

                String textNome = campoNome.getText().toString();
                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                // Verificação de preenchimento dos campos:

                if (!textNome.isEmpty())
                {
                    if(!textEmail.isEmpty())
                    {
                        if(!textSenha.isEmpty())
                        {
                            usuario = new Usuario();
                            usuario.setNome(textNome);
                            usuario.setEmail(textEmail);
                            usuario.setSenha(textSenha);
                            cadastrarUsuario();
                        }
                        else
                        {
                            Toast.makeText(CadastroActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(CadastroActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CadastroActivity.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cadastrarUsuario()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // Para verificar o sucesso do cadastro
                if (task.isSuccessful()) {

                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    Log.e("SALVAMENTO", "Salvamento sucesso");
                    finish(); // Usado para fechar e manter uma validação automática


                } else {
                    String excecao = "";
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        excecao = "Digite uma senha mais forte!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        excecao = "Por favor, digite um email válido!";
                    }
                    catch (FirebaseAuthUserCollisionException e) // Usado para quando o cadastro já existe
                    {
                        excecao = "Essa conta já foi cadastrada!";
                    }
                    catch (Exception e) // Exceção genérica
                    {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace(); // Print da variável no log
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}