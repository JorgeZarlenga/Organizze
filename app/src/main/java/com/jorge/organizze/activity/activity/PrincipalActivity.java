package com.jorge.organizze.activity.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jorge.organizze.R;
import com.jorge.organizze.activity.adapter.MovimentoAdapter;
import com.jorge.organizze.activity.config.ConfiguracaoFirebase;
import com.jorge.organizze.activity.helper.Base64Custom;
import com.jorge.organizze.activity.model.Movimentacao;
import com.jorge.organizze.activity.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaldo, textoSaudacao;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private MovimentoAdapter movimentoAdapter;
    private List<Movimentacao> listaMovimentos = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar); // Método importante para que a toolbar funcione corretamente
                                      // em versões anteriores do Android

        // Identificação dos itens em tela:
        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);
        configurarCalendarView();
        swipe();

        // Configuração do Adapter:
        movimentoAdapter = new MovimentoAdapter(listaMovimentos, this);

        // Configuração do RecyclerView:
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movimentoAdapter);
    }

    public void swipe()
    {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) { // Configura como deve ser o movimento
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE; // Desabilita o drag multidirecional
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags); //2 parâmetros: flags de drag e de swipe
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Log.i("swipe", "Item foi arrastado");
            }
        };

        // Anexando ao RecyclerView:

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }
    public void recuperarMovimentacoes()
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        // Movimentação de referência:
        movimentacaoRef = firebaseRef.child("movimentacao")
                                     .child(idUsuario)
                                     .child(mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMovimentos.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()) // Recupera todos os filhos, percorrendo-os
                {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class); // Isso recupera uma movimentação inteira a cada iteração

                    listaMovimentos.add(movimentacao);
                }

                movimentoAdapter.notifyDataSetChanged(); // Método que notifica que os dados foram atualizados
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumo()
    {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        Log.i("Evento", "evento foi adicionado");

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() { // Definindo o listener ao objeto
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##"); // Representa o dígito mas desconsidera o 0
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText(resultadoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // Sobreescrevendo o método onCreateOptionsMenu:

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

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", dataAtual.getMonth()); // % é caractere coringa
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear()); // Convertendo um inteiro para string
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", date.getMonth()); // % é caractere coringa
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear()); // Convertendo um inteiro para string
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes(); // A cada mudança de mês, recupera as movimentações deste (mas anexa um evento para as movimentações, por isso o removeListener)
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    // Sobreescrevendo o método onStop (quando o aplicativo não estiver sendo utilizado) para evitar
    // consumo de recursos:


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "evento foi removido");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}