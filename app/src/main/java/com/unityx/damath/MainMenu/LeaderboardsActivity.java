package com.unityx.damath.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Leaderboard.ScoreAdapter;
import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers, databaseReference;
    private FirebaseUser user;

    private Player player;
    List<Player>list;
    ScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        recyclerView = findViewById(R.id.LeadViewID);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Signed Up Players");

        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();
            }
        });
        refSignUpPlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = mAuth.getCurrentUser();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(user != null) {//(double check)when user sign up in the mainActivity, this listener will be called, but the activity has not been called, so there is no user
                        if (dataSnapshot1.getValue(Player.class).getUsername().equals(user.getDisplayName())) {
                            player = dataSnapshot1.getValue(Player.class);

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.orderByChild("win").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = mAuth.getCurrentUser();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Player data = snapshot.getValue(Player.class);
                    list.add(data);
                }
                adapter = new ScoreAdapter(list, LeaderboardsActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LeaderboardsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button Backbtn = findViewById(R.id.btnBackID);
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

