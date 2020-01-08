package com.example.taskmanager.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.fragments.AboutNoteFragment;
import com.example.taskmanager.fragments.ListFragments;
import com.example.taskmanager.model.Note;

public class ListNoteActivity extends SingleFragmentActivity
        implements ListFragments.Callbacks, AboutNoteFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new ListFragments();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onNoteSelected(Note note) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AboutNoteActivity.newIntent(this, note.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = AboutNoteFragment.newInstance(note.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onNoteUpdate(Note note) {
        ListFragments listFragment = (ListFragments) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI(null);
    }
}
