package com.xves.agendamento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtCPF;
    EditText edtSenha;
    Button btnEntrar;
    SharedPreferences prefs;
    BaseDados base;
    LinearLayout btn_entrar_sem_login, btn_cadastrar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        base = new BaseDados(this);
        prefs = getSharedPreferences("PREFUSUARIO", 0);
        boolean logado = prefs.getBoolean("LOGADO", false);
        if (logado) {
            Intent i = new Intent(this, ServicosActivity.class);
            startActivity(i);
            // Removido o finish() para permitir a navegação de volta para esta atividade.
        }

        edtCPF = findViewById(R.id.edtCPF);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_entrar_sem_login = findViewById(R.id.btn_entrar_sem_login);
        prefs = getSharedPreferences("PREFUSUARIO", 0);

        // Adiciona um TextWatcher para formatar o CPF enquanto o usuário digita
        edtCPF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cpf = editable.toString().replaceAll("[^\\d]", "");

                if (cpf.length() == 11) {
                    cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
                }

                edtCPF.removeTextChangedListener(this);
                edtCPF.setText(cpf);
                edtCPF.setSelection(cpf.length());
                edtCPF.addTextChangedListener(this);
            }
        });

        btnEntrar.setOnClickListener(v -> {
            String cpf = edtCPF.getText().toString().replaceAll("[^\\d]", ""); // Remover pontos e traços do CPF
            String senha = edtSenha.getText().toString();

            SQLiteDatabase db = base.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM xves_user WHERE cpf=? AND senha=? LIMIT 1", new String[]{cpf, senha});

            if (c.moveToNext()) {
                String id = String.valueOf(c.getInt(0));
                String nome = c.getString(c.getColumnIndexOrThrow("nome"));
                String tipo = c.getString(c.getColumnIndexOrThrow("tipo"));

                if (tipo.equals("C")) {
                    SharedPreferences.Editor ed = prefs.edit();
                    ed.putBoolean("LOGADO", true).apply();
                    ed.putString("id_user", id).apply();
                    Intent i = new Intent(this, ServicosActivity.class);
                    startActivity(i);
                    // Removido o finish() para permitir a navegação de volta para esta atividade.
                } else {
                    SharedPreferences.Editor ed = prefs.edit();
                    ed.putString("id_user", id).apply();
                    Intent i = new Intent(this, ListaAgendamentoProfissionalActivity.class);
                    startActivity(i);
                    // Removido o finish() para permitir a navegação de volta para esta atividade.
                }
            } else {
                Toast.makeText(getApplicationContext(), "Usuário não encontrado!", Toast.LENGTH_LONG).show();
            }
        });

        btn_cadastrar.setOnClickListener(v -> {
            Intent i = new Intent(this, CadastroActivity.class);
            startActivity(i);
            // Removido o finish() para permitir a navegação de volta para esta atividade.
        });

        btn_entrar_sem_login.setOnClickListener(v -> {
            startActivity(new Intent(this, ServicosActivity.class));
            // Removido o finish() para permitir a navegação de volta para esta atividade.
        });
    }
}
