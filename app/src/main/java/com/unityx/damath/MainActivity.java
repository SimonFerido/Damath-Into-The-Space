package com.unityx.damath;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.GameMode.GameModeActivity;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.MainMenu.CustomizeActivity;
import com.unityx.damath.MainMenu.HowToPlayActivity;
import com.unityx.damath.MainMenu.LeaderboardsActivity;
import com.unityx.damath.User.Player;
import com.unityx.damath.MainMenu.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    private PlayerManager playerManager = new PlayerManager();
    private RoomManager roomManager = new RoomManager();
    private Player player;

    private ImageButton ibtnDamath;
    private ImageButton userProfile;
    private ImageButton userCustom;
    private ImageButton userHowto;
    private ImageButton userLeaderbaords;
    private TextView tvGreeting, tvVersionCode;

    //MEDIAPLAYER
    private View decorView;
    private ViewPager2 viewPager2;
    VideoView videoView;
    public MediaPlayer backgroundSound;
    MediaPlayer clickSound;
    private ImageButton btnVolume;
    private boolean backgroundRunning;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refUsername, refVersion;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //**************************** START OF UI CONFIGURATION *********************************//

        clickSound = MediaPlayer.create(MainActivity.this, R.raw.click);
        backgroundSound = MediaPlayer.create(MainActivity.this, R.raw.adventure);
        backgroundSound.setLooping(true);
        backgroundSound.start();

        videoView = (VideoView) findViewById(R.id.videoView);
        decorView = getWindow().getDecorView();

        btnVolume = findViewById(R.id.ibVolume);

        //GAME UPDATE IF NOT UPDATED
        String currentVersion = "10"; // replace with the current version of the game
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String installedVersion = packageInfo.versionName;
            if (!installedVersion.equals(currentVersion)) {
                // prompt user to update the game
                Uri updateUri = Uri.parse("https://play.google.com/store/apps/details?id=com.unityx.damath&gl=US" + getPackageName());
                Intent updateIntent = new Intent(Intent.ACTION_VIEW, updateUri);
                startActivity(updateIntent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
        //Hide SystemUI System Navigation and StatusBar
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        tvGreeting = findViewById(R.id.tvGreetingID);
        ibtnDamath = findViewById(R.id.ibtnDamathID);
        ibtnDamath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameModeActivity.class);
                startActivity(intent);
            }
        });
        tvGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
            }
        });

        userProfile = findViewById(R.id.btn_acount);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        userCustom = findViewById(R.id.btn_custom);
        userCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, CustomizeActivity.class);
                startActivity(intent);
            }
        });

        userHowto = findViewById(R.id.btn_h2p);
        userHowto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, HowToPlayActivity.class);
                startActivity(intent);
            }
        });

        userLeaderbaords = findViewById(R.id.btn_lead);
        userLeaderbaords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, LeaderboardsActivity.class);
                startActivity(intent);
            }
        });

        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteUnmute();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refUsername = database.getReference("username");
        refVersion = database.getReference("version");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("Step","Step1");
                user = mAuth.getCurrentUser();
                if(user != null){
                    if(user.getDisplayName()!= null) {
                        //user is signed in
                        Log.d(TAG, "user signed in");
                        tvGreeting.setText("Hello, " + user.getDisplayName());
                        tvGreeting.setVisibility(View.VISIBLE);
                        ibtnDamath.setEnabled(true);
                    }
                    else{
                        //User is signed out
                        Log.d(TAG,"user signed out");
                        tvGreeting.setVisibility(View.GONE);
                        tvGreeting.setVisibility(View.GONE);
                        ibtnDamath.setEnabled(false);
                    }

                }
                else{
                    //User is signed out
                    Log.d(TAG,"user signed out");
                    tvGreeting.setVisibility(View.GONE);
                    ibtnDamath.setEnabled(false);
                }
            }
        };

        refUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot username : dataSnapshot.getChildren()){
                    playerManager.getUsernameList().add(username.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void muteUnmute() {
        if(backgroundRunning){
            muteSound();
        } else {
            unMuteSound();
        }
    }

    private void muteSound() {
        backgroundSound.pause();
        btnVolume.setImageResource(R.drawable._ic_volume_mute);
        backgroundRunning = false;
    }

    private void unMuteSound(){
        backgroundSound.start();
        btnVolume.setImageResource(R.drawable._ic_volume_up);
        backgroundRunning = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
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
        backgroundSound.stop();
        videoView.stopPlayback();
        super.onDestroy();

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}