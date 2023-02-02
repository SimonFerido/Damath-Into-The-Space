package com.unityx.damath.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.MainActivity;
import com.unityx.damath.PlayerManager;
import com.unityx.damath.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

//Console Firebase


public class RegistrationActivity extends AppCompatActivity {

    private PlayerManager playerManager = new PlayerManager();
    private RoomManager roomManager = new RoomManager();
    private Player player;

    private View decorView;
    VideoView videoView;

    //MEDIAPLAYER
    MediaPlayer backgroundSound;
    MediaPlayer clickSound;

    //SIGN UP
    private EditText regUsername;
    private EditText regEmailSignUp;
    private EditText regPassword, regCPassword;
    private Button btnSignup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //AUTHENTICATION
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refUsername;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Background Video
        decorView = getWindow().getDecorView();

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refUsername = database.getReference("username");

        //Hide SystemUI System Navigation and StatusBar
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        //*************************** END OF SYSTEM UI CONFIGURATION *********************************//

        //************************* START OF USER AUTHENTICATION (SIGN-UP) ***************************//

        regUsername = findViewById(R.id.textInputEditTextUser);
        regEmailSignUp = findViewById(R.id.textInputEditTextEmail);
        regPassword = findViewById(R.id.textInputEditPassword);
        regCPassword = findViewById(R.id.textInputEditCPassword);
        btnSignup = findViewById(R.id.btn_sign_up);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = regUsername.getText().toString();
                final String email = regEmailSignUp.getText().toString();
                final String password = regPassword.getText().toString();
                final String cpassword = regCPassword.getText().toString();
                boolean duplicateCopy =playerManager.getDuplicateUsername(username);
                if(duplicateCopy == true) {
                    Toast.makeText(RegistrationActivity.this, "Username Taken", Toast.LENGTH_LONG).show();
                }else if(!email.matches(emailPattern)){
                    regEmailSignUp.setError("Invalid Email");
                    Toast.makeText(RegistrationActivity.this, "Please input character a-z,", Toast.LENGTH_SHORT).show();
                }if (!email.equals("") && !password.equals("") && !username.equals("") && duplicateCopy == false){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Failed Sign Up", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Signed Up!", Toast.LENGTH_LONG).show();
                                //***store the sign up user info to the database***//
                                player = playerManager.createPlayer(username,email);
                                refSignUpPlayers.child(username).setValue(player);

                                refUsername.child(username).setValue(username);

                                user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();

                                user.updateProfile(profileUpdates);
                                mAuth.signOut();
                                Log.d("Step","Step2");

                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegistrationActivity.this, "Failed Sign in", Toast.LENGTH_LONG).show();
                                        } else {
                                            user = mAuth.getCurrentUser();
                                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
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

}