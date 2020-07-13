package com.example.smokingcessationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ContactActivity extends AppCompatActivity {
    private ContactListAdapter contactListAdapter = new ContactListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Button btAdd = (Button) findViewById(R.id.ContactFragment_btAddContact);
        Button btDel = (Button) findViewById(R.id.ContactFragment_btDelContact);
        final ListView lvContacts = (ListView) findViewById(R.id.ContactFragment_lvContacts);
        lvContacts.setAdapter(contactListAdapter);

        btAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, ContactInputActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        btDel.setOnClickListener(new Button.OnClickListener() {
            SparseBooleanArray checkedItems;
            @Override
            public void onClick(View view) {
                checkedItems = lvContacts.getCheckedItemPositions();
                int i_cnt = contactListAdapter.getCount();

                for (int i = i_cnt - 1; i >= 0; i--)
                    if (checkedItems.get(i))
                        contactListAdapter.removeItem(i);
                lvContacts.clearChoices();
                contactListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            // 아이템 추가
            contactListAdapter.addItem(data.getStringExtra("name"), data.getStringExtra("phoneNum"));
            contactListAdapter.notifyDataSetChanged();
        }
    }
}