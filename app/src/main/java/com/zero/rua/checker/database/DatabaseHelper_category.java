package com.zero.rua.checker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zero.rua.checker.database.model.Note_category;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper_category extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db_category";

    public DatabaseHelper_category(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note_category.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Note_category.TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String name, String package_app, String category, String country, int status, String rank) {
        SQLiteDatabase db = this.getWritableDatabase();        // get writable database as we want to write data

        ContentValues values = new ContentValues();
        values.put(Note_category.COLUMN_NAME, name);
        values.put(Note_category.COLUMN_PACKAGE_APP, package_app);        // `id` and `timestamp` will be inserted automatically.no need to add them
        values.put(Note_category.COLUMN_CATEGORY, category);
        values.put(Note_category.COLUMN_COUNTRY, country);
        values.put(Note_category.COLUMN_STATUS, status);
        values.put(Note_category.COLUMN_RANK, rank);

        long id = db.insert(Note_category.TABLE_NAME, null, values);        // insert row
        db.close();
        return id;        // return newly inserted row id
    }

    public Note_category getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();        // get readable database as we are not inserting anything

        Cursor cursor = db.query(Note_category.TABLE_NAME,
                new String[]{Note_category.COLUMN_ID, Note_category.COLUMN_NAME, Note_category.COLUMN_PACKAGE_APP, Note_category.COLUMN_CATEGORY, Note_category.COLUMN_COUNTRY, Note_category.COLUMN_STATUS, Note_category.COLUMN_RANK, Note_category.COLUMN_TIMESTAMP},
                Note_category.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        assert cursor != null;
        Note_category note = new Note_category(
                cursor.getInt(cursor.getColumnIndex(Note_category.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_PACKAGE_APP)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_COUNTRY)),
                cursor.getInt(cursor.getColumnIndex(Note_category.COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_RANK)),
                cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_TIMESTAMP)));

        cursor.close();        // close the db connection
        return note;
    }

    public List<Note_category> getAllNotes() {
        List<Note_category> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note_category.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note_category note = new Note_category();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note_category.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_NAME)));
                note.setPacKage(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_PACKAGE_APP)));
                note.setCategory(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_CATEGORY)));
                note.setCountry(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_COUNTRY)));
                note.setImg_check(cursor.getInt(cursor.getColumnIndex(Note_category.COLUMN_STATUS)));
                note.setRank(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_RANK)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note_category.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();
        return notes;        // return notes list
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note_category.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void updateNote(Note_category note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note_category.COLUMN_NAME, note.getName());
        values.put(Note_category.COLUMN_PACKAGE_APP, note.getPacKage());
        values.put(Note_category.COLUMN_CATEGORY, note.getCategory());
        values.put(Note_category.COLUMN_COUNTRY, note.getCountry());
        values.put(Note_category.COLUMN_STATUS, note.getImg_check());
        values.put(Note_category.COLUMN_RANK, note.getRank());

        // updating row
        db.update(Note_category.TABLE_NAME, values, Note_category.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note_category note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note_category.TABLE_NAME, Note_category.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
