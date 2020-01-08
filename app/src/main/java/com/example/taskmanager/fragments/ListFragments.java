package com.example.taskmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.NoteSingleton;
import com.example.taskmanager.R;
import com.example.taskmanager.database.NoteDb;
import com.example.taskmanager.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ListFragments extends Fragment {

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private Callbacks mCallbacks;
    private static final String TAG = "ListFragments";

    public interface Callbacks {
        void onNoteSelected(Note note);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(mNoteAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                NoteSingleton.getInstance(getActivity()).addNote(note);
                mCallbacks.onNoteSelected(note);
                updateUI(null);
            }
        });
        updateUI(null);
        setHasOptionsMenu(true);
        return view;
    }

    public void updateUI(String groupBy) {
        NoteSingleton noteSingleton = NoteSingleton.getInstance(getActivity());
        List<Note> noteList = noteSingleton.getNoteList(groupBy);
        if (noteList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(noteList);
            mRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.notifyDataSetChanged();
            mNoteAdapter.setNote(noteList);
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatTextView mTitleTask;
        private AppCompatTextView mDateTask, mDescription;
        private Note mNote;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTask = itemView.findViewById(R.id.task_name);
            mDateTask = itemView.findViewById(R.id.date_task);
            mDescription = itemView.findViewById(R.id.task_description);
        }

        public void bind(Note note) {
            mNote = note;
            mTitleTask.setText(mNote.getTitle());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = mNote.getDate();
            mDateTask.setText(dateFormat.format(date));
//            int length;
//            if (note.getDescription() != null) {
//                length = note.getDescription().length();
//
//                mDescription.setText(note.getDescription().substring(0, 30));
//            } else {
//                mDescription.setText(note.getDescription());
//            }
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onNoteSelected(mNote);
        }
    }


    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mNoteList;

        public NoteAdapter(List<Note> noteList) {
            mNoteList = noteList;
        }

        public void setNote(List<Note> notes) {
            this.mNoteList = notes;
        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            Note note = mNoteList.get(position);
            holder.bind(note);
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
    }

    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private NoteAdapter mAdapter;

        private List<Note> noteList;

        public SwipeToDeleteCallback(NoteAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mAdapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            noteList = NoteSingleton.getInstance(getActivity()).getNoteList(null);
            Note note = noteList.get(position);
            NoteSingleton.getInstance(getActivity()).deleteNote(note, NoteDb.NoteTable.Cols.UUID + "=?", new String[]{note.getId().toString()});
            Log.d(TAG, "onSwiped: " + note.getId().toString());
            noteList = NoteSingleton.getInstance(getActivity()).getNoteList(null);
            updateUI(null);
            Snackbar.make(getView(), "note delete", Snackbar.LENGTH_SHORT).show();
            //mAdapter.(position);

        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.item_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                updateUI(NoteDb.NoteTable.Cols.DATE);
                break;
            case R.id.elswe:
                Toast.makeText(getActivity(), "This", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(null);
    }
}
