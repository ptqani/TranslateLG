package com.example.translatelg;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private RecyclerView rvHistory;

    private TranslationHistoryAdapter adapter;

    // Khởi tạo danh sách lịch sử dịch
    private ArrayList<Pair<String, String>> translationHistory = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rvHistory = view.findViewById(R.id.rvHistory);
        adapter = new TranslationHistoryAdapter(translationHistory);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String text = bundle.getString("text");
            String doneText = bundle.getString("doneText");
            // Sử dụng dữ liệu đã chuyển đến đây
            updateTranslationHistory(text, doneText);
        }
        showTranslationHistory();

        return view;
    }

    // Phương thức cập nhật lịch sử dịch
    public void updateTranslationHistory(String sourceText, String translation) {
        Pair<String, String> historyEntry = new Pair<>(
                "From: " + sourceText + " - To: " + translation,
                "");
        translationHistory.add(historyEntry);
        adapter.notifyDataSetChanged();   // Thông báo cho adapter cập nhật lại

        // Hiển thị lịch sử dịch
        showTranslationHistory();
    }

    // Phương thức hiển thị lịch sử dịch
    private void showTranslationHistory() {
        if (translationHistory.isEmpty()) {
            rvHistory.setVisibility(View.GONE);
        } else {
            rvHistory.setVisibility(View.VISIBLE);


            // Thiết lập RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            rvHistory.setLayoutManager(layoutManager);

            // Thiết lập adapter
            TranslationHistoryAdapter adapter = new TranslationHistoryAdapter(translationHistory);
            rvHistory.setAdapter(adapter);
        }
    }

    // tạo một tham chiếu đến HistoryFragment hiện tại:
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

}