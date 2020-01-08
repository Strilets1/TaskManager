package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database.NoteCursorWrapper;
import com.example.taskmanager.database.NoteDBHelper;
import com.example.taskmanager.database.NoteDb;
import com.example.taskmanager.database.NoteDb.NoteTable;
import com.example.taskmanager.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteSingleton {

    private static NoteSingleton sNoteSingleton;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    private NoteSingleton(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new NoteDBHelper(mContext).getWritableDatabase();
    }

    public static NoteSingleton getInstance(Context context) {
        if (sNoteSingleton == null) {
            sNoteSingleton = new NoteSingleton(context);
        }
        return sNoteSingleton;
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper cursorWrapper = queryCrimes(NoteTable.Cols.UUID + " = ?", new String[]{id.toString()},null);
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getNote();
        } finally {
            cursorWrapper.close();
        }
    }

    public List<Note> getNoteList(String groupBy) {
        List<Note> noteList = new ArrayList<>();
        NoteCursorWrapper cursorWrapper = queryCrimes(null, null, groupBy);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                noteList.add(cursorWrapper.getNote());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return noteList;
    }

    private NoteCursorWrapper queryCrimes(String whereCause, String[] args, String groupBy) {
        Cursor cursor = mSQLiteDatabase.query(NoteTable.NAME, null, whereCause, args, groupBy, null, null);
        return new NoteCursorWrapper(cursor);
    }

    public void addNote(Note note) {
        ContentValues values = getContextValues(note);
        mSQLiteDatabase.insert(NoteTable.NAME, null, values);
    }

    public void deleteNote(Note note, String whereCause, String[] whereArgs) {
        mSQLiteDatabase.delete(NoteTable.NAME, whereCause, whereArgs);
    }

    public void updateNotes(Note note) {
        String uuisString = note.getId().toString();
        ContentValues values = getContextValues(note);

        mSQLiteDatabase.update(NoteTable.NAME, values,
                NoteTable.Cols.UUID + " = ?",
                new String[]{uuisString});
    }

    private static ContentValues getContextValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteTable.Cols.UUID, note.getId().toString());
        contentValues.put(NoteTable.Cols.DATE, note.getDate().getTime());
        contentValues.put(NoteTable.Cols.DESCRIPTION, note.getDescription());
        contentValues.put(NoteTable.Cols.TITLE, note.getTitle());
        return contentValues;
    }

}
