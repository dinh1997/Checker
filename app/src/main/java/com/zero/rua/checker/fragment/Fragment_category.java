package com.zero.rua.checker.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.zero.rua.checker.R;
import com.zero.rua.checker.database.DatabaseHelper_category;
import com.zero.rua.checker.database.model.Note_category;
import com.zero.rua.checker.utils.MyDividerItemDecoration;
import com.zero.rua.checker.utils.RecyclerTouchListener;
import com.zero.rua.checker.view.NoteAdapter_category;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_category extends Fragment {
    private NoteAdapter_category mAdapter;
    private List<Note_category> notesList = new ArrayList<>();
    private TextView noNotesView;

    private int number_count, number_1;
    private int number_rank = 0;
    private int int_result = 0;

    private DatabaseHelper_category db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Category");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        noNotesView = view.findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper_category(getActivity());

        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NoteAdapter_category(getContext(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

// Actions when open fragment
        toggleEmptyNotes();
        checkAppExist();

//        Intent intent = new Intent(getContext(), MyService.class);
//        this.startService(intent);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showActionsDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void checkAppExist() {
        int i = db.getNotesCount() - 1; // get number of database's row
        for (int jj = 0; jj <= i; jj++) {
            Note_category note1 = notesList.get(jj);
            note1.setImg_check(R.drawable.ic_add_white_24dp);
        }
        new mTask().execute(i);
    }

    private void createNote(String name, String packKage, String category, String country, int status, String rank) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(name, packKage, category, country, status, rank);

        // get the newly inserted note from db
        Note_category n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    private void updateNote(String name, String pacKage, String category, String country, int status, String rank, int position) {
        Note_category n = notesList.get(position);
        // updating note text
        n.setName(name);
        n.setPacKage(pacKage);
        n.setCategory(category);
        n.setCountry(country);
        n.setImg_check(status);
        n.setRank(rank);

        db.updateNote(n);        // updating note in db

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    showDeleteDialog(position);
                }
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete it ?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(position);
                    }
                }).show();
    }

    @SuppressLint("InflateParams")
    private void showNoteDialog(final boolean shouldUpdate, final Note_category note, final int position) {
        View view = getLayoutInflater().inflate(R.layout.note_dialog_category, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());

        final EditText inputName = view.findViewById(R.id.dialog_name);
        final EditText inputPackage = view.findViewById(R.id.dialog_package);
        final SearchableSpinner spinner_category = view.findViewById(R.id.spinner_searchable_category);
        final SearchableSpinner spinner_country = view.findViewById(R.id.spinner_searchable);

        ArrayAdapter<String> adapter_category = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.array_category));
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_category.setTitle("Choose category");
        spinner_category.setAdapter(adapter_category);

        ArrayAdapter<String> adapter_country = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.array_country));
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_country.setTitle("Choose country");
        spinner_country.setAdapter(adapter_country);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            inputName.setText(note.getName());
            inputPackage.setText(note.getPacKage());

            String str_category = note.getCategory();
            String gl = note.getCountry();
            String str_gl_1 = "";
            switch (gl) {
                case "vi":
                    str_gl_1 = "Vietnam";
                    break;
                case "us":
                    str_gl_1 = "USA";
                    break;
                case "mm":
                    str_gl_1 = "Myanmar";
                    break;
                case "my":
                    str_gl_1 = "Malaysia";
                    break;
                case "la":
                    str_gl_1 = "Laos";
                    break;
                case "kh":
                    str_gl_1 = "Cambodia";
                    break;
                case "id":
                    str_gl_1 = "Indonesia";
                    break;
                case "sg":
                    str_gl_1 = "Singapore";
                    break;
                case "ph":
                    str_gl_1 = "Philippines";
                    break;
                default:
                    break;
            }
            int spinnerPosition = adapter_country.getPosition(str_gl_1);
            spinner_country.setSelection(spinnerPosition);
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
//                        stopService(new Intent(MainActivity.this, MyService.class));
//                        startService(new Intent(MainActivity.this, MyService.class));
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        alertDialogBuilderUserInput.setView(view);
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                String str_name = inputName.getText().toString();
                String str_package = inputPackage.getText().toString();
                String str_category = spinner_category.getSelectedItem().toString();
                String str_country = spinner_country.getSelectedItem().toString();
                String str_gl_1 = "";
                switch (str_country) {
                    case "Vietnam":
                        str_gl_1 = "vi";
                        break;
                    case "USA":
                        str_gl_1 = "us";
                        break;
                    case "Myanmar":
                        str_gl_1 = "mm";
                        break;
                    case "Malaysia":
                        str_gl_1 = "my";
                        break;
                    case "Laos":
                        str_gl_1 = "la";
                        break;
                    case "Cambodia":
                        str_gl_1 = "kh";
                        break;
                    case "Indonesia":
                        str_gl_1 = "id";
                        break;
                    case "Singapore":
                        str_gl_1 = "sg";
                        break;
                    case "Philippines":
                        str_gl_1 = "ph";
                        break;
                    default:
                        break;
                }
                if (TextUtils.isEmpty(inputPackage.getText().toString())) {
                    Toast.makeText(getActivity(), "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(str_name, str_package, str_category, str_gl_1, R.drawable.error, "", position);
                    int i = db.getNotesCount() - 1; // get number of database's row
                    for (int jj = 0; jj <= i; jj++) {
                        Note_category note1 = notesList.get(jj);
                        note1.setImg_check(R.drawable.ic_add_white_24dp);
                    }
                    new mTask().execute(i);
                } else {
                    // create new note
                    createNote(str_name, str_package, str_category, str_gl_1, R.drawable.ic_add_white_24dp, "");
                    int i = db.getNotesCount() - 1; // get number of database's row
                    for (int jj = 0; jj <= i; jj++) {
                        Note_category note1 = notesList.get(jj);
                        note1.setImg_check(R.drawable.ic_add_white_24dp);
                    }
                    new mTask().execute(i);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class mTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {


            Document document;
            number_count = integers[0];  // = number of element of array list - 1
            for (number_1 = 0; number_1 <= number_count; number_1++) {
//                str_array.clear();
                ArrayList<String> str_array = new ArrayList<>();

                Note_category note = notesList.get(number_1);
                String str_package = note.getPacKage();
                String url = "https://play.google.com/store/apps/category/" + note.getCategory() + "/collection/topselling_free?gl=" + note.getCountry();
                try {
                    document = Jsoup.connect(url).get();//.userAgent("Chrome")
                    Elements elements = document.select("a.title");
                    for (Element element : elements) {
                        String href = element.attr("href");
                        String[] output = href.split("=");
                        str_array.add(output[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int i = str_array.indexOf(str_package) + 1;
                int int_size = str_array.size();
                Log.d("position", "" + i);
//                if (str_array.contains(str_package)) {
//                    do {
//                        int_1++;
//                    } while (!str_package.equals(str_array.get(int_1)));
//                }

                publishProgress(number_1, i, int_size);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int int_update = values[0];   // get int_update = number_1
            int int_rank = values[1];
            int int_size = values[2];

            Note_category note = notesList.get(int_update);
            if (int_rank > 0) {
                note.setImg_check(R.drawable.check_ok);

                note.setRank("Rank :\n" + int_rank + " / " + int_size);
                mAdapter.notifyDataSetChanged();
            } else {
                int_result++;
                note.setImg_check(R.drawable.error);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int h = int_result;
//            checkIfAnAppDeleted(h);
        }
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

}
