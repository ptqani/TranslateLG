package com.example.translatelg;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TranslationHistoryAdapter extends RecyclerView.Adapter<TranslationHistoryAdapter.ViewHolder> {

    private List<Pair<String, String>> translationHistory;

    public TranslationHistoryAdapter(List<Pair<String, String>> translationHistory) {
        this.translationHistory = translationHistory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, String> entry = translationHistory.get(position);
        holder.sourceTextView.setText(entry.first);
        holder.translationTextView.setText(entry.second);
    }

    @Override
    public int getItemCount() {
        return translationHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView;
        TextView translationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.source_text);
            translationTextView = itemView.findViewById(R.id.translation_text);
        }
    }
}
