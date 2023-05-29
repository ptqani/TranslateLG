package com.example.translatelg;


import android.database.Cursor;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
  DBHelper dbHelper;
  Button delete_data;
  ArrayList<String> historyid,text, textts;
  CustomAdapter customAdapter;
    private RecyclerView rvHistory;


    private TranslationHistoryAdapter adapter;

    // Khởi tạo danh sách lịch sử dịch
    private ArrayList<Pair<String, String>> translationHistory = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rvHistory = view.findViewById(R.id.rvHistory);
        delete_data = view.findViewById(R.id.deleteHis);
        dbHelper = new DBHelper(getContext());
        historyid = new ArrayList<>();
        text = new ArrayList<>();
        textts = new ArrayList<>();
        showTranslationHistory();
        customAdapter = new CustomAdapter(getContext(),historyid,text,textts);
        rvHistory.setAdapter( customAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllData();
            }
        });
        return view;
    }


    // Phương thức cập nhật lịch sử dịch
    public void updateTranslationHistory(String sourceText, String translation) {
        Pair<String, String> historyEntry = new Pair<>(
                "From: " + sourceText + " - To: " + translation,
                "");
        translationHistory.add(historyEntry);

        // Thông báo cho adapter cập nhật lại
        adapter.notifyDataSetChanged();

        // Hiển thị lịch sử dịch
        showTranslationHistory();
    }


    // Phương thức hiển thị lịch sử dịch
    private void showTranslationHistory() {
        Cursor cursor = dbHelper.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                historyid.add(cursor.getString(0));
                text.add(cursor.getString(1));
                textts.add(cursor.getString(2));

            }
        }
    }

    private void deleteAllData() {
        dbHelper.deleteAllData();
        historyid.clear();
        text.clear();
        textts.clear();
        customAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Đã xóa tất cả dữ liệu", Toast.LENGTH_SHORT).show();
    }

}