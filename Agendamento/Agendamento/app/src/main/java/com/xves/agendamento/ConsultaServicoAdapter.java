package com.xves.agendamento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsultaServicoAdapter extends RecyclerView.Adapter<ConsultaServicoAdapter.HolderServico> {
    private List<ServiceModel> list;
    private Context ctx;
    private Activity activity;
    private SharedPreferences prefs;

    public ConsultaServicoAdapter(List<ServiceModel> list, Context context) {
        this.list = list;
        this.ctx = context;
        this.activity = (Activity) context;
        this.prefs = context.getSharedPreferences("PREFUSUARIO", Context.MODE_PRIVATE);
    }

    class HolderServico extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtEndereco, txtData, txtHora, txtPreco;
        LinearLayout btnCard;
        LinearLayout btnCancelar;
        Button btnDadosCliente;

        public HolderServico(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtData = itemView.findViewById(R.id.txtData);
            txtHora = itemView.findViewById(R.id.txtHora);
            txtPreco = itemView.findViewById(R.id.txtPreco);

            btnCard = itemView.findViewById(R.id.btnCard);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnDadosCliente = itemView.findViewById(R.id.btnDadosCliente);
        }
    }

    @Override
    public HolderServico onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servico, parent, false);
        return new HolderServico(view);
    }

    @Override
    public void onBindViewHolder(HolderServico holder, int position) {
        ServiceModel dataModel = list.get(position);
        holder.txtDescricao.setText(dataModel.getDescricao());
        holder.txtEndereco.setText(dataModel.getEndereco());

        String dataString = dataModel.getData();
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date data = formatoEntrada.parse(dataString + " " + dataModel.getHora());
            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatoSaida.format(data);
            holder.txtData.setText(dataFormatada);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txtHora.setText(dataModel.getHora());
        holder.txtPreco.setText(dataModel.getValor());

        prefs = activity.getSharedPreferences("PREFUSUARIO", Context.MODE_PRIVATE);

        boolean cancelamentoPermitido = verificaCancelamentoPermitido(dataModel.getData(), dataModel.getHora());

        holder.btnCancelar.setEnabled(cancelamentoPermitido);

        holder.btnCancelar.setOnClickListener(v -> {
            if (cancelamentoPermitido) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(activity);
                mensagem.setMessage("Deseja Realmente Cancelar? ");
                mensagem.setCancelable(false);
                mensagem.setPositiveButton("Sim", (dialog, id) -> {
                    BaseDados database = new BaseDados(activity);
                    SQLiteDatabase db = database.getWritableDatabase();
                    int deletedRows = db.delete("xves_service", "id=?", new String[]{String.valueOf(dataModel.getId())});
                    if (deletedRows > 0) {
                        Toast.makeText(ctx, "Cancelado com Sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "Erro ao Cancelar!", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    activity.startActivity(new Intent(activity, ListaAgendamentoActivity.class));
                    activity.finish();
                }).setNegativeButton("Não", (dialog, id) -> dialog.cancel());
                mensagem.show();
            } else {
                Toast.makeText(ctx, "Cancelamento não permitido. Mínimo de duas horas de antecedência necessário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isUserLoggedIn() {
        return prefs.getBoolean("loggedIn", false);
    }

    private boolean verificaCancelamentoPermitido(String data, String hora) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dataServico = formatoEntrada.parse(data + " " + hora);
            Date dataAtual = new Date();
            long diferencaMillis = dataServico.getTime() - dataAtual.getTime();
            long diferencaHoras = diferencaMillis / (60 * 60 * 1000);
            return diferencaHoras >= 2;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
