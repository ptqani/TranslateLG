package com.example.translatelg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    Button buttonAddNote;
    TextView titleNote, contentNote;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        dbHelper = new DBHelper(this); // Initialize the dbHelper object
        buttonAddNote = findViewById(R.id.buttonAddNote);
        titleNote = findViewById(R.id.titleNote);
        contentNote = findViewById(R.id.contentNote);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleNote.getText().toString().trim();
                String content = contentNote.getText().toString().trim();
                if (!title.isEmpty() && !content.isEmpty()) {
                    dbHelper.addContent(title, content);
                    Toast.makeText(AddNoteActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Bạn cần điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
