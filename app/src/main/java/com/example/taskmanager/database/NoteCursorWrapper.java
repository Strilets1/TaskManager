package com.example.taskmanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.database.NoteDb.NoteTable;
import com.example.taskmanager.model.Note;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        String description = getString(getColumnIndex(NoteTable.Cols.DESCRIPTION));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));

        Note note = new Note(UUID.fromString(uuidString));

        note.setTitle(title);
        note.setDate(new Date(date));
        note.setDescription(description);

        return note;
    }
}
