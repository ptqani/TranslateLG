package com.example.translatelg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private TextView tvHistory;

    // Khởi tạo danh sách lịch sử dịch
    private ArrayList<Pair<String, String>> translationHistory = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        tvHistory = view.findViewById(R.id.tvHistory);
        // Inflate the layout for this fragment

        return view;
    }

    // Phương thức cập nhật lịch sử dịch
    public void updateTranslationHistory(String sourceText, String translation) {
        Pair<String, String> historyEntry = new Pair<>(sourceText, translation);
        translationHistory.add(historyEntry);

        // Hiển thị lịch sử dịch
        showTranslationHistory();
    }

    // Phương thức hiển thị lịch sử dịch
    private void showTranslationHistory() {
        if (translationHistory.isEmpty()) {
            tvHistory.setVisibility(View.GONE);
        } else {
            tvHistory.setVisibility(View.VISIBLE);
            // Hiển thị toàn bộ lịch sử dịch
            StringBuilder historyText = new StringBuilder();
            for (Pair<String, String> entry : translationHistory) {
                String sourceText = entry.first;
                String translation = entry.second;
                String historyEntry = "From: " + sourceText + " - To: " + translation + "\n";
                historyText.append(historyEntry);
            }
            tvHistory.setText(historyText.toString());
        }
    }

    // tạo một tham chiếu đến HistoryFragment hiện tại:
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

}