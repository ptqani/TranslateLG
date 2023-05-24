package com.example.translatelg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HistoryFragment extends Fragment {
    private TextView idTVHistoryTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        idTVHistoryTV = view.findViewById(R.id.idTVHistoryTV);
        // Inflate the layout for this fragment

        return view;
    }

}