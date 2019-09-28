package com.example.tallerlocation;

import androidx.appcompat.app.AppCompatActivity;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;


public class Contacts extends AppCompatActivity {


    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        list=(ListView)findViewById(R.id.listi);

        String[] mProjection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };

        Cursor mCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
        ContactsAdapter mContactsAdapter = new ContactsAdapter(this, null, 0);

        mContactsAdapter.changeCursor(mCursor);
        list.setAdapter(mContactsAdapter);


    }
}
