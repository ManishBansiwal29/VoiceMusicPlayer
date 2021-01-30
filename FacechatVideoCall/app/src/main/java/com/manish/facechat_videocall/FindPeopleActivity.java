package com.manish.facechat_videocall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindPeopleActivity extends AppCompatActivity {
    private RecyclerView findFriendsList;
    private EditText searchET;
    private String str="";

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchET = findViewById(R.id.search_user_text);




        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count) {
                if (searchET.getText().toString().equals("")){
                    Toast.makeText(FindPeopleActivity.this , "Please enter name..." , Toast.LENGTH_SHORT).show();
                }else{
                    str=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findFriendsList = findViewById(R.id.find_friends_list);
        findFriendsList.setHasFixedSize(true);
        findFriendsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = null;

        if (str.equals("")){
            options = new FirebaseRecyclerOptions.Builder<Contacts>()
                    .setQuery(usersRef,Contacts.class)
                    .build();
        }else {
            options = new FirebaseRecyclerOptions.Builder<Contacts>()
                    .setQuery(usersRef.orderByChild("name")
                    .startAt(str)
                    .endAt(str + "\uf8ff"),Contacts.class)
            .build();
        }

        FirebaseRecyclerAdapter<Contacts,FindPeopleViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Contacts, FindPeopleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPeopleViewHolder holder, final int i, @NonNull final Contacts contacts) {
                holder.userNameTxt.setText(contacts.getName());
                Picasso.get().load(contacts.getImage()).into(holder.profileImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(i).getKey();


                        Intent intent = new Intent(FindPeopleActivity.this,ProfileActivity.class);
                        intent.putExtra("visit_user_id", visit_user_id);
                        intent.putExtra("profile_name",contacts.getName());
                        intent.putExtra("profile_image",contacts.getImage());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FindPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_design,parent,false);
                FindPeopleViewHolder viewHolder = new FindPeopleViewHolder(view);
                return viewHolder;
            }
        };

        findFriendsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();


    }

    public static class FindPeopleViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameTxt;
        Button videoCallBtn;
        ImageView profileImageView;
        RelativeLayout cardView1;

        public FindPeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTxt=itemView.findViewById(R.id.name_contact);
            videoCallBtn=itemView.findViewById(R.id.call_btn);
            profileImageView=itemView.findViewById(R.id.image_contact);
            cardView1=itemView.findViewById(R.id.card_view1);

            videoCallBtn.setVisibility(View.GONE);
        }
    }

}
