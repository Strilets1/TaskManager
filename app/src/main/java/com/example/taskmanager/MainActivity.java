package com.example.taskmanager;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.taskmanager.activity.AboutNoteActivity;
import com.example.taskmanager.activity.SingleFragmentActivity;
import com.example.taskmanager.fragments.AboutNoteFragment;
import com.example.taskmanager.fragments.ListFragments;
import com.example.taskmanager.model.Note;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {

    private static final String CRIME_ID = "some_id";


    @Override
    protected Fragment createFragment() {
        //return new CrimeFragment();
        UUID uuid = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        return AboutNoteFragment.newInstance(uuid);
    }


}
