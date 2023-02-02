package com.unityx.damath.User;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unityx.damath.MainActivity;
import com.unityx.damath.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//Console Firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private View decorView;
    VideoView videoView;

    //LoginActivity
    private Button btnSignin, btnSignup;
    private Button btnGuest;
    private ImageButton Damath;
    private TextView txtSignup;
    private TextView tvEmail, tvPass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //MediaPlayer
    private MediaPlayer backgroundSound;
    MediaPlayer clickSound;

    //FirebaseAuth
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refUsername;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private static final String TAG = "DebugMainActivity";

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        clickSound = MediaPlayer.create(LoginActivity.this, R.raw.click);

        //Background Video
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
        //Hide SystemUI System Navigation and StatusBar
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://mail.google.com/mail/u/0/")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.unityx.damath",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();

        //*************************** END OF SYSTEM UI CONFIGURATION *********************************//

        //**************************** START OF USER AUTHENTICATION **********************************//

        //User Authentication
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refUsername = database.getReference("username");

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        txtSignup = findViewById(R.id.txt_signup);
        Damath = findViewById(R.id.damathLogin);
        Damath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity();
            }
        });
        txtSignup.setOnClickListener(v -> openRegister());
    }

    private void loginActivity() {

        final Dialog loginDialog = new Dialog(LoginActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.login,null);

        tvEmail = dialogView.findViewById(R.id.textInputEmail);
        tvPass = dialogView.findViewById(R.id.textInputPassword);

        //User Sign In
        btnSignin  = dialogView.findViewById(R.id.sign_in);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                String email = tvEmail.getText().toString();
                String password = tvPass.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter Your Email/Password.", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    tvEmail.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Please input character a-z,", Toast.LENGTH_SHORT).show();
                }else if(password.length()<8){
                    tvPass.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Input 8 Character Password.", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Incorrect Email or Password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        loginDialog.setContentView(dialogView);
        loginDialog.show();
    }

    public void openRegister(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

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

    public void unAvailable(View view) {
        Toast.makeText(LoginActivity.this, "Not Yet Available",Toast.LENGTH_SHORT).show();
    }
}






