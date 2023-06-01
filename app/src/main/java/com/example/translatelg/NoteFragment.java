package com.example.translatelg;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NoteFragment extends Fragment {

    // nút thêm ghi chú mới
    FloatingActionButton addNote;
    // dc sử dung thao tác với csdl
    DBHelper dbHelper;
    // lưu thông tin
    ArrayList<String> noteid, titleNote, contentNode;
    // hiển  thị danh sách ghi chú trong recy
    CustomNoteAdapter adapter;
    // hiển thị danh sách gchu dưới dạng cuộn

    private RecyclerView rvNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        rvNote = view.findViewById(R.id.rvNote);
        addNote = view.findViewById(R.id.addNote);

        // đối tượng làm việc với csdl
        dbHelper = new DBHelper(getContext());
        // lưu dữ liệu gh chú
        noteid = new ArrayList<>();
        titleNote = new ArrayList<>();
        contentNode = new ArrayList<>();
        //
        showTranslationHistory(); // Load and display the data
        adapter = new CustomNoteAdapter(getContext(), noteid, titleNote, contentNode);
        rvNote.setAdapter(adapter);
        rvNote.setLayoutManager(new LinearLayoutManager(getContext()));
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTranslationNote(); // Cập nhật lịch sử dịch khi fragment được hiển thị lại
    }

    private void updateTranslationNote() {
        noteid.clear();
        titleNote.clear();
        contentNode.clear();
        showTranslationHistory();
        adapter.notifyDataSetChanged();
    }

    private void showTranslationHistory() {
        Cursor cursor = dbHelper.readAllDataNote();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                noteid.add(cursor.getString(0));
                titleNote.add(cursor.getString(1));
                contentNode.add(cursor.getString(2));

            }

        }
    }

}