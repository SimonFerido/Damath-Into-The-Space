package com.unityx.damath.Damath.GameMode.Polynomial;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unityx.damath.Damath.GameMode.Radical.PinkDamathRadicalActivity;
import com.unityx.damath.Damath.Room;
import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import java.util.ArrayList;

public class RecyclerViewAdapterPolynomial extends RecyclerView.Adapter<RecyclerViewAdapterPolynomial.ViewHolder>{

    private Context context;
    private ArrayList<Room>roomList;
    private Room room;
    private Player player;

    MediaPlayer clickSound;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference refSignUpPlayers;
    private DatabaseReference refRoom;
    private FirebaseAuth mAuth;

    public RecyclerViewAdapterPolynomial(Context context, ArrayList<Room>roomList){
        this.roomList = roomList;
        this.context = context;
        clickSound = MediaPlayer.create(context, R.raw.click);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refSignUpPlayers = database.getReference("Signed Up Players");
        refRoom = database.getReference("Room").child("polynomial").child("available");

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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPolynomial.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterPolynomial.ViewHolder viewHolder, int position) {
        final Room room = roomList.get(position);
        if(room.getPlayer1() != null) {
            viewHolder.tvUsername.setText(room.getPlayer1().getUsername());
            viewHolder.tvRoom.setText(String.valueOf(room.getId()));
        }
    }

    @Override
    public int getItemCount() { return roomList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvUsername;
        public TextView tvRoom;
        public CardView cardView;

        public ViewHolder(View view, Context ctx) {
            super(view);
            view.setOnClickListener(this);

            context = ctx;
            tvUsername = view.findViewById(R.id.tvUsernameID);
            tvRoom = view.findViewById(R.id.tvRoomID);
            cardView = view.findViewById(R.id.cardviewLayoutID);

        }
        @Override
        public void onClick(View view) {
            clickSound.start();
            Log.d("clicked","clicked");
            int position = getAdapterPosition();
            room = roomList.get(position);
            room.setPlayer2(player);
            room.setAvailability(false);
            refRoom.child(String.valueOf(room.getId())).setValue(room);
            Intent intent = new Intent(context, PinkDamathPolynomialActivity.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        }
    }
}
