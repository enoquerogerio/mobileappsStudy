package com.example.player;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etNationality, etBirthDate, etTitles;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etNationality = findViewById(R.id.etNationality);
        etBirthDate = findViewById(R.id.etBirthDate);
        etTitles = findViewById(R.id.etTitles);
        resultView = findViewById(R.id.resultView);
        Button btnSendPlayer = findViewById(R.id.btnSendPlayer);
        Button btnGetPlayers = findViewById(R.id.btnGetPlayers);

        // POST
        btnSendPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = etName.getText().toString();
                    String nationality = etNationality.getText().toString();
                    String birthDate = etBirthDate.getText().toString();
                    int titles = Integer.parseInt(etTitles.getText().toString());

                    Player playerToSend = new Player(0, name, nationality, birthDate, titles);
                    String urlString = "http://192.168.1.6:8080/players";

                    new WebService(resultView, urlString, "POST", playerToSend).execute();
                } catch (Exception e) {
                    resultView.setText("Erro ao processar os dados: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        // GET
        btnGetPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = "http://192.168.1.6:8080/players";
                new WebService(resultView, urlString, "GET", null).execute();
            }
        });
    }
}
