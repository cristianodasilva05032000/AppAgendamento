package com.xves.agendamento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConsultaProfissionaisAdapter extends RecyclerView.Adapter<ConsultaProfissionaisAdapter.HolderClientes> {
    List<UserModel> list;
    Context ctx;
    Activity activity;
    SharedPreferences prefs;
    public ConsultaProfissionaisAdapter(List<UserModel> list, Context context) {
        this.list = list;
        this.ctx = context;
        this.activity = (Activity) context;
    }

    class HolderClientes extends RecyclerView.ViewHolder {
        TextView nome, preco;
        LinearLayout btnCard;
        LinearLayout info;

        public HolderClientes(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNome);
            preco = itemView.findViewById(R.id.txtPreco);
            btnCard = itemView.findViewById(R.id.btnCard);

        }
    }

    @Override
    public HolderClientes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profissional, null);
        return new HolderClientes(view);
    }

    @Override
    public void onBindViewHolder(HolderClientes holder, int position) {
        UserModel dataModel = list.get(position);
        holder.nome.setText(dataModel.getNome());
        holder.preco.setText(dataModel.getPreco());

        prefs = activity.getSharedPreferences("PREFUSUARIO", 0);
     //   String id = prefs.getString("id", "");


        holder.btnCard.setOnClickListener(v -> {
            prefs = activity.getSharedPreferences("PREFUSUARIO", 0);
            boolean logado = prefs.getBoolean("LOGADO", false);
            if (logado) {
                Intent i = new Intent(activity, AgendamentoActivity.class);
                i.putExtra("id_profissional", dataModel.getId());
                i.putExtra("endereco", dataModel.getEndereco());
                i.putExtra("valor", dataModel.getPreco());
                i.putExtra("descricao", dataModel.getDescricao());
                activity.startActivity(i);
                activity.finish();
            } else {
                Toast.makeText(activity, "Faça o cadastro para acessar!", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(activity, LoginActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
