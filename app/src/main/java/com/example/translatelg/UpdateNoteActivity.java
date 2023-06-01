package com.example.translatelg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNoteActivity extends AppCompatActivity {
    EditText titleNoteUp, contentNoteUp;
    Button buttonAddNoteUp, buttonDeleteNote;
    String id, title, content;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        titleNoteUp = findViewById(R.id.titleNoteUp);
        contentNoteUp = findViewById(R.id.contentNoteUp);
        buttonAddNoteUp = findViewById(R.id.buttonAddNoteUp);
        buttonDeleteNote = findViewById(R.id.buttonDeleteNote);

        getIntenData(); //nhận dữ liệu từ
        //Cập nhật ghi chú

        buttonAddNoteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DBHelper(UpdateNoteActivity.this);
                title = titleNoteUp.getText().toString().trim();
                content = contentNoteUp.getText().toString().trim();
                dbHelper.UpdateDataNote(id, title, content);
                Toast.makeText(UpdateNoteActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Xóa ghi chú

        buttonDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DBHelper(UpdateNoteActivity.this);
                dbHelper.DeleteDataNote(id);
                Toast.makeText(UpdateNoteActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

//lấy dữ liệu
    void getIntenData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("content")) {
            // lấy dữ liệu từ intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            content = getIntent().getStringExtra("content");
            // cài đặt intent
            titleNoteUp.setText(title);
            contentNoteUp.setText(content);
        } else {
            Toast.makeText(UpdateNoteActivity.this, "Không nhận được dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }
}