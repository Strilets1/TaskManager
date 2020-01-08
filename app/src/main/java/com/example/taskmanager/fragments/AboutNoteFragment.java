package com.example.taskmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.taskmanager.NoteSingleton;
import com.example.taskmanager.R;
import com.example.taskmanager.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AboutNoteFragment extends Fragment {

    private static final String DATA_DIALOG = "date_dialog";
    private static final String ARG_NOTE_ID = "id_note";
    private static final String TAG = "AboutNoteFragment";

    private static final int REQUEST_DATE = 0;

    private Note mNote;
    private UUID uuid;
    private AppCompatButton mSetDateBtn;
    private AppCompatEditText mTitleEt, mDescriptionEt;
    private AppCompatTextView mDateTv;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onNoteUpdate(Note note);
    }

    public static AboutNoteFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, uuid);

        AboutNoteFragment noteFragment = new AboutNoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteSingleton.getInstance(getActivity()).getNote(uuid);
        Log.d(TAG, "uuid: " + uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_note_fragment, container, false);

        mSetDateBtn = view.findViewById(R.id.set_date_btn);
        mTitleEt = view.findViewById(R.id.enter_title_et);
        mDescriptionEt = view.findViewById(R.id.enter_description_et);
        mDateTv = view.findViewById(R.id.date_tv);

        updateDate();

        mSetDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mNote.getDate());
                datePickerFragment.setTargetFragment(AboutNoteFragment.this, 0);
                datePickerFragment.show(fragmentManager, DATA_DIALOG);
            }
        });

        mTitleEt.setText(mNote.getTitle());
        mTitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
                updateNote();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDescriptionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setDescription(s.toString());
                updateNote();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void updateNote() {
        NoteSingleton.getInstance(getActivity()).updateNotes(mNote);
        mCallbacks.onNoteUpdate(mNote);
    }

    private void updateDate(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date date = mNote.getDate();
        mDateTv.setText(dateFormat.format(date).toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE_EXTRA);
            mNote.setDate(date);
            updateDate();
            updateNote();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        NoteSingleton.getInstance(getActivity()).updateNotes(mNote);
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

}
