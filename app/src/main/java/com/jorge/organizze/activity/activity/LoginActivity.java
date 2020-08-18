package com.jorge.organizze.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.jorge.organizze.activity.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button buttonEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonEntrar = findViewById(R.id.buttonEntrar);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                if(!textEmail.isEmpty())
                {
                    if(!textSenha.isEmpty())
                    {
                        usuario = new Usuario();
                        usuario.setEmail(textEmail);
                        usuario.setSenha(textSenha);
                        validarLogin();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    abrirTelaPrincipal();
                }
                else
                {
                    String excecao = "";
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e) // Exceção para email inválido ou desabilitado
                    {
                        excecao = "Usuário não está cadastrado!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) // Exceção para senha errada
                    {
                        excecao = "Email e senha não correspondem a um usuário cadastrado!";
                    }
                    catch (Exception e) // Exceção genérica
                    {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace(); // Print da variável no log
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal()
    {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish(); // Para fechar a Activity de login
    }
}