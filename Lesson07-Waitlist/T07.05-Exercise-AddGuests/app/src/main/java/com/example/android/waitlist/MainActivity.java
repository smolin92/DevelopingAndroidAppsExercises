package com.example.android.waitlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.waitlist.data.TestUtil;
import com.example.android.waitlist.data.WaitlistContract;
import com.example.android.waitlist.data.WaitlistDbHelper;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;

    // DONE (1) Create local EditText members for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;

    // DONE (13) Create a constant string LOG_TAG that is equal to the class.getSimpleName()
    public final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // DONE (2) Set the Edit texts to the corresponding views using findViewById
        this.mNewGuestNameEditText = (EditText) findViewById(R.id.person_name_edit_text);
        this.mNewPartySizeEditText = (EditText) findViewById(R.id.party_count_edit_text);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // DONE (3) Remove this fake data call since we will be inserting our own data now

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);

    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {

        // DONE (9) First thing, check if any of the EditTexts are empty, return if so
        if (this.mNewGuestNameEditText.getText().length() == 0 ||
                this.mNewPartySizeEditText.getText().length() == 0){
            return;
        }

        // DONE (10) Create an integer to store the party size and initialize to 1
        int partySize = 1;

        // DONE (11) Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
        try {
            // DONE (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
            partySize = Integer.parseInt(this.mNewPartySizeEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // DONE (14) call addNewGuest with the guest name and party size
        String partyName = this.mNewGuestNameEditText.getText().toString();
        addNewGuest(partyName, partySize);

        // DONE (19) call mAdapter.swapCursor to update the cursor by passing in getAllGuests()
        mAdapter.swapCursor(getAllGuests());

        // DONE (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        this.mNewPartySizeEditText.getText().clear();
        this.mNewGuestNameEditText.getText().clear();
        this.mNewPartySizeEditText.clearFocus();
    }



    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }

    // DONE (4) Create a new addGuest method
    private long addNewGuest(String name, int size) {
        // DONE (5) Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues contentValues = new ContentValues();

        // DONE (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        contentValues.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);

        // DONE (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        contentValues.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, size);

        // DONE (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, contentValues);
    }

}