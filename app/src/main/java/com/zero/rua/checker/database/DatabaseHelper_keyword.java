package com.zero.rua.checker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zero.rua.checker.database.model.Note_keyword;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper_keyword extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper_keyword(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note_keyword.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Note_keyword.TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String name, String package_app, String keyword, String country, int status, String rank) {
        SQLiteDatabase db = this.getWritableDatabase();        // get writable database as we want to write data

        ContentValues values = new ContentValues();
        values.put(Note_keyword.COLUMN_NAME, name);
        values.put(Note_keyword.COLUMN_PACKAGE_APP, package_app);        // `id` and `timestamp` will be inserted automatically.no need to add them
        values.put(Note_keyword.COLUMN_KEYWORD, keyword);
        values.put(Note_keyword.COLUMN_COUNTRY, country);
        values.put(Note_keyword.COLUMN_STATUS, status);
        values.put(Note_keyword.COLUMN_RANK, rank);

        long id = db.insert(Note_keyword.TABLE_NAME, null, values);        // insert row
        db.close();
        return id;        // return newly inserted row id
    }

    public Note_keyword getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();        // get readable database as we are not inserting anything

        Cursor cursor = db.query(Note_keyword.TABLE_NAME,
                new String[]{Note_keyword.COLUMN_ID, Note_keyword.COLUMN_NAME, Note_keyword.COLUMN_PACKAGE_APP, Note_keyword.COLUMN_KEYWORD, Note_keyword.COLUMN_COUNTRY, Note_keyword.COLUMN_STATUS, Note_keyword.COLUMN_RANK, Note_keyword.COLUMN_TIMESTAMP},
                Note_keyword.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        assert cursor != null;
        Note_keyword note = new Note_keyword(
                cursor.getInt(cursor.getColumnIndex(Note_keyword.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_PACKAGE_APP)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_KEYWORD)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_COUNTRY)),
                cursor.getInt(cursor.getColumnIndex(Note_keyword.COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_RANK)),
                cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_TIMESTAMP)));

        cursor.close();        // close the db connection
        return note;
    }

    public List<Note_keyword> getAllNotes() {
        List<Note_keyword> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note_keyword.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note_keyword note = new Note_keyword();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note_keyword.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_NAME)));
                note.setPacKage(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_PACKAGE_APP)));
                note.setKeyword(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_KEYWORD)));
                note.setCountry(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_COUNTRY)));
                note.setImg_check(cursor.getInt(cursor.getColumnIndex(Note_keyword.COLUMN_STATUS)));
                note.setRank(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_RANK)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note_keyword.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();
        return notes;        // return notes list
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note_keyword.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void updateNote(Note_keyword note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note_keyword.COLUMN_NAME, note.getName());
        values.put(Note_keyword.COLUMN_PACKAGE_APP, note.getPacKage());
        values.put(Note_keyword.COLUMN_KEYWORD, note.getKeyword());
        values.put(Note_keyword.COLUMN_COUNTRY, note.getCountry());
        values.put(Note_keyword.COLUMN_STATUS, note.getImg_check());
        values.put(Note_keyword.COLUMN_RANK, note.getRank());

        // updating row
        db.update(Note_keyword.TABLE_NAME, values, Note_keyword.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note_keyword note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note_keyword.TABLE_NAME, Note_keyword.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
