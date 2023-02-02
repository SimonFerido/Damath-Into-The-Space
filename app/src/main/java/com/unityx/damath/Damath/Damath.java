package com.unityx.damath.Damath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Damath implements Serializable {

    private int row;
    private int column;
    private String type;
    private boolean kingStatus;

    public Damath(){
    }

    public Damath(String type) {
        this.type = type;

    }

    public Damath(int row, int column, String type){
        this.row = row;
        this.column = column;
        this.type = type;
        this.kingStatus = false;
    }

    public Damath(Damath damath){
        this.row = damath.getRow();
        this.column = damath.getColumn();
        this.kingStatus = damath.isKingStatus();
    }

    public int getRow(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return column;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public boolean isKingStatus(){
        return kingStatus;
    }

    public void setKingStatus(boolean kingStatus){
        this.kingStatus = kingStatus;
    }

    public ArrayList<int[]> getMove(List<List<Damath>> damathList) {return null;}

    public ArrayList<int[]> getMove2(List<List<Damath>> damathList) {return null;}

    public ArrayList<int[]> getKillList() {return null;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

}
