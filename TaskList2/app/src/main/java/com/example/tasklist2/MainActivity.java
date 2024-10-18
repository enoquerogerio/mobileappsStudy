package com.example.tasklist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> notesMap; // HashMap para armazenar notas
    private ListView listView;
    private ArrayList<String> taskList;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.newNote);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNote.class);
                startActivityForResult(intent, 1);
            }
        });

        notesMap = new HashMap<>();
        listView = findViewById(R.id.listView);
        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Abrir ReadNoteActivity com o conteúdo da nota
                String selectedTitle = taskList.get(position);
                String selectedDescription = notesMap.get(selectedTitle);

                Intent intent = new Intent(MainActivity.this, ReadNote.class);
                intent.putExtra("title", selectedTitle);
                intent.putExtra("description", selectedDescription);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Adicionar nova nota à lista
            String noteTitle = data.getStringExtra("title");
            String noteDescription = data.getStringExtra("description");

            notesMap.put(noteTitle, noteDescription);
            taskList.add(noteTitle);
            adapter.notifyDataSetChanged();
        }
    }

}