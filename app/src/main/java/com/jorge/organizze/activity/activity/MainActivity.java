package com.jorge.organizze.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        // Para criar o slider usando fragment:
        // Criar new resource file em layout (intro_1), com root como LinearLayout
        // Fazer o mesmo com intro_2

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .build()
        );

        /*
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new SimpleSlide.Builder()
                                .title("Título")
                                .description("Descrição")
                                .image(R.drawable.um)
                                .background(android.R.color.holo_orange_light)
                                .build()
        );

        addSlide(new SimpleSlide.Builder()
                                .title("Título 2")
                                .description("Descrição 2")
                                .image(R.drawable.dois)
                                .background(android.R.color.holo_orange_light)
                                .build()
        );

        addSlide(new SimpleSlide.Builder()
                .title("Título 3")
                .description("Descrição 3")
                .image(R.drawable.tres)
                .background(android.R.color.holo_orange_light)
                .build()
        );

         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void btEntrar(View view)
    {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view)
    {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void verificarUsuarioLogado()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null)
        {
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal()
    {
        startActivity(new Intent(this, PrincipalActivity.class));

    }
}