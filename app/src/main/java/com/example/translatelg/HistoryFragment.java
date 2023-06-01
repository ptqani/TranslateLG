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
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    DBHelper dbHelper;
    Button delete_data;
    ArrayList<String> historyid, text, textts;
    CustomHistoryAdapter customAdapter;
    private RecyclerView rvHistory;

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

        customAdapter = new CustomHistoryAdapter(getContext(), historyid, text, textts);
        rvHistory.setAdapter(customAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllData();
            }
        });

        loadData(); // Tải dữ liệu lịch sử dịch từ cơ sở dữ liệu vào các danh sách

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTranslationHistory(); // Cập nhật lịch sử dịch khi fragment được hiển thị lại
    }

    // tải dữ liệu lịch sử dịch từ cơ sở dữ liệu vào các danh sách
    private void loadData() {
        Cursor cursor = dbHelper.readAllData(); // Lấy dữ liệu từ cơ sở dữ liệu
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                historyid.add(cursor.getString(0)); // Thêm ID vào danh sách lịch sử
                text.add(cursor.getString(1)); // Thêm văn bản gốc vào danh sách lịch sử
                textts.add(cursor.getString(2)); // Thêm văn bản dịch vào danh sách lịch sử
            }
        }
    }


    // xóa toàn bộ dữ liệu lịch sử dịch
    private void deleteAllData() {
        dbHelper.deleteAllDataTable(); // Xóa toàn bộ dữ liệu trong cơ sở dữ liệu
        historyid.clear();
        text.clear();
        textts.clear();
        customAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị danh sách trống
        Toast.makeText(getContext(), "Đã xóa tất cả dữ liệu", Toast.LENGTH_SHORT).show();
    }


    //  cập nhật lịch sử dịch từ cơ sở dữ liệu và cập nhật dữ liệu trong adapter
    private void updateTranslationHistory() {
        historyid.clear();
        text.clear();
        textts.clear();
        loadData(); // Tải lại dữ liệu lịch sử dịch từ cơ sở dữ liệu
        customAdapter.notifyDataSetChanged(); // Cập nhật adapter để hiển thị dữ liệu mới
    }
}
