package com.xves.agendamento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsultaServicoProfissionalAdapter extends RecyclerView.Adapter<ConsultaServicoProfissionalAdapter.HolderServico> {
    List<ServiceModel> list;
    Context ctx;
    Activity activity;
    SharedPreferences prefs;
    public ConsultaServicoProfissionalAdapter(List<ServiceModel> list, Context context) {
        this.list = list;
        this.ctx = context;
        this.activity = (Activity) context;
    }

    class HolderServico extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtEndereco, txtData, txtHora, txtPreco;
        LinearLayout btnCard;
        LinearLayout btnCancelar;

        public HolderServico(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtData = itemView.findViewById(R.id.txtData);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            btnCard = itemView.findViewById(R.id.btnCard);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }

    @Override
    public HolderServico onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servico, null);
        return new HolderServico(view);
    }

    @Override
    public void onBindViewHolder(HolderServico holder, int position) {
        ServiceModel dataModel = list.get(position);
        holder.txtDescricao.setText(dataModel.getDescricao());
        holder.txtEndereco.setText(dataModel.getEndereco());

        String dataString = dataModel.getData();
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
            Date data = formatoEntrada.parse(dataString);
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatoSaida.format(data);
            holder.txtData.setText(dataFormatada);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txtHora.setText(dataModel.getHora());
        holder.txtPreco.setText(dataModel.getValor());

        prefs = activity.getSharedPreferences("PREFUSUARIO", 0);

        holder.btnCancelar.setOnClickListener(v -> {
            AlertDialog.Builder mensagem = new AlertDialog.Builder(activity);
            mensagem.setMessage("Deseja Realmente Cancelar? ");
            mensagem.setCancelable(false);
            mensagem.setPositiveButton("Sim", (dialog, id) -> {
                        BaseDados database = new BaseDados(activity);
                        SQLiteDatabase db = database.getWritableDatabase();
                        Cursor c = db.rawQuery("DELETE FROM xves_service WHERE id = " + dataModel.getId() + "", null);
                        if (c.getCount() <= 0) {
                            Toast.makeText(ctx, "Cancelado com Sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ctx, "Erro ao Cancelar!", Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                        c.close();
                        activity.startActivity(new Intent(activity, ListaAgendamentoProfissionalActivity.class));
                        activity.finish();
                    })
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            mensagem.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
