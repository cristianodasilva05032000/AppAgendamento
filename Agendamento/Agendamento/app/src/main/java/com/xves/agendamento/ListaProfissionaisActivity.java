package com.xves.agendamento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaProfissionaisActivity extends AppCompatActivity {

    BaseDados database;
    RecyclerView recyclerView;
    ConsultaProfissionaisAdapter recycler;
    List<UserModel> datamodel;
    SharedPreferences prefs;
    TextView txtInfo, txtTitle;
    String esp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissionais);

        prefs = getSharedPreferences("PREFUSUARIO", 0);
        Intent i = getIntent();
        esp = i.getStringExtra("especialidade");
        datamodel = new ArrayList<>();
        recyclerView = findViewById(R.id.ListaClientes);
        txtTitle = findViewById(R.id.txtTitle);
        txtInfo = findViewById(R.id.txtinfo);
        EditText editTextSearch = null;

        database = new BaseDados(this);
        datamodel = getProfissionais();
        recycler = new ConsultaProfissionaisAdapter(datamodel, this);

        RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
        recyclerView.setItemViewCacheSize(datamodel.size());

    }

    public List<UserModel> getProfissionais() {
        List<UserModel> data = new ArrayList<>();
        SQLiteDatabase db = database.getWritableDatabase();
        prefs = getSharedPreferences("PREFUSUARIO", 0);
        Cursor cursor = db.rawQuery("SELECT * FROM xves_user U, " +
                "               xves_user_specialty S   " +
                " WHERE U.tipo = 'P'" +
                " AND U.id = S.id_usuario" +
                " AND S.id_especialidade = ?" +
                " ORDER BY nome ASC", new String[]{esp});
        StringBuffer stringBuffer = new StringBuffer();
        UserModel dataModel = null;
        while (cursor.moveToNext()) {
            txtInfo.setVisibility(View.INVISIBLE);
            dataModel = new UserModel();
            String id_usuario = cursor.getString(cursor.getColumnIndexOrThrow("id_usuario"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            // Removido o campo "email" da consulta
            // String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow("cpf"));
            String fone = cursor.getString(cursor.getColumnIndexOrThrow("fone"));
            String preco = cursor.getString(cursor.getColumnIndexOrThrow("preco"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
            String endereco = cursor.getString(cursor.getColumnIndexOrThrow("endereco"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            txtTitle.setText(descricao);
            dataModel.setId(id_usuario);
            dataModel.setNome(nome);
            // Removido o campo "email" da inserção nos dados do modelo
            // dataModel.setEmail(email);
            dataModel.setCpf(cpf);
            dataModel.setFone(fone);
            dataModel.setPreco(preco);
            dataModel.setTipo(tipo);
            dataModel.setEndereco(endereco);
            dataModel.setDescricao(descricao);
            stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        cursor.close();
        db.close();
        return data;
    }

}
