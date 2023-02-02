package com.unityx.damath.Damath.GameMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.GameMode.Fraction.FractionRoomActivity;
import com.unityx.damath.Damath.GameMode.Polynomial.PolynomialRoomActivity;
import com.unityx.damath.Damath.GameMode.Radical.DamathRoomActivity;
import com.unityx.damath.Damath.Room;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import java.util.ArrayList;
import java.util.List;

public class GameModeActivity extends AppCompatActivity {

    private ImageButton btnRadical, btnPolynomial, btnFraction;

    private DatabaseReference refRoom;
    private static DatabaseReference refThisRoom;

    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    MediaPlayer clickSound;
    VideoView videoView;

    private View decorView;

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
        setContentView(R.layout.activity_game_mode);

        clickSound = MediaPlayer.create(GameModeActivity.this, R.raw.click);

//**********************************

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");

        videoView = (VideoView) findViewById(R.id.videoView);
        decorView = getWindow().getDecorView();

        String path = "android.resource://com.unityx.damath/" + R.raw.bgstars;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();
        //Background Video
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (user != null) {//(double check)when user sign up in the mainActivity, this listener will be called, but the activity has not been called, so there is no user
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
        btnRadical = findViewById(R.id.btn_radical);
        btnRadical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent radical = new Intent (GameModeActivity.this, DamathRoomActivity.class);
                startActivity(radical);
            }
        });
        btnPolynomial = findViewById(R.id.btn_polynomial);
        btnPolynomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent polynomial = new Intent (GameModeActivity.this, PolynomialRoomActivity.class);
                startActivity(polynomial);
            }
        });
        btnFraction = findViewById(R.id.btn_fraction);
        btnFraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fraction = new Intent(GameModeActivity.this, FractionRoomActivity.class);
                startActivity(fraction);
            }
        });

        Button backBtn = findViewById(R.id.btnBackID);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        videoView.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();

    }

}
