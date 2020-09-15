package com.jorge.organizze.activity.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaldo, textoSaudacao;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar); // Método importante para que a toolbar funcione corretamente
                                      // em versões anteriores do Android


        textoSaldo = findViewById(R.id.textSaldo);
        textoSaldo = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        configurarCalendarView();

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
    }

    // Sobreescrevendo o método onCreate:

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); // menuInflater é o objeto que converte o XML em View
        return super.onCreateOptionsMenu(menu);
    }

    // Método para tratar a opção selecionada:

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuSair:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish(); // Finaliza a activity principal
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view)
    {
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view)
    {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void configurarCalendarView()
    {
        // Configurando valores para os meses:
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses); // Array do tipo char sequence

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }
}