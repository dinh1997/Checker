package com.zero.rua.checker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zero.rua.checker.R;
import com.zero.rua.checker.database.model.Note_category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapter_category extends RecyclerView.Adapter<NoteAdapter_category.MyViewHolder> {
    private Context context;
    private List<Note_category> notesList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dot;
        TextView item_name;
        TextView item_package;
        TextView item_keyword;
        TextView timestamp;
        TextView rank;
        ImageView img_check;

        MyViewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            item_name = view.findViewById(R.id.item_name);
            item_package = view.findViewById(R.id.item_package);
            item_keyword = view.findViewById(R.id.item_keyword);
            timestamp = view.findViewById(R.id.timestamp);
            img_check = view.findViewById(R.id.check_status);
            rank = view.findViewById(R.id.txt_rank);
        }
    }

    public NoteAdapter_category(Context context, List<Note_category> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull NoteAdapter_category.MyViewHolder holder, int position) {
        Note_category note = notesList.get(position);

        holder.dot.setText(Html.fromHtml("&#8226;"));        // Displaying dot from HTML character code
        holder.item_name.setText(note.getName());
        holder.item_package.setText(note.getPacKage());

        String gl = note.getCountry();
        String link = "https://play.google.com/store/apps/category/" + note.getCategory() + "/collection/topselling_free?gl=" + gl;
        holder.item_keyword.setText(link);

        holder.timestamp.setText(formatDate(note.getTimestamp()));        // Formatting and displaying timestamp
        holder.img_check.setImageResource(note.getImg_check());
        holder.rank.setText(note.getRank());
    }

    public int getItemCount() {
        return notesList.size();
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {
            Log.d("logDate", e.toString());
        }

        return "";
    }
}