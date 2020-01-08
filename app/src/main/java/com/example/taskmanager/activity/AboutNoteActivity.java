package com.example.taskmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.taskmanager.NoteSingleton;
import com.example.taskmanager.R;
import com.example.taskmanager.fragments.AboutNoteFragment;
import com.example.taskmanager.model.Note;

import java.util.List;
import java.util.UUID;

public class AboutNoteActivity extends AppCompatActivity implements AboutNoteFragment.Callbacks {

    ViewPager mViewPager;
    List<Note> mNoteList;

    private static final String NOTE_ID = "note_id";

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, AboutNoteActivity.class);
        intent.putExtra(NOTE_ID, uuid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        mViewPager = findViewById(R.id.view_pager);
        mNoteList = NoteSingleton.getInstance(this).getNoteList(null);

        UUID uuid = (UUID) getIntent().getSerializableExtra(NOTE_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setPageMargin(50);
        mViewPager.setPadding(40, 40, 0, 0);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Note crime = mNoteList.get(position);
                return AboutNoteFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mNoteList.size();
            }
        });


        for (int i = 0; i < mNoteList.size(); i++) {
            if (mNoteList.get(i).getId().equals(uuid)) {
                mViewPager.setCurrentItem(i);
                break;
            }

        }
    }

    @Override
    public void onNoteUpdate(Note note) {

    }
}
