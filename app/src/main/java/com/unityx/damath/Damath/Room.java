package com.unityx.damath.Damath;

import com.unityx.damath.Damath.BlueDamath;
import com.unityx.damath.Damath.Damath;
import com.unityx.damath.Damath.NullChecker;
import com.unityx.damath.Damath.PinkDamath;
import com.unityx.damath.User.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class  Room implements Serializable {

    private String gameMode;
    private int id;
    private long score;
    private Player player1;
    private Player player2;
    private Boolean turn = true;
    private Boolean availability = true;
    private List<List<Damath>> damathList;
    private List<List<Damath>> operationList;

    public Room(){
    }

    public Room(int id, Player player1){
        this.id = id;
        this.player1 = player1;
        this.damathList = new ArrayList<List<Damath>>();
        Damath nullc = new NullChecker();
        Damath [][] damathArray = new Damath[][]
                {{new PinkDamath(0,0), nullc, new PinkDamath(0,2), nullc, new PinkDamath(0,4), nullc, new PinkDamath(0,6), nullc},
                        {nullc, new PinkDamath(1,1), nullc, new PinkDamath(1,3), nullc, new PinkDamath(1,5), nullc, new PinkDamath(1,7)},
                        {new PinkDamath(2,0), nullc, new PinkDamath(2,2), nullc, new PinkDamath(2,4), nullc, new PinkDamath(2,6), nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, new BlueDamath(5,1), nullc, new BlueDamath(5,3), nullc, new BlueDamath(5,5), nullc, new BlueDamath(5,7)},
                        {new BlueDamath(6,0), nullc, new BlueDamath(6,2), nullc, new BlueDamath(6,4), nullc, new BlueDamath(6,6), nullc},
                        {nullc, new BlueDamath(7,1), nullc, new BlueDamath(7,3), nullc, new BlueDamath(7,5), nullc, new BlueDamath(7,7)}
                };

        for(int r = 0; r < damathArray.length; r++) {
            List<Damath> row = new ArrayList<>();
            for (int c = 0; c < damathArray[r].length; c++) {
                row.add(damathArray[r][c]);
            }
            this.damathList.add(row);
        }

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }


    public Boolean getTurn() {
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public List<List<Damath>> getDamathList() {
        return damathList;
    }

    public void setDamathList(List<List<Damath>> damathList) {
        this.damathList = damathList;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

}
