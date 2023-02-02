package com.unityx.damath.Damath.GameMode.Polynomial;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.Room;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import java.util.ArrayList;
import java.util.List;

public class PolynomialRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterPolynomial recyclerViewAdapter;

    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refRoom;
    private DatabaseReference refUnavailableRoom;
    private FirebaseAuth mAuth;

    MediaPlayer clickSound;

    private Button btnCreate;
    private EditText etRoom;
    private Button btnJoin;
    private Player player;
    private Room room;
    private FirebaseUser user;
    private RoomManager roomManager = new RoomManager();
    private List<Integer> unavailableRoomIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_polynomial_room);
        clickSound = MediaPlayer.create(PolynomialRoomActivity.this, R.raw.click);

//**********RecyclerView***********
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//**********************************

        btnCreate = findViewById(R.id.btnCreateRoomID);
        etRoom = findViewById(R.id.etRoomID);
        btnJoin = findViewById(R.id.btnjoinID);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refRoom = database.getReference("Room").child("polynomial").child("available");
        refUnavailableRoom = database.getReference("Room").child("polynomial").child("unavailable");

        Button backBtn = findViewById(R.id.btnBackID);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        refRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomManager.getRoomList().clear();
                roomManager.getIdList().clear();
                for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()){
                    roomManager.getRoomList().add(roomSnapshot.getValue(Room.class));
                    roomManager.getIdList().add(roomSnapshot.getValue(Room.class).getId());

                }
                recyclerViewAdapter = new RecyclerViewAdapterPolynomial(PolynomialRoomActivity.this, roomManager.getRoomList());
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refUnavailableRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unavailableRoomIdList.clear();
                for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()){
                    unavailableRoomIdList.add(roomSnapshot.getValue(Room.class).getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                room = roomManager.createRoom(player);
                refRoom.child(String.valueOf(room.getId())).setValue(room);
                Intent intent = new Intent(PolynomialRoomActivity.this, BlueDamathPolynomialActivity.class);
                intent.putExtra("room",room);
                startActivity(intent);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                Boolean exist = false;
                if (!etRoom.getText().toString().equals("")){
                    int roomNum = Integer.parseInt(etRoom.getText().toString());
                    if (roomNum != 0) {
                        for (int i = 0; i < roomManager.getRoomList().size(); i++) {
                            if (roomNum == roomManager.getRoomList().get(i).getId()) {
                                exist = true;
                                Room room = roomManager.getRoomList().get(i);
                                room.setPlayer2(player);
                                room.setAvailability(false);
                                refRoom.child(String.valueOf(room.getId())).setValue(room);
                                Intent intent = new Intent(PolynomialRoomActivity.this, PinkDamathPolynomialActivity.class);
                                intent.putExtra("room", room);
                                PolynomialRoomActivity.this.startActivity(intent);
                                break;
                            }
                        }
                        if (exist == false) {
                            Toast.makeText(PolynomialRoomActivity.this, "Room Doesn't Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{//join random room
                    if(roomManager.getRoomList().size() == 0){
                        Toast.makeText(PolynomialRoomActivity.this, "No Available Rooms", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int min = 0;
                        int max = roomManager.getRoomList().size();
                        int range = (roomManager.getRoomList().size() - 1) + 1;
                        int random = (int) (Math.random() * range) + min;
                        Room room = roomManager.getRoomList().get(random);
                        room.setPlayer2(player);
                        room.setAvailability(false);
                        refRoom.child(String.valueOf(room.getId())).setValue(room);
                        Intent intent = new Intent(PolynomialRoomActivity.this, PinkDamathPolynomialActivity.class);
                        intent.putExtra("room", room);
                        PolynomialRoomActivity.this.startActivity(intent);
                    }
                }
            }
        });
    }
}