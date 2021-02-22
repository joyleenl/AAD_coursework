package com.example.llesson1.notes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.llesson1.R;
import com.example.llesson1.notes.adapters.NotesAdapters;
import com.example.llesson1.notes.database.NotesDatabase;
import com.example.llesson1.notes.entities.Note;
import com.example.llesson1.notes.listeners.NotesListeners;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity implements NotesListeners {

    public static final int REQUEST_CODE_ADD_NOTE =1; //to add new note
    public static final int REQUEST_CODE_UPDATE_NOTE =2; //request code to update note
    public static final int REQUEST_CODE_SHOW_NOTE = 3; //req code to display all notes

    private RecyclerView notesRecyclerView; // for the reclycerview
    private List<Note>noteList; //note list
    private NotesAdapters notesAdapters; // view and update note

    private int noteClickedPosition= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //click the button on the bottom left and then prompt the createnote activity
        ImageView imageAddNote = findViewById(R.id.imageAddNote);
        imageAddNote.setOnClickListener((v) ->
                {
                    startActivityForResult(
                            new Intent(getApplicationContext(), CreateNote.class),
                            REQUEST_CODE_ADD_NOTE
                    );
                }
        );

    //recycler view
        notesRecyclerView = findViewById(R.id.notesRecylerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        noteList = new ArrayList<>();
        notesAdapters = new NotesAdapters(noteList, this);
        notesRecyclerView.setAdapter(notesAdapters);


        getNotes(REQUEST_CODE_SHOW_NOTE,false);// getNotes method called from onCreate() method of an activity
        // we are passing REQUEST_CODE_SHOW_NOTES to get nots method
        // it means the app just started and need to display all notes from db
        //the param isNoteDeleted is passed as false

        //search for notes
        EditText inputSearch = findViewById(R.id.InputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapters.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size()!= 0){
                    notesAdapters.searchNotes(s.toString());
                }
            }
        });
    }

    // view or update note
    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNote.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }
    //like async task to save note, we need to get notes from the db
    private void getNotes(final int requestCode, final boolean isNoteDeleted) { // getting the req code as a method param
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                //req code =REQUEST_CODE_SHOW_NOTE
                // add all notes fr db to noteList and notify adapter abt new data set
                if (requestCode == REQUEST_CODE_SHOW_NOTE) {
                    noteList.addAll(notes);
                    notesAdapters.notifyDataSetChanged();
                }
                //req code = REQUEST_CODE_ADD_NOTE
                //add only first note (newly added) from the db to noteList
                // notify the adapter for the newly inserted item and scroll recycler view to top
                else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapters.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                }
                //req code= REQUEST_CODE_UPDATE_NOTE
                //remove note from list
                //check if deleted
                //if deleted : notify adapter abt item removed
                //if not deleted: updated >> add a newly updated note to ihe same position where we remove n notify adapter abt item change
                else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        notesAdapters.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapters.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }

        new  GetNotesTask().execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE,false); //getNotes() method called fr onActivityResult() method
            // check the current req code is for add note, result is RESULT_OK
            //new note added from CreateNote, result= sent back to this activity
            // thats why pass REQUEST_CODE ADD NOTE TO getNotes() method
            //the param isNoteDeleted is passed as false
        }else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if (data != null){
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted", false));  // same as before but req code for update note
                //avail note updated fr CreateNote activity and sent result back to this
                // thta's why pass REQ_CODE_UPDTAE_NOTE to that method
                //may be possible that notes get deleted
                // the param isNoteDeleted passing value fr CreateNote wether the nore is deleted or not using intent data key= isNoteDeleted
            }
        }
    }
}