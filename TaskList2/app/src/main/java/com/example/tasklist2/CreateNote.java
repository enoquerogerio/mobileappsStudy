package com.example.tasklist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class CreateNote extends AppCompatActivity {

    private Button btnOk, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNote.this, MainActivity.class);
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
    private void addItem(View view) {
        EditText editTextTitle = findViewById(R.id.titleNote);
        EditText editTextNote = findViewById(R.id.noteText);

        String title = editTextTitle.getText().toString();
        String description = editTextNote.getText().toString();

        Intent intent = new Intent(CreateNote.this, MainActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        setResult(RESULT_OK, intent);
        finish();
    }
}