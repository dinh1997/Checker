package com.zero.rua.checker.view;

/*
 * Created by ravi on 20/02/18.
 */

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
import com.zero.rua.checker.database.model.Note_keyword;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NotesAdapter_keyword extends RecyclerView.Adapter<NotesAdapter_keyword.MyViewHolder> {

    private Context context;
    private List<Note_keyword> notesList;

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


    public NotesAdapter_keyword(Context context, List<Note_keyword> notesList) {
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note_keyword note = notesList.get(position);

        holder.dot.setText(Html.fromHtml("&#8226;"));        // Displaying dot from HTML character code
        holder.item_name.setText(note.getName());
        holder.item_package.setText(note.getPacKage());

        String gl = note.getCountry();
        String link = "https://play.google.com/store/search?q=" + note.getKeyword() + "&c=apps&gl=" + gl;
        holder.item_keyword.setText(link);

        holder.timestamp.setText(formatDate(note.getTimestamp()));        // Formatting and displaying timestamp
        holder.img_check.setImageResource(note.getImg_check());
        holder.rank.setText(note.getRank());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
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
