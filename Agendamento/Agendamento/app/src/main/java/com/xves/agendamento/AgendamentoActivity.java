package com.xves.agendamento;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgendamentoActivity extends AppCompatActivity {

    EditText edtData, edtHora;
    Button btnAgendar;
    SharedPreferences prefs;
    ImageView imgBack;
    BaseDados base;
    AlarmManager alarmManager;
    PendingIntent oneTimeAlarmIntent;
    DatePickerDialog picker;
    Intent intent;
    Calendar currentTime;
    String id_cliente, id_profissional, endereco, valor, descricao, data_save, time;

    ActivityResultLauncher<String> requestPermissionLauncher;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        base = new BaseDados(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        edtData = findViewById(R.id.edtData);
        edtHora = findViewById(R.id.edtHora);
        btnAgendar = findViewById(R.id.btnAgendar);
        prefs = getSharedPreferences("PREFUSUARIO", Context.MODE_PRIVATE);
        id_cliente = prefs.getString("id_user", "");
        imgBack = findViewById(R.id.imgBack);

        Intent i = getIntent();
        id_profissional = i.getStringExtra("id_profissional");
        endereco = i.getStringExtra("endereco");
        valor = i.getStringExtra("valor");
        descricao = i.getStringExtra("descricao");

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(this, MyBroadcastReceiver.class);
        oneTimeAlarmIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_MUTABLE);

        btnAgendar.setOnClickListener(v -> {
            if (edtData.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Data vazia", Toast.LENGTH_LONG).show();
                return;
            }
            if (edtHora.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Hora vazia", Toast.LENGTH_LONG).show();
                return;
            }

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, currentTime.getTimeInMillis(), oneTimeAlarmIntent);
            Toast.makeText(getApplicationContext(), "Alarme Configurado!", Toast.LENGTH_LONG).show();

            ContentValues values = new ContentValues();
            values.put("id_cliente", id_cliente);
            values.put("id_profissional", id_profissional);
            values.put("endereco", endereco);
            values.put("valor", valor);
            values.put("descricao", descricao);
            values.put("data", data_save);
            values.put("hora", time);

            SQLiteDatabase db = base.getWritableDatabase();

            long newRowId = db.insertWithOnConflict("xves_service", null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (newRowId == -1) {
                Toast.makeText(getApplicationContext(), "Erro ao cadastrar!", Toast.LENGTH_LONG).show();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    showDummyNotification();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
                startActivity(new Intent(this, ListaAgendamentoActivity.class));
                finish();
            }
        });

        edtData.setInputType(InputType.TYPE_NULL);
        edtData.setOnClickListener(marker -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
                String data_show = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                edtData.setText(data_show);
                data_save = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            }, year, month, day);

            picker.show();
        });

        edtHora.setInputType(InputType.TYPE_NULL);
        edtHora.setOnClickListener(marker -> {
            currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, (view, hourOfDay, minuteOfHour) -> {
                currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                currentTime.set(Calendar.MINUTE, minuteOfHour);
                currentTime.set(Calendar.SECOND, 0);
                currentTime.set(Calendar.MILLISECOND, 0);

                String hora = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                edtHora.setText(hora);
            }, hour, minute, true);

            timePickerDialog.show();
        });

        imgBack.setOnClickListener(marker -> {
            finish();
        });

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Important Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Esta notificação contém anúncios importantes, etc.");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDummyNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Parabéns! 🎉🎉🎉")
                .setContentText("Agendamento Confirmado!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    public static final String CHANNEL_ID = "dummy_channel";
}
