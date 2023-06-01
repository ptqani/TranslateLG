package com.example.translatelg;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomNoteAdapter extends RecyclerView.Adapter<CustomNoteAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> noteid, titleNote, contentNode;

    CustomNoteAdapter(Context context, ArrayList noteid, ArrayList titleNote, ArrayList contentNode) {
        this.context = context;
        this.noteid = noteid;
        this.titleNote = titleNote;
        this.contentNode = contentNode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // tạo ra một view để hiển thịmột mục trong ds
        LayoutInflater inflater = LayoutInflater.from(context); // chuyển các tệp nguồn => View
        View view = inflater.inflate(R.layout.note_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNoteAdapter.MyViewHolder holder, final int position) { //liên kết dữ liệu với viewHolder
        int reversePosition = getItemCount() - position - 1;
        holder.noteid.setText(noteid.get(reversePosition));
        holder.titleNote.setText(titleNote.get(reversePosition));
        holder.contentNode.setText(contentNode.get(reversePosition));
        holder.MainNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateNoteActivity.class);
                intent.putExtra("id", String.valueOf(noteid.get(reversePosition)));
                intent.putExtra("title", String.valueOf(titleNote.get(reversePosition)));
                intent.putExtra("content", String.valueOf(contentNode.get(reversePosition)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteid.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noteid, titleNote, contentNode;
        LinearLayout MainNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteid = itemView.findViewById(R.id.idNote);
            titleNote = itemView.findViewById(R.id.title_Node);
            contentNode = itemView.findViewById(R.id.content_Note);
            MainNote = itemView.findViewById(R.id.MainNote);
        }
    }
}
