package com.unityx.damath.Leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unityx.damath.R;
import com.unityx.damath.User.Player;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewAdapter> {

    List<Player> list;
    Context context;
    int k;

    public ScoreAdapter(List<Player>list, Context context) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ScoreViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_list,parent, false);
        return new ScoreViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewAdapter holder, int position) {
        k = list.size() - 10;
        Player currentItem = list.get(position + k);
        holder.username.setText(currentItem.getUsername());
        holder.win.setText(String.valueOf(currentItem.getWin()));
        holder.rank.setText(String.valueOf(list.size()-(position + k)));
        holder.winRate.setText(String.valueOf(currentItem.getWinningRate()));
        k++;
        if(currentItem.getWinningRate() == null){
            holder.winRate.setText("0.0%");
        }
    }

    @Override
    public int getItemCount() {

        return Math.min(list.size(), 10);

    }

    public class ScoreViewAdapter extends RecyclerView.ViewHolder {

        TextView username, win, winRate, rank;

        public ScoreViewAdapter(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvLeadUsername);
            win = itemView.findViewById(R.id.tvLeadWins);
            winRate = itemView.findViewById(R.id.tvLeadWinRate);
            rank = itemView.findViewById(R.id.tvLeadRank);
        }
    }
}
