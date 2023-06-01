package com.example.translatelg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class SettingFragment extends Fragment {

    private Switch Switch;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        switchButton = view.findViewById(R.id.switch1);

        // Khởi tạo SharedPreferences với tên "MODE"
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);

        // Lấy trạng thái chế độ ban đêm hiện tại từ SharedPreferences
        boolean nightMode = sharedPreferences.getBoolean("night", false);

        // Đặt trạng thái ban đầu của Switch và áp dụng chế độ ban đêm nếu cần
        switchButton.setChecked(nightMode);
        setNightMode(nightMode);


        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đổi trạng thái chế độ ban đêm và cập nhật SharedPreferences
                boolean newNightMode =! nightMode;
                setNightMode(newNightMode);
                editor = sharedPreferences.edit();
                editor.putBoolean("night", newNightMode);
                editor.apply();
            }
        });

        return view;
    }

    // Đặt chế độ ban đêm cho ứng dụng
    private void setNightMode(boolean nightMode) {
        int mode = nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}