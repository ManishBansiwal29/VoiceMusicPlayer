package com.manish.recyclerviewexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView contactsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Contact> contacts = new ArrayList<>();

        contactsRecyclerView=findViewById(R.id.contactsRecView);

        contacts.add(new Contact("manish","manish@gmail.com","https://images.freeimages.com/images/large-previews/ed3/a-stormy-paradise-1-1563744.jpg"));
        contacts.add(new Contact("manish","manish@gmail.com","https://pbs.twimg.com/profile_images/674584056751722497/A59Y4r5w_400x400.jpg"));
        contacts.add(new Contact("manish","manish@gmail.com","https://pbs.twimg.com/profile_images/674584056751722497/A59Y4r5w_400x400.jpg"));
        contacts.add(new Contact("manish","manish@gmail.com","https://pbs.twimg.com/profile_images/674584056751722497/A59Y4r5w_400x400.jpg"));

        RecViewAdapter adapter = new RecViewAdapter(this);
        adapter.setContacts(contacts);
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

    }
}

