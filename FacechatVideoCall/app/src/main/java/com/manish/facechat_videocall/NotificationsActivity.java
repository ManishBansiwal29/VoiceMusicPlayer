package com.manish.facechat_videocall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationList;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference friendRequestRef,contactsRef,usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        friendRequestRef= FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        contactsRef=FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        notificationList = findViewById(R.id.notification_list);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(friendRequestRef.child(currentUserId),Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts,NotificationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder , int i , @NonNull Contacts contacts) {

            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friend_design,parent,false);
                NotificationViewHolder notificationViewHolder = new NotificationViewHolder(view);
                return notificationViewHolder;
            }
        };

        notificationList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameTxt;
        Button acceptBtn, cancelBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTxt=itemView.findViewById(R.id.name_notification);
            acceptBtn=itemView.findViewById(R.id.request_accept_btn);
            cancelBtn=itemView.findViewById(R.id.request_decline_btn);
            profileImageView=itemView.findViewById(R.id.image_notification);
            cardView=itemView.findViewById(R.id.card_view);
        }
    }
}
