package com.example.gravador_de_chamadas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private CallLogAdapter adapter;
    private List<CallLogItem> callLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        callLogList = new ArrayList<>();
        adapter = new CallLogAdapter(callLogList);
        recyclerView.setAdapter(adapter);

        // Solicitar permissões em tempo de execução
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Carregar o histórico de chamadas se as permissões já estiverem concedidas
            loadCallLogs();
        }

        // Verifica se existem informações de chamada recebidas via Intent (caso tenha uma nova chamada)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("number")) {
            String number = intent.getStringExtra("number");
            String type = intent.getStringExtra("type");
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String duration = intent.getStringExtra("duration");

            // Adicionar os dados da chamada à lista
            addCallToList(number, type, date, time, duration);
        }
    }

    // Metodo para adicionar os dados da chamada à lista e atualizar o RecyclerView
    private void addCallToList(String number, String type, String date, String time, String duration) {
        callLogList.add(new CallLogItem(number, type, date, time, duration));
        adapter.notifyDataSetChanged();
    }

    // Carregar o histórico de chamadas do dispositivo
    public void loadCallLogs() {
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            try {
                int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME); // Novo índice para o nome
                int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
                int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

                // Verifica se os índices das colunas são válidos
                if (numberIndex == -1 || typeIndex == -1 || dateIndex == -1 || durationIndex == -1) {
                    Log.e("CallLog", "Erro ao acessar colunas do Call Log");
                    return; // Interrompe o processamento se qualquer coluna não for encontrada
                }

                while (cursor.moveToNext()) {
                    String number = cursor.getString(numberIndex);
                    String name = cursor.getString(nameIndex); // Obtém o nome da pessoa, se disponível
                    String type = cursor.getString(typeIndex);
                    String date = cursor.getString(dateIndex);
                    String duration = cursor.getString(durationIndex);

                    // Se o nome for nulo, exiba o número como fallback
                    if (name == null || name.isEmpty()) {
                        name = number;
                    }

                    // Ignorar chamadas perdidas
                    if (Integer.parseInt(type) == CallLog.Calls.MISSED_TYPE) {
                        continue; // Pula para a próxima iteração, ignorando chamadas perdidas
                    }

                    // Converte a data e a hora
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String callDate = sdfDate.format(new Date(Long.parseLong(date)));
                    String callTime = sdfTime.format(new Date(Long.parseLong(date)));

                    // Determina o tipo de chamada (1: Incoming, 2: Outgoing)
                    String callType = "";
                    switch (Integer.parseInt(type)) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            callType = "Outgoing";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            callType = "Incoming";
                            break;
                    }

                    // Adiciona os dados à lista, agora com o nome
                    callLogList.add(new CallLogItem(name, callType, callDate, callTime, duration));
                }
            } finally {
                cursor.close();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Carregar o histórico de chamadas após a permissão ser concedida
                loadCallLogs();
            }
        }
    }
}


