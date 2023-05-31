package com.example.translatelg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    private ArrayList<String> historyid, text, textts;

    CustomAdapter(Context context, ArrayList historyid, ArrayList text, ArrayList textts) {
        this.context = context;
        this.historyid = historyid;
        this.text = text;
        this.textts = textts;
    }

    // Tạo ViewHolder bằng cách inflate layout cho mỗi item trong RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_row, parent, false);
        return new MyViewHolder(view);
    }

    // Gán dữ liệu cho ViewHolder
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        // Đảo ngược vị trí để hiển thị các item theo thứ tự ngược lại
        int reversePosition = getItemCount() - position - 1;

        // Gán dữ liệu cho các view trong ViewHolder
        holder.historyid.setText(historyid.get(reversePosition));
        holder.histori_id_to.setText(text.get(reversePosition));
        holder.histori_id_from.setText(textts.get(reversePosition));
    }

    // Trả về số lượng item trong dataset
    @Override
    public int getItemCount() {
        return historyid.size();
    }

    // Lớp ViewHolder để lưu trữ các view cho mỗi item trong RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView historyid, histori_id_to, histori_id_from;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Khởi tạo các view trong ViewHolder
            historyid = itemView.findViewById(R.id.histori_id);
            histori_id_to = itemView.findViewById(R.id.histori_id_to);
            histori_id_from = itemView.findViewById(R.id.histori_id_from);
        }
    }
}
