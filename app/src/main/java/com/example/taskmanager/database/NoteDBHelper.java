package com.example.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taskmanager.database.NoteDb.NoteTable;

public class NoteDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "note.db";

    public NoteDBHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME
                + "("
                + "_id integer primary key autoincrement, "
                + NoteTable.Cols.UUID + ", "
                + NoteTable.Cols.TITLE + ", "
                + NoteTable.Cols.DATE + ","
                + NoteTable.Cols.DESCRIPTION
                + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
