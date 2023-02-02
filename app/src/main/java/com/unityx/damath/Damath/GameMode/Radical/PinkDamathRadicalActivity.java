package com.unityx.damath.Damath.GameMode.Radical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.BlueDamath;
import com.unityx.damath.Damath.Damath;
import com.unityx.damath.Damath.NullChecker;
import com.unityx.damath.Damath.Operation.DamathCalculator;
import com.unityx.damath.Damath.PinkDamath;
import com.unityx.damath.Damath.Room;
import com.unityx.damath.Damath.RoomManager;
import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import com.unityx.damath.Damath.GameMode.Radical.BlueDamathRadicalActivity;

import java.util.ArrayList;
import java.util.List;

public class PinkDamathRadicalActivity extends AppCompatActivity {

    private ImageButton[][] imageButtonList;
    //private Checker[][] damathList;
    private List<List<Damath>> damathList;
    private ArrayList<int[]> possibleMove;
    private ArrayList<int[]> killList;
    private int [] killLocation;
    private boolean turn = true; //true for black's turn, false for white's turn
    private boolean secondClick = false;
    private boolean destroyed = false;
    private int row;
    private int column;
    private boolean disableAllButOneButton = false;
    private Damath nullc = new NullChecker();

    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;

    private int round = 0;//for tracking the number of turns, if round is even, show the toast saying "your turn"

    MediaPlayer clickSound;
    private int counter;

    //==========COUNTDOWN TIMER==============//
    private TextView mTextTimer, pTimer;
    private CountDownTimer mTimer;
    private long timeLeftInMilliseconds = 2400000;

    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refRoom;
    private DatabaseReference refThisRoom;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private RoomManager roomManager = new RoomManager();
    private Player player;//Red
    private Player player1;//Black
    private Room room;
    private boolean player1Exited = false;
    private boolean backPressed = false;
    private boolean paused = false;

    private TextView tvRoom;
    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;

    //==========CALCULATOR VARIABLES=========//
    private Button btnPlus, btnMinus, btnMultiply, btnDivide, btnRadical, btnOpenP, btnCloseP, btnAC, btnPeriod, btnEquals, btnDelete, btnPow, btnOk;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private TextView tvWorkings, tvResult;
    private TextView resultsTV, signBox;
    private TextView workingsTV;
    private String workings = "";
    private String formula = "";
    private String tempFormula = "";
    private String sign, value1, value2;
    private Double num1, num2, result;

    private boolean hasDot = false;

    public TextView player1Score, player2Score;
    public TextView player1ScoreID;

    //row 1
    private ImageButton ibtn_0_0;
    private ImageButton ibtn_0_1;
    private ImageButton ibtn_0_2;
    private ImageButton ibtn_0_3;
    private ImageButton ibtn_0_4;
    private ImageButton ibtn_0_5;
    private ImageButton ibtn_0_6;
    private ImageButton ibtn_0_7;
    //row 2
    private ImageButton ibtn_1_0;
    private ImageButton ibtn_1_1;
    private ImageButton ibtn_1_2;
    private ImageButton ibtn_1_3;
    private ImageButton ibtn_1_4;
    private ImageButton ibtn_1_5;
    private ImageButton ibtn_1_6;
    private ImageButton ibtn_1_7;
    //row 3
    private ImageButton ibtn_2_0;
    private ImageButton ibtn_2_1;
    private ImageButton ibtn_2_2;
    private ImageButton ibtn_2_3;
    private ImageButton ibtn_2_4;
    private ImageButton ibtn_2_5;
    private ImageButton ibtn_2_6;
    private ImageButton ibtn_2_7;
    //row 4
    private ImageButton ibtn_3_0;
    private ImageButton ibtn_3_1;
    private ImageButton ibtn_3_2;
    private ImageButton ibtn_3_3;
    private ImageButton ibtn_3_4;
    private ImageButton ibtn_3_5;
    private ImageButton ibtn_3_6;
    private ImageButton ibtn_3_7;

    //row 5
    private ImageButton ibtn_4_0;
    private ImageButton ibtn_4_1;
    private ImageButton ibtn_4_2;
    private ImageButton ibtn_4_3;
    private ImageButton ibtn_4_4;
    private ImageButton ibtn_4_5;
    private ImageButton ibtn_4_6;
    private ImageButton ibtn_4_7;

    //row 6
    private ImageButton ibtn_5_0;
    private ImageButton ibtn_5_1;
    private ImageButton ibtn_5_2;
    private ImageButton ibtn_5_3;
    private ImageButton ibtn_5_4;
    private ImageButton ibtn_5_5;
    private ImageButton ibtn_5_6;
    private ImageButton ibtn_5_7;

    //row 7
    private ImageButton ibtn_6_0;
    private ImageButton ibtn_6_1;
    private ImageButton ibtn_6_2;
    private ImageButton ibtn_6_3;
    private ImageButton ibtn_6_4;
    private ImageButton ibtn_6_5;
    private ImageButton ibtn_6_6;
    private ImageButton ibtn_6_7;

    //row 8
    private ImageButton ibtn_7_0;
    private ImageButton ibtn_7_1;
    private ImageButton ibtn_7_2;
    private ImageButton ibtn_7_3;
    private ImageButton ibtn_7_4;
    private ImageButton ibtn_7_5;
    private ImageButton ibtn_7_6;
    private ImageButton ibtn_7_7;

    private Button btnSurrender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pink_damath);

        clickSound = MediaPlayer.create(PinkDamathRadicalActivity.this, R.raw.click);

        tvRoom = findViewById(R.id.tvRoomIdID);
        tvPlayer1Name = findViewById(R.id.tvPlayer1ID);
        tvPlayer2Name = findViewById(R.id.tvPlayer2ID);

//        player1Score = findViewById(R.id.tvPlayer1Score);
//        player2Score = findViewById(R.id.tvPlayer2Score);
//        player1Score.setText(player1Score.getText());

        TextView Display = findViewById(R.id.tvPlayer1Score);
        Bundle bn = getIntent().getExtras();
        String name = bn.getString("abc");
        Display.setText(String.valueOf(name));

        mTextTimer = findViewById(R.id._play_timer);

        tvPlayer2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                if(player1Exited == false){
                    dialogBuilder = new AlertDialog.Builder(PinkDamathRadicalActivity.this);
                    View statusView = getLayoutInflater().inflate(R.layout.status,null);
                    TextView tvWin = statusView.findViewById(R.id.tvWinID);
                    TextView tvLoss =  statusView.findViewById(R.id.tvLossID);
                    TextView tvWinningRate = statusView.findViewById(R.id.tvWinningRateID);

                    tvWin.setText("Win: " + player1.getWin());
                    tvLoss.setText("Loss: " + player1.getLoss());
                    tvWinningRate.setText("Winning Rate: " + player1.getWinningRate());

                    dialogBuilder.setView(statusView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });


//row 1
        ibtn_0_0 = findViewById(R.id.ibtn_0_0);
        ibtn_0_1 = findViewById(R.id.ibtn_0_1);
        ibtn_0_2 = findViewById(R.id.ibtn_0_2);
        ibtn_0_3 = findViewById(R.id.ibtn_0_3);
        ibtn_0_4 = findViewById(R.id.ibtn_0_4);
        ibtn_0_5 = findViewById(R.id.ibtn_0_5);
        ibtn_0_6 = findViewById(R.id.ibtn_0_6);
        ibtn_0_7 = findViewById(R.id.ibtn_0_7);
//row 2
        ibtn_1_0 = findViewById(R.id.ibtn_1_0);
        ibtn_1_1 = findViewById(R.id.ibtn_1_1);
        ibtn_1_2 = findViewById(R.id.ibtn_1_2);
        ibtn_1_3 = findViewById(R.id.ibtn_1_3);
        ibtn_1_4 = findViewById(R.id.ibtn_1_4);
        ibtn_1_5 = findViewById(R.id.ibtn_1_5);
        ibtn_1_6 = findViewById(R.id.ibtn_1_6);
        ibtn_1_7 = findViewById(R.id.ibtn_1_7);
//row 3
        ibtn_2_0 = findViewById(R.id.ibtn_2_0);
        ibtn_2_1 = findViewById(R.id.ibtn_2_1);
        ibtn_2_2 = findViewById(R.id.ibtn_2_2);
        ibtn_2_3 = findViewById(R.id.ibtn_2_3);
        ibtn_2_4 = findViewById(R.id.ibtn_2_4);
        ibtn_2_5 = findViewById(R.id.ibtn_2_5);
        ibtn_2_6 = findViewById(R.id.ibtn_2_6);
        ibtn_2_7 = findViewById(R.id.ibtn_2_7);
//row 4
        ibtn_3_0 = findViewById(R.id.ibtn_3_0);
        ibtn_3_1 = findViewById(R.id.ibtn_3_1);
        ibtn_3_2 = findViewById(R.id.ibtn_3_2);
        ibtn_3_3 = findViewById(R.id.ibtn_3_3);
        ibtn_3_4 = findViewById(R.id.ibtn_3_4);
        ibtn_3_5 = findViewById(R.id.ibtn_3_5);
        ibtn_3_6 = findViewById(R.id.ibtn_3_6);
        ibtn_3_7 = findViewById(R.id.ibtn_3_7);

//row 5
        ibtn_4_0 = findViewById(R.id.ibtn_4_0);
        ibtn_4_1 = findViewById(R.id.ibtn_4_1);
        ibtn_4_2 = findViewById(R.id.ibtn_4_2);
        ibtn_4_3 = findViewById(R.id.ibtn_4_3);
        ibtn_4_4 = findViewById(R.id.ibtn_4_4);
        ibtn_4_5 = findViewById(R.id.ibtn_4_5);
        ibtn_4_6 = findViewById(R.id.ibtn_4_6);
        ibtn_4_7 = findViewById(R.id.ibtn_4_7);

//row 6
        ibtn_5_0 = findViewById(R.id.ibtn_5_0);
        ibtn_5_1 = findViewById(R.id.ibtn_5_1);
        ibtn_5_2 = findViewById(R.id.ibtn_5_2);
        ibtn_5_3 = findViewById(R.id.ibtn_5_3);
        ibtn_5_4 = findViewById(R.id.ibtn_5_4);
        ibtn_5_5 = findViewById(R.id.ibtn_5_5);
        ibtn_5_6 = findViewById(R.id.ibtn_5_6);
        ibtn_5_7 = findViewById(R.id.ibtn_5_7);

//row 7
        ibtn_6_0 = findViewById(R.id.ibtn_6_0);
        ibtn_6_1 = findViewById(R.id.ibtn_6_1);
        ibtn_6_2 = findViewById(R.id.ibtn_6_2);
        ibtn_6_3 = findViewById(R.id.ibtn_6_3);
        ibtn_6_4 = findViewById(R.id.ibtn_6_4);
        ibtn_6_5 = findViewById(R.id.ibtn_6_5);
        ibtn_6_6 = findViewById(R.id.ibtn_6_6);
        ibtn_6_7 = findViewById(R.id.ibtn_6_7);

//row 7
        ibtn_7_0 = findViewById(R.id.ibtn_7_0);
        ibtn_7_1 = findViewById(R.id.ibtn_7_1);
        ibtn_7_2 = findViewById(R.id.ibtn_7_2);
        ibtn_7_3 = findViewById(R.id.ibtn_7_3);
        ibtn_7_4 = findViewById(R.id.ibtn_7_4);
        ibtn_7_5 = findViewById(R.id.ibtn_7_5);
        ibtn_7_6 = findViewById(R.id.ibtn_7_6);
        ibtn_7_7 = findViewById(R.id.ibtn_7_7);

        imageButtonList = new ImageButton[][]
                {{ibtn_0_0, null, ibtn_0_2, null, ibtn_0_4, null, ibtn_0_6, null},
                        {null, ibtn_1_1, null, ibtn_1_3, null, ibtn_1_5, null, ibtn_1_7},
                        {ibtn_2_0, null, ibtn_2_2, null, ibtn_2_4, null, ibtn_2_6, null},
                        {null, ibtn_3_1, null, ibtn_3_3, null, ibtn_3_5, null, ibtn_3_7},
                        {ibtn_4_0, null, ibtn_4_2, null, ibtn_4_4, null, ibtn_4_6, null},
                        {null, ibtn_5_1, null, ibtn_5_3, null, ibtn_5_5, null, ibtn_5_7},
                        {ibtn_6_0, null, ibtn_6_2, null, ibtn_6_4, null, ibtn_6_6, null},
                        {null, ibtn_7_1, null, ibtn_7_3, null, ibtn_7_5, null, ibtn_7_7}};

        room = (Room)getIntent().getSerializableExtra("room");
        tvRoom.setText(getResources().getText(R.string.room_id) + String.valueOf(room.getId()));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refRoom = database.getReference("Room");
        refThisRoom = refRoom.child("radical").child("unavailable").child(String.valueOf(room.getId()));

        refRoom.child("radical").child("unavailable").child(String.valueOf(room.getId())).setValue(room);
        refRoom.child("radical").child("available").child(String.valueOf(room.getId())).removeValue();

        btnSurrender = findViewById(R.id.btnSurrenderID);


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
                    if(dataSnapshot1.getValue(Player.class).getUsername().equals(user.getDisplayName())){
                        player = dataSnapshot1.getValue(Player.class);
                        tvPlayer1Name.setText(getResources().getText(R.string.player1) + player.getUsername());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refThisRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(backPressed == false) {
                    if(paused == false) {
                        if (player1Exited == false) {
                            if(dataSnapshot.getValue(Room.class) != null) {
                                if (dataSnapshot.getValue(Room.class).getPlayer1() != null) {
                                    btnSurrender.setClickable(true);
                                    tvPlayer2Name.setClickable(true);
                                    tvPlayer2Name.setText(getResources().getText(R.string.player2) + dataSnapshot.getValue(Room.class).getPlayer1().getUsername());
                                    player1 = dataSnapshot.getValue(Room.class).getPlayer1();
                                    turn = dataSnapshot.getValue(Room.class).getTurn();
                                    round++;
                                    if(turn == false && (round%2 != 0)){
                                        Toast.makeText(PinkDamathRadicalActivity.this, "Your Turn", Toast.LENGTH_SHORT).show();
                                        pTimer = (TextView) findViewById(R.id._player_timer);
                                        TextView pID = (TextView) findViewById(R.id._player_ID);
                                        TextView getPText= (TextView) findViewById(R.id.getPlayerText);
                                        new CountDownTimer(60000,1000) {
                                            int counter;
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                pID.setText("Player 2:");
                                                getPText.setText("Player 1");
                                                pTimer.setText(""+ millisUntilFinished / 1000);
                                                counter++;
                                                if (turn == true){
                                                    this.cancel();
                                                    pID.setText("Player 1:");
                                                    getPText.setText("Player 2");
                                                    pTimer.setText("Turn");
                                                }
                                            }
                                            @Override
                                            public void onFinish() {
                                                if(counter == 0){
                                                    turn = false;
                                                }
                                            }
                                        }.start();
                                    }
                                    damathList = dataSnapshot.getValue(Room.class).getDamathList();
                                    processDamathList();
                                    if(player1Exited == false){
                                        startTimer();
                                    }
                                    updateAllButtons();
                                    disableButtons();
                                    if(checkWin() == true){
                                        player1Exited =true;
                                        Toast.makeText(PinkDamathRadicalActivity.this, "You win!", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PinkDamathRadicalActivity.this);
                                        builder.setTitle("Room Update");
                                        builder.setMessage("Excellent! You win");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Nice", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                clickSound.start();
                                                player.updateWin();
                                                refSignUpPlayers.child(player.getUsername()).setValue(player);
                                                PinkDamathRadicalActivity.super.onBackPressed();
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                    if(checkLose() == true){
                                        player1Exited =true;
                                        Toast.makeText(PinkDamathRadicalActivity.this, "You Lost!", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PinkDamathRadicalActivity.this);
                                        builder.setTitle("Room Update");
                                        builder.setMessage("Sorry! You Lost");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                clickSound.start();
                                                refThisRoom.removeValue();
                                                player.updateLoss();
                                                refSignUpPlayers.child(player.getUsername()).setValue(player);
                                                PinkDamathRadicalActivity.super.onBackPressed();
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                } else {
                                    btnSurrender.setClickable(false);
                                    tvPlayer2Name.setClickable(false);
                                    player1Exited = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PinkDamathRadicalActivity.this);
                                    builder.setTitle("Room Update");
                                    builder.setMessage("Player Surrendered! You win");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            clickSound.start();
                                            player.updateWin();
                                            refSignUpPlayers.child(player.getUsername()).setValue(player);
                                            refThisRoom.removeValue();
                                            PinkDamathRadicalActivity.super.onBackPressed();
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSurrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSound.start();
                AlertDialog.Builder builder = new AlertDialog.Builder(PinkDamathRadicalActivity.this);
                builder.setTitle("Surrender");
                builder.setMessage("Are you sure you want to surrender?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickSound.start();
                        backPressed = true;
                        refThisRoom.child("player2").removeValue();
                        player.updateLoss();
                        refSignUpPlayers.child(player.getUsername()).setValue(player);
                        PinkDamathRadicalActivity.super.onBackPressed();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickSound.start();
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

    }

    public void startTimer() {
        mTimer = new CountDownTimer(timeLeftInMilliseconds,1000){
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        mTextTimer.setText(timeLeftText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pausedCalled", "called");
        if(backPressed == false) {
            if (player1Exited == false) {
                paused = true;
                refThisRoom.child("player2").removeValue();
                player.updateLoss();
                refSignUpPlayers.child(player.getUsername()).setValue(player);
                finish();
            }
        }
    }

    public void myOnClick(View view) {
        clickSound.start();
        if(turn == false){
            if (secondClick == false) {//(First Click)
                for (int r = 0; r < imageButtonList.length; r++) {
                    for (int c = 0; c < imageButtonList[r].length; c++) {
                        if (imageButtonList[r][c] != null) {//if imageButtonList[r][c] is not null
                            if (view.getId() == imageButtonList[r][c].getId()) {//get the r and c of the ibtn clicked
                                if (!(damathList.get(r).get(c) instanceof NullChecker)) {//if the corresponding location in the damathlist has a checker
                                    Log.d("Type", damathList.get(r).get(c).getClass().toString());
                                    if (damathList.get(r).get(c) instanceof PinkDamath) {//if clicked checker is a blue dama
                                        row = r;
                                        column = c;
                                        if (destroyed == false) {
                                            possibleMove = damathList.get(r).get(c).getMove(damathList);//get the possibleMove from blue dama class
                                            killList = damathList.get(r).get(c).getKillList();//get the corresponding killList
                                            Log.d("PossibleMove", String.valueOf(possibleMove.size()));
                                            if (disableAllButOneButton == false) {
                                                updateAllButtons();
                                                disableButtons();
                                            }
                                            for (int i = 0; i < possibleMove.size(); i++) {//go through the possibleMove
                                                int row = possibleMove.get(i)[0];//get each row
                                                int column = possibleMove.get(i)[1];//get each column
                                                imageButtonList[row][column].setClickable(true);//make the possible places clickable
                                                imageButtonList[row][column].setBackgroundColor(Color.WHITE);
                                            }
                                            secondClick = true;
                                        }
                                        else {
                                            destroyed = false;
                                            possibleMove = damathList.get(r).get(c).getMove2(damathList);//get the possibleMove from blue dama class
                                            killList = damathList.get(r).get(c).getKillList();
                                            if (possibleMove.size() != 0) {
                                                for (int i = 0; i < possibleMove.size(); i++) {//go through the possibleMove
                                                    int row = possibleMove.get(i)[0];//get each row
                                                    int column = possibleMove.get(i)[1];//get each column
                                                    disableAllButPossible(possibleMove);
                                                }
                                                secondClick = true;
                                            }
                                            else {
                                                secondClick = false;
                                                turn = true;
                                                updateAllButtons();
                                                disableAllButOneButton(r, c);
                                                disableAllButOneButton = true;
                                                refThisRoom.child("damathList").setValue(damathList);
                                                refThisRoom.child("turn").setValue(turn);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else {//PinkDamath's turn (Second Click)
                for (int r = 0; r < imageButtonList.length; r++) {
                    for (int c = 0; c < imageButtonList[r].length; c++) {
                        if (imageButtonList[r][c] != null) {//if imageButtonList[r][c] is not null
                            if (!(damathList.get(r).get(c) instanceof BlueDamath)) {
                                if (view.getId() == imageButtonList[r][c].getId()) {//get the r and c of the ibtn clicked
                                    if(damathList.get(r).get(c) instanceof NullChecker) {
                                        damathList.get(r).set(c, new PinkDamath(damathList.get(row).get(column)));//row and column are the position of the new position, copy the checker to the new position
                                        damathList.get(r).get(c).setRow(r);
                                        damathList.get(r).get(c).setColumn(c);
                                        damathList.get(r).get(c).setType("PinkDamath");
                                        if(damathList.get(r).get(c).getRow() == 7){
                                            damathList.get(r).get(c).setKingStatus(true);
                                        }
                                        damathList.get(row).set(column, nullc);//delete the check in the old location
                                        killLocation = getKillDamathLocation(r,c);
                                        if(killLocation != null) {
                                            damathList.get(killLocation[0]).set(killLocation[1], nullc);
                                            destroyed = true;
                                            secondClick = false;
                                            onKillCalculator();
                                            onMoveIndicator();
                                            updateAllButtons();
                                            possibleMove = damathList.get(r).get(c).getMove2(damathList);
                                            if(possibleMove.size() == 0){
                                                disableAllButOneButton = false;
                                                destroyed = false;
                                                secondClick = false;
                                                turn = true;
                                                disableButtons();
                                                refThisRoom.child("damathList").setValue(damathList);
                                                refThisRoom.child("turn").setValue(turn);
                                            }
                                            else{
                                                disableAllButOneButton(r, c);
                                                disableAllButOneButton = true;
                                            }
                                        }
                                        else{
                                            disableAllButOneButton = false;
                                            destroyed = false;
                                            secondClick = false;
                                            turn = true;
                                            updateAllButtons();
                                            disableButtons();
                                            refThisRoom.child("damathList").setValue(damathList);
                                            refThisRoom.child("turn").setValue(turn);
                                        }
                                        break;
                                    }
                                    else {
                                        updateAllButtons();//update the layout and made all the buttons clickable
                                        disableButtons();
                                        secondClick = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
    }

    private void onMoveIndicator() {

    }

    private void onKillCalculator() {

        final Dialog calculatorDialog = new Dialog(PinkDamathRadicalActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.calculator,null);

        workingsTV = dialogView.findViewById(R.id.tvWorkingsID);
        resultsTV = dialogView.findViewById(R.id.tvResultID);
        signBox = dialogView.findViewById(R.id.signID);

        btnPlus = dialogView.findViewById(R.id.btnPlusID);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = "+";
                value1 = workingsTV.getText().toString();
                workingsTV.setText(null);
                signBox.setText("+");
                hasDot = false;
            }
        });

        btnMinus = dialogView.findViewById(R.id.btnMinusID);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = "-";
                value1 = workingsTV.getText().toString();
                workingsTV.setText(null);
                signBox.setText("-");
                hasDot = false;
            }
        });

        btnMultiply = dialogView.findViewById(R.id.btnMultiplyID);
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = "*";
                value1 = workingsTV.getText().toString();
                workingsTV.setText(null);
                signBox.setText("×");
                hasDot = false;
            }
        });

        btnDivide = dialogView.findViewById(R.id.btnDivideID);
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = "/";
                value1 = workingsTV.getText().toString();
                workingsTV.setText(null);
                signBox.setText("÷");
                hasDot = false;
            }
        });

        btnRadical = dialogView.findViewById(R.id.btnSquareID);
        btnRadical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = workingsTV.getText().toString();
                double r = Math.sqrt(Double.parseDouble(val));
                workingsTV.setText(String.valueOf(r));
            }
        });

        btnOpenP = dialogView.findViewById(R.id.btnOpenPID);
        btnOpenP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"(");
            }
        });

        btnCloseP = dialogView.findViewById(R.id.btnClosePID);
        btnCloseP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+")");
            }
        });

        btnAC = dialogView.findViewById(R.id.btnAcID);
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(null);
                signBox.setText(null);
                value1 = null;
                value2 = null;
                sign = null;
                hasDot = false;
            }
        });

        btnPow = dialogView.findViewById(R.id.btnPowerID);
        btnPow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = "power";
                value1 = workingsTV.getText().toString();
                workingsTV.setText(null);
                hasDot = false;
                signBox.setText("xⁿ");
            }
        });

        btnPeriod = dialogView.findViewById(R.id.btnPointID);
        btnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasDot) {
                    if (workingsTV.getText().equals("")) {

                        workingsTV.setText("0.");
                    } else {

                        workingsTV.setText(workingsTV.getText() + ".");
                    }

                    hasDot = true;
                }
            }
        });
        btnOk = dialogView.findViewById(R.id.btnOkID);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workingsTV.getText() != null){
                    value1 = player2Score.getText().toString();
                    value2 = resultsTV.getText().toString();
                    num1 = Double.parseDouble(value1);
                    num2 = Double.parseDouble(value2);
                    result = num1 + num2;
                    player2Score.setText(result + "");
                }
                calculatorDialog.dismiss();
            }
        });
        player1Score.getText();
        btnEquals = dialogView.findViewById(R.id.btnEqualsID);
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign == null) {
                    signBox.setText("Error!");
                } else if (workingsTV.getText().equals("")) {
                    signBox.setText("Error!");
                } else if ((sign.equals("+") || sign.equals("-") || sign.equals("*") || sign.equals("/")) && value1.equals("")) {
                    signBox.setText("Error!");
                } else {
                    switch (sign) {
                        default:
                            break;
                        case "log":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            resultsTV.setText(Math.log10(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "ln":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            resultsTV.setText(Math.log(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "power":
                            num1 = Double.parseDouble((value1));
                            value2 = workingsTV.getText().toString();
                            num2 = Double.parseDouble(value2);
                            workingsTV.setText(Math.pow(num1, num2) + "");
                            resultsTV.setText(Math.pow(num1, num2) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "root":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble((value1));
                            resultsTV.setText(Math.sqrt(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "factorial":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble((value1));
                            int i = Integer.parseInt(value1) - 1;

                            while (i > 0) {
                                num1 = num1 * i;
                                i--;
                            }

                            resultsTV.setText(num1 + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "sin":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble((value1));
                            resultsTV.setText(Math.sin(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "cos":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble((value1));
                            resultsTV.setText(Math.cos(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "tan":
                            value1 = workingsTV.getText().toString();
                            num1 = Double.parseDouble((value1));
                            workingsTV.setText(Math.tan(num1) + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "+":
                            value2 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            num2 = Double.parseDouble(value2);
                            result = num1 + num2;
                            workingsTV.setText(result + "");
                            resultsTV.setText(result + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "-":
                            value2 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            num2 = Double.parseDouble(value2);
                            result = num1 - num2;
                            workingsTV.setText(result + "");
                            resultsTV.setText(result + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "*":
                            value2 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            num2 = Double.parseDouble(value2);
                            result = num1 * num2;
                            workingsTV.setText(result + "");
                            resultsTV.setText(result + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                        case "/":
                            value2 = workingsTV.getText().toString();
                            num1 = Double.parseDouble(value1);
                            num2 = Double.parseDouble(value2);
                            result = num1 / num2;
                            workingsTV.setText(result + "");
                            resultsTV.setText(result + "");
                            sign = null;
                            signBox.setText(null);
                            break;
                    }
                }
            }
        });
        btnDelete = dialogView.findViewById(R.id.btnDeleteID);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workingsTV.getText().equals("")) {
                    workingsTV.setText(null);
                } else {
                    int len = workingsTV.getText().length();
                    String s = workingsTV.getText().toString();
                    if (s.charAt(len - 1) == '.') {
                        hasDot = false;
                        workingsTV.setText(workingsTV.getText().subSequence(0, workingsTV.getText().length() - 1));

                    } else {
                        workingsTV.setText(workingsTV.getText().subSequence(0, workingsTV.getText().length() - 1));
                    }
                }
            }
        });

        btn0 = dialogView.findViewById(R.id.btn0ID);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText() + "0");
            }
        });
        btn1 = dialogView.findViewById(R.id.btn1ID);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText() + "1");
            }
        });
        btn2 = dialogView.findViewById(R.id.btn2ID);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText() + "2");
            }
        });
        btn3 = dialogView.findViewById(R.id.btn3ID);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText() + "3");
            }
        });
        btn4 = dialogView.findViewById(R.id.btn4ID);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText() + "4");
            }
        });
        btn5 = dialogView.findViewById(R.id.btn5ID);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"5");
            }
        });
        btn6 = dialogView.findViewById(R.id.btn6ID);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"6");
            }
        });
        btn7 = dialogView.findViewById(R.id.btn7ID);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"7");
            }
        });
        btn8 = dialogView.findViewById(R.id.btn8ID);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"8");
            }
        });
        btn9 = dialogView.findViewById(R.id.btn9ID);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workingsTV.setText(workingsTV.getText()+"9");
            }
        });

        calculatorDialog.setContentView(dialogView);
        calculatorDialog.setCancelable(false);
        calculatorDialog.show();

    }

    public void disableButtons(){//disable unmovable dama
        Log.d("DisableCalled","Turn " + String.valueOf(turn));
        if(turn) {//if it is blue dama turn
            for (int r = 0; r < damathList.size(); r++) {
                for (int c = 0; c < damathList.get(r).size(); c++) {
                    if (damathList.get(r).get(c) instanceof PinkDamath) {//if the dama is pink (disable all the redCheckers)
                        imageButtonList[r][c].setClickable(false);
                        //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                    }
                    else if(damathList.get(r).get(c) instanceof BlueDamath){//disable the unmovable blue dama
                        if(r == 0){//if the blue dama is at row 0 (crown)
                            imageButtonList[r][c].setClickable(true);
                        }
                        else {
                            if (c == 0){
                                if(damathList.get(r - 1).get(c + 1) instanceof BlueDamath) { //if the blue dama is at column 0 and there is one blue dama at upper right
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            }
                            else if (c == 7){
                                if(damathList.get(r - 1).get(c - 1) instanceof BlueDamath) {//if the blue dama is at column 7 and there is one blue dama at upper left
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            } else {//if the blue dama is located at column 1 - 6
                                if (damathList.get(r - 1).get(c - 1) instanceof BlueDamath && damathList.get(r - 1).get(c + 1) instanceof BlueDamath) {//if there are blue damas on both upper left and right
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            }
                        }
                    }
                    else{//if the damathlist updates ex: one black checker died, need to update the corresponding image button to not clickable
                        if(imageButtonList[r][c] != null) {
                            imageButtonList[r][c].setClickable(false);
                            //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                        }
                    }
                }
            }
        }
        else{//if it is pink damas turn
            for (int r = 0; r < damathList.size(); r++) {
                for (int c = 0; c < damathList.get(r).size(); c++) {
                    if (damathList.get(r).get(c) instanceof BlueDamath) {//if the dama is blue (disable all the blue damas)
                        imageButtonList[r][c].setClickable(false);
                        //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                    }
                    else if(damathList.get(r).get(c) instanceof PinkDamath) {//disable the unmovable pink damas
                        if (r == 7) {//if the pink dama is at row 7 (crown)
                            imageButtonList[r][c].setClickable(true);
                        } else {
                            if (c == 0) {
                                if (damathList.get(r + 1).get(c + 1) instanceof PinkDamath) { //if the pink dama is at column 0 and there is one redChecker at upper right
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            } else if (c == 7) {
                                if (damathList.get(r + 1).get(c - 1) instanceof PinkDamath) {//if the blue dama is at column 7 and there is one blue dama at upper left
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            } else {//if the blue dama is located at column 1 - 6
                                if (damathList.get(r + 1).get(c - 1) instanceof PinkDamath && damathList.get(r + 1).get(c + 1) instanceof PinkDamath) {//if there are redCheckers on both upper left and right
                                    imageButtonList[r][c].setClickable(false);
                                    //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                                }
                            }
                        }

                    }
                    else{//if the damathlist updates ex: one pink dama died, need to update the corresponding image button to not clickable
                        if(imageButtonList[r][c] != null) {
                            imageButtonList[r][c].setClickable(false);
                            //imageButtonList[r][c].setBackgroundColor(Color.BLACK);
                        }
                    }
                }
            }
        }

    }

    public void disableAllButOneButton(int row, int column){
        updateAllButtons();
        Log.d("DisableAllButOneButton","called");
        for (int r = 0; r < imageButtonList.length; r++) {
            for (int c = 0; c < imageButtonList[r].length; c++) {
                if(imageButtonList[r][c] != null) {
                    imageButtonList[r][c].setClickable(false);
                }
            }
        }
        imageButtonList[row][column].setClickable(true);
    }

    public void disableAllButPossible(ArrayList <int[]> possiblemove){
        updateAllButtons();
        for (int r = 0; r < imageButtonList.length; r++) {
            for (int c = 0; c < imageButtonList[r].length; c++) {
                if(imageButtonList[r][c] != null) {
                    imageButtonList[r][c].setClickable(false);
                }
            }
        }
        for (int i = 0; i < possibleMove.size(); i++) {//go through the possibleMove
            int row = possibleMove.get(i)[0];//get each row
            int column = possibleMove.get(i)[1];//get each column
            imageButtonList[row][column].setClickable(true);//make the possible places clickable
            imageButtonList[row][column].setBackgroundColor(Color.WHITE);
        }


    }

    public void updateAllButtons(){//update the whole layout based on the contents in damathList, also make all the imageButtons clickable
        Log.d("UpdateCalled","Turn " + String.valueOf(turn));
        for(int r = 0; r < imageButtonList.length; r++) {
            for (int c = 0; c < imageButtonList[r].length; c++) {
                if (imageButtonList[r][c] != null) {//if the imageButton is not null
                    imageButtonList[r][c].setClickable(true);//make all the imageButtons clickable
                    imageButtonList[r][c].setImageDrawable(null);//Erase all the drawables and background color

                    if (!(damathList.get(r).get(c) instanceof NullChecker)) {//in the movable location if the checker in the damathlist is not null
                        if (damathList.get(r).get(c) instanceof BlueDamath) {//if its a BlueDamath
                            if(damathList.get(r).get(c).isKingStatus() == false) {
                                imageButtonList[r][c].setImageResource(R.drawable.radical_blue_0_0);
                            } else {
                                imageButtonList[r][c].setImageResource(R.drawable.radical_blue_0_0);
                            }
                        }
                        if (damathList.get(r).get(c) instanceof PinkDamath) {//if its a RedChecker
                            if(damathList.get(r).get(c).isKingStatus() == false) {
                                imageButtonList[r][c].setImageResource(R.drawable.radical_pink_5_1);
                            } else {
                                imageButtonList[r][c].setImageResource(R.drawable.radical_pink_5_1);
                            }
                        }
                    }
                    imageButtonList[r][c].setBackgroundColor(getResources().getColor(android.R.color.transparent)); //set the desirable background color
                }
            }
        }
    }

    public int [] getKillDamathLocation(int rIndex, int cIndex){
        int [] location = new int [2];
        for (int i = 0; i < possibleMove.size(); i++){
            if(possibleMove.get(i)[0] == rIndex && possibleMove.get(i)[1] == cIndex){
                location = killList.get(i);
                break;
            }
        }
        return location;
    }

    public void processDamathList() {
        Boolean status;
        for (int r = 0; r < damathList.size(); r++) {
            for (int c = 0; c < damathList.get(r).size(); c++) {
                if (damathList.get(r).get(c).getType().equals("BlueDamath")) {//if the dama is blue
                    damathList.get(r).get(c).getType();
                    status = damathList.get(r).get(c).isKingStatus();
                    damathList.get(r).set(c, (new BlueDamath(damathList.get(r).get(c).getRow(), damathList.get(r).get(c).getColumn())));
                    damathList.get(r).get(c).setKingStatus(status);
                    damathList.get(r).get(c).setType("BlueDamath");
                }
                if (damathList.get(r).get(c).getType().equals("PinkDamath")) {//if the dama is pink
                    damathList.get(r).get(c).getType();
                    status = damathList.get(r).get(c).isKingStatus();
                    damathList.get(r).set(c, (new PinkDamath(damathList.get(r).get(c).getRow(), damathList.get(r).get(c).getColumn())));
                    damathList.get(r).get(c).setKingStatus(status);
                    damathList.get(r).get(c).setType("PinkDamath");
                }
                if (damathList.get(r).get(c).getType().equals("NullChecker")) {//if the dama is pink
                    damathList.get(r).get(c).getType();
                    damathList.get(r).set(c, (new NullChecker()));
                    damathList.get(r).get(c).setType("NullChecker");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d("Pressed","Hello");
        if(player1Exited == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Leaving while in a game");
            builder.setMessage("Leave now means surrender");
            builder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickSound.start();
                    backPressed = true;
                    refThisRoom.child("player2").removeValue();
                    player.updateLoss();
                    refSignUpPlayers.child(player.getUsername()).setValue(player);
                    PinkDamathRadicalActivity.super.onBackPressed();
                    finish();
                }
            });
            builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickSound.start();
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else{
            backPressed = true;
            refThisRoom.removeValue();
            PinkDamathRadicalActivity.super.onBackPressed();
            finish();
        }
    }

    public boolean checkWin(){
        boolean win = true;
        for(int r = 0; r < damathList.size(); r++){
            for(int c = 0; c < damathList.get(r).size(); c++){
                if(damathList.get(r).get(c).getType().equals("BlueDamath")){
                    win = false;
                    break;
                }
            }
        }
        return win;
    }

    public boolean checkLose(){
        boolean lose = true;
        for(int r = 0; r < damathList.size(); r++){
            for(int c = 0; c < damathList.get(r).size(); c++){
                if(damathList.get(r).get(c).getType().equals("PinkDamath")){
                    lose = false;
                    break;
                }
            }
        }
        return lose;
    }
}