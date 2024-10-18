package com.example.tasklist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReadNote extends AppCompatActivity {
    private TextView noteTitleTextView;
    private TextView noteDescriptionTextView;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        noteTitleTextView = findViewById(R.id.note_title);
        noteDescriptionTextView  = findViewById(R.id.note_text);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadNote.this, MainActivity.class);
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // Receber o título e a descrição da nota
        Intent intent = getIntent();
        String noteTitle = intent.getStringExtra("title");
        String noteDescription = intent.getStringExtra("description");

        noteTitleTextView.setText(noteTitle);
        noteDescriptionTextView.setText(noteDescription);
    }
}