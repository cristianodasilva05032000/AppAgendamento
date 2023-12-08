package com.xves.agendamento;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    EditText edtNome;
    EditText edtCPF;
    EditText edtFone;
    EditText edtSenha;
    Button btnSalvar;
    SharedPreferences prefs;
    ImageView imgBack;
    BaseDados base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        base = new BaseDados(this);

        edtNome = findViewById(R.id.edtNome);
        edtCPF = findViewById(R.id.edtCPF);
        edtFone = findViewById(R.id.edtFone);
        edtSenha = findViewById(R.id.edtSenha);
        btnSalvar = findViewById(R.id.btnSalvar);
        prefs = getSharedPreferences("PREFUSUARIO", 0);
        imgBack = findViewById(R.id.imgBack);

        // Adiciona um filtro para aceitar apenas letras e espaços no campo de nome
        InputFilter nameFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                        return ""; // Remove caracteres não permitidos
                    }
                }
                return null; // Aceita o caractere
            }
        };

        edtNome.setFilters(new InputFilter[]{nameFilter});

        // Adiciona um TextWatcher para formatar o número de telefone
        edtFone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                String phone = editable.toString().replaceAll("[^\\d]", "");

                if (phone.length() >= 2) {
                    phone = "(" + phone.substring(0, 2) + ")" + phone.substring(2);
                }

                edtFone.removeTextChangedListener(this);
                edtFone.setText(phone);
                edtFone.setSelection(phone.length());
                edtFone.addTextChangedListener(this);
            }
        });

        // Adiciona um TextWatcher para formatar o CPF
        edtCPF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
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

        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String cpf = edtCPF.getText().toString().replaceAll("[^\\d]", "");
            String fone = edtFone.getText().toString().replaceAll("[^\\d]", "");
            String senha = edtSenha.getText().toString().trim();

            if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(fone)) {
                Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_LONG).show();
            } else if (senha.length() < 4 || senha.length() > 12) {
                Toast.makeText(getApplicationContext(), "A senha deve ter entre 4 e 12 caracteres.", Toast.LENGTH_LONG).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("nome", nome);
                values.put("cpf", cpf);
                values.put("fone", fone);
                values.put("senha", senha);
                values.put("tipo", "C");
                SQLiteDatabase db = base.getWritableDatabase();

                long newRowId = db.insertWithOnConflict("xves_user", null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (newRowId == -1) {
                    Toast.makeText(getApplicationContext(), "Erro ao cadastrar!", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        imgBack.setOnClickListener(marker -> {
            onBackPressed();
        });
    }
}
