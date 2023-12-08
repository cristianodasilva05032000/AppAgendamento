package com.xves.agendamento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ServicosActivity extends AppCompatActivity {

    LinearLayout ll1, ll2, ll3, ll4, ll5;
    SharedPreferences prefs;
    ImageView img1, img2;
    String id_user; // Alterado de "id" para "id_user"
    Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        btnSair = findViewById(R.id.btnSair);

        prefs = getSharedPreferences("PREFUSUARIO", 0);
        id_user = prefs.getString("id_user", ""); // Corrigido para "id_user" conforme definido no LoginActivity
        boolean logado = prefs.getBoolean("LOGADO", false);

        if (logado) {
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
        }

        ll1.setOnClickListener(v -> {
            Intent i = new Intent(this, ListaProfissionaisActivity.class);
            i.putExtra("especialidade", "1");
            startActivity(i);
        });

        ll2.setOnClickListener(v -> {
            if (logado) {
                Intent i = new Intent(this, ListaProfissionaisActivity.class);
                i.putExtra("especialidade", "2");
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Faça o cadastro para acessar!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
        });

        ll3.setOnClickListener(v -> {
            Intent i = new Intent(this, ListaProfissionaisActivity.class);
            i.putExtra("especialidade", "3");
            startActivity(i);
        });

        ll4.setOnClickListener(v -> {
            if (logado) {
                Intent i = new Intent(this, ListaAgendamentoActivity.class);
                // i.putExtra("id_profissional", id_profissional);
                // i.putExtra("id_serv", id_serv);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Faça o cadastro para acessar!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
        });

        ll5.setOnClickListener(v -> {
            Intent i = new Intent(this, AgradecimentoActivity.class);
            // i.putExtra("id_profissional", id_profissional);
            // i.putExtra("id_serv", id_serv);
            startActivity(i);
        });

        btnSair.setOnClickListener(v -> {
            prefs.edit().remove("id_user").apply(); // Alterado para "id_user"
            prefs.edit().remove("LOGADO").apply();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });
    }

}
