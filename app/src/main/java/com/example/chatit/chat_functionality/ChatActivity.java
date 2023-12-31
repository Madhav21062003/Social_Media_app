package com.example.chatit.chat_functionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chatit.ProfileViewHolder;
import com.example.chatit.R;
import com.example.chatit.ReplyActivity;
import com.example.chatit.ViewHolderQuestions;
import com.example.chatit.models.All_UserMmber;
import com.example.chatit.models.QuestionMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference profileRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    RecyclerView recyclerView;
    EditText searchEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        searchEt = findViewById(R.id.search_user_ch);
        recyclerView = findViewById(R.id.rv_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        profileRef = database.getReference("All Users");

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    String query = searchEt.getText().toString().toUpperCase();
                Query search = profileRef.orderByChild("name").startAt(query).endAt(query+"\uf0ff");

                FirebaseRecyclerOptions<All_UserMmber> options1 =
                        new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                                .setQuery(search, All_UserMmber.class)
                                .build();

                FirebaseRecyclerAdapter<All_UserMmber, ProfileViewHolder> firebaseRecyclerAdapter1 =
                        new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull All_UserMmber model) {

                                final  String postKey = getRef(position).getKey();
                                holder.setProfileInChat(getApplication(), model.getName(), model.getUid(), model.getProf(), model.getUrl());

                                String name = getItem(position).getName();
                                String url = getItem(position).getUrl();
                                String uid = getItem(position).getUid();

                                holder.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                                        intent.putExtra("n", name);
                                        intent.putExtra("u", url);
                                        intent.putExtra("uid", uid);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent, false);
                                return new ProfileViewHolder(view);
                            }
                        };

                firebaseRecyclerAdapter1.startListening();
                recyclerView.setAdapter(firebaseRecyclerAdapter1);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<All_UserMmber> options1 =
                new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(profileRef, All_UserMmber.class)
                        .build();

        FirebaseRecyclerAdapter<All_UserMmber   , ProfileViewHolder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull All_UserMmber model) {

                            final  String postKey = getRef(position).getKey();
                            holder.setProfileInChat(getApplication(), model.getName(), model.getUid(), model.getProf(), model.getUrl());

                            String name = getItem(position).getName();
                            String url = getItem(position).getUrl();
                            String uid = getItem(position).getUid();

                            holder.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                                    intent.putExtra("n", name);
                                    intent.putExtra("u", url);
                                    intent.putExtra("uid", uid);
                                    startActivity(intent);
                                }
                            });
                    }

                    @NonNull
                    @Override
                    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_profile_item,parent, false);
                        return new ProfileViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);



    }
}