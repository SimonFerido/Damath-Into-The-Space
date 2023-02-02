package com.unityx.damath.MainMenu;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.Room;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.PlayerManager;
import com.unityx.damath.R;
import com.unityx.damath.User.LoginActivity;
import com.unityx.damath.User.Player;

public class UserProfileActivity extends AppCompatActivity {

    private PlayerManager playerManager = new PlayerManager();

    private Player player;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers, refUsername;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    VideoView videoView;

    private RoomManager roomManager = new RoomManager();
    private Room room;

    private TextView tvRoom;
    private TextView tvPlayer1Name, tvEmail, tvPlayerWin, tvPlayerLost, tvPlayerWinRate, tvUserID;

    private Button userout, edituser;

    private MediaPlayer backgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        //GET WIN RATE DETAILS
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        refSignUpPlayers = database.getReference().child("Signed Up Players");
        refUsername = database.getReference("username");

        tvPlayer1Name = findViewById(R.id._text_username);
        tvEmail = findViewById(R.id._user_email);
        tvPlayerWin = findViewById(R.id._text_wins);
        tvPlayerLost = findViewById(R.id._text_lose);
        tvPlayerWinRate = findViewById(R.id._text_winrate);

        videoView = (VideoView) findViewById(R.id.videoView);

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
        refUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot username : snapshot.getChildren()){
                    user = mAuth.getCurrentUser();
                    playerManager.getUsernameList().add(username.getValue(String.class));
                    tvPlayer1Name.setText("" + user.getDisplayName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //---------------------------USER ACCOUNT-----------------------//
        refSignUpPlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot1 : snapshot.getChildren()){
                    if(datasnapshot1.getValue(Player.class).getUsername().equals(user.getDisplayName())){
                        user = mAuth.getCurrentUser();
                        player = datasnapshot1.getValue(Player.class);
                        tvEmail.setText(getResources().getText(R.string._user_email) + player.getEmail());
                        tvPlayerWin.setText(" " + player.getWin());
                        tvPlayerLost.setText(" " + player.getLoss());
                        if(player.getWinningRate() != null) {
                            tvPlayerWinRate.setText(" " + player.getWinningRate());
                        } else {
                            tvPlayerWinRate.setText("0");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userout = findViewById(R.id._user_logout);
        userout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(UserProfileActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                backgroundSound.stop();
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

    @Override
    protected void onResume() {
        if (this.isFinishing() == false){
            videoView.resume();
            super.onResume();
        }
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