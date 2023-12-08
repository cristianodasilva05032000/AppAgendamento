package com.xves.agendamento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaAgendamentoActivity extends AppCompatActivity {

    BaseDados database;
    RecyclerView recyclerView;
    ConsultaServicoAdapter recycler;
    List<ServiceModel> datamodel;
    SharedPreferences prefs;
    TextView txtInfo;
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agendamento);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        prefs = getSharedPreferences("PREFUSUARIO", 0);
        datamodel = new ArrayList<>();
        recyclerView = findViewById(R.id.ListaClientes);
        txtInfo = findViewById(R.id.txtinfo);

        database = new BaseDados(this);
        datamodel = getServicos();
        recycler = new ConsultaServicoAdapter(datamodel, this);

        RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
        recyclerView.setItemViewCacheSize(datamodel.size());
        }



    public List<ServiceModel> getServicos() {
        List<ServiceModel> data = new ArrayList<>();
        SQLiteDatabase db = database.getWritableDatabase();
        prefs = getSharedPreferences("PREFUSUARIO", 0);
        id_user = prefs.getString("id_user", "");
        Cursor cursor = db.rawQuery("SELECT * FROM xves_service WHERE id_cliente = ?", new String[]{id_user});
        StringBuffer stringBuffer = new StringBuffer();
        ServiceModel dataModel = null;
        while (cursor.moveToNext()) {
            txtInfo.setVisibility(View.INVISIBLE);
            dataModel = new ServiceModel();
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            String endereco = cursor.getString(cursor.getColumnIndexOrThrow("endereco"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
            String valor = cursor.getString(cursor.getColumnIndexOrThrow("valor"));
            dataModel.setId(id);
            dataModel.setDescricao(descricao);
            dataModel.setEndereco(endereco);
            dataModel.setData(date);
            dataModel.setHora(hora);
            dataModel.setValor(valor);
            stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        cursor.close();
        db.close();
        return data;
    }
}
