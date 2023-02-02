package com.unityx.damath.Damath;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BlueDamath extends Damath {

    private ArrayList <int[]> possibleMove;
    private ArrayList <int[]> killList;

    private static String type = "BlueDamath";

    public BlueDamath(int row, int column){ super(row, column, type);}

    public BlueDamath(Damath blueDamath){ super(blueDamath); }


    @Override
    public ArrayList<int[]> getMove(List<List<Damath>>damathList){
        possibleMove = new ArrayList<>();
        killList = new ArrayList<>();

        recursiveSearch(getRow(), getColumn(), damathList);
        return possibleMove;
    }

    public void recursiveSearch(int r, int c, List<List<Damath>> damathList){
        if(isKingStatus() == false) {
            recursiveSearchNotKing(r, c, damathList);
        }
        else{//else for crown status
            recursiveSearchNotKing(r, c, damathList);
            recursiveSearchKing(r, c, damathList);
        }
    }

    public void recursiveSearchNotKing(int r, int c, List<List<Damath>> damathList){
        if (c == 0 && r > 0) {
            if (!(damathList.get(r - 1).get(c + 1) instanceof BlueDamath)) {
                if (damathList.get(r - 1).get(c + 1) instanceof PinkDamath) {
                    if (r - 2 >= 0) {
                        if (damathList.get(r - 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);
                        }
                    }
                } else {
                    possibleMove.add(new int[]{r - 1, c + 1});
                    killList.add(null);
                }
            }
            if (!(damathList.get(r - 1).get(c + 1) instanceof BlueDamath)) {
                if (damathList.get(r - 1).get(c + 1) instanceof PinkDamath) {
                    if (r - 2 >= 0) {
                        if (damathList.get(r - 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);
                        }
                    }
                } else {
                    possibleMove.add(new int[]{r - 1, c + 1});
                    killList.add(null);
                }
            }

        } else if (c > 0 && c < 7 && r > 0) {
            if (!(damathList.get(r - 1).get(c - 1) instanceof BlueDamath)) {
                if (damathList.get(r - 1).get(c - 1) instanceof PinkDamath) {
                    if ((r - 2 >= 0) && (c - 2 >= 0)) {
                        if (damathList.get(r - 2).get(c - 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c - 2});
                            killList.add(new int[]{r - 1, c - 1});
                            //recursiveSearch(r - 2, c - 2, checkerList);
                        }
                    }

                } else {
                    possibleMove.add(new int[]{r - 1, c - 1});
                    killList.add(null);
                }

            }
            if (!(damathList.get(r - 1).get(c + 1) instanceof BlueDamath)) {
                if (damathList.get(r - 1).get(c + 1) instanceof PinkDamath) {
                    if ((r - 2 >= 0) && (c + 2 <= 7)) {
                        if (damathList.get(r - 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);//start a new search
                        }
                    }

                } else {
                    possibleMove.add(new int[]{r - 1, c + 1});
                    killList.add(null);
                }

            }
        } else {
            if (c == 7 && r > 0) {
                if (!(damathList.get(r - 1).get(c - 1) instanceof BlueDamath)) {
                    if (damathList.get(r - 1).get(c - 1) instanceof PinkDamath) {
                        if (r + 2 >= 0) {
                            if (damathList.get(r - 2).get(c - 2) instanceof NullChecker) {
                                possibleMove.add(new int[]{r - 2, c - 2});
                                killList.add(new int[]{r - 1, c - 1});
                                //recursiveSearch(r - 2, c - 2, checkerList);
                            }
                        }
                    } else {
                        possibleMove.add(new int[]{r - 1, c - 1});
                        killList.add(null);
                    }
                }
            }
        }
    }

    public void recursiveSearchKing(int r, int c, List<List<Damath>> damathList){
        if(c == 0 && r < 7) {
            if (!(damathList.get(r + 1).get(c + 1) instanceof BlueDamath)) { //if PinkDamath is at column 0 and there's no PinkDamath at the lower right
                if(damathList.get(r + 1).get(c + 1) instanceof PinkDamath){
                    if(r + 2 <= 7) {
                        if (damathList.get(r + 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r + 2, c + 2});
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);
                        }
                    }
                }
                else{
                    possibleMove.add(new int[]{r + 1, c + 1});
                    killList.add(null);
                }
            }

        }
        else if(c > 0 && c < 7 && r < 7){
            if(!(damathList.get(r + 1).get(c - 1) instanceof BlueDamath)){//if the lower left is not a PinkDamath
                if(damathList.get(r + 1).get(c - 1) instanceof PinkDamath){//if the lower left is a black checker
                    if ((r + 2 <= 7) && (c - 2 >= 0)) {
                        if (damathList.get(r + 2).get(c - 2) instanceof NullChecker) {//if the lower left of the lower left is not null
                            possibleMove.add(new int[]{r + 2, c - 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c - 1});
                            //recursiveSearch(r + 2, c - 2, checkerList);//start a new search
                        }
                    }

                }
                else{
                    possibleMove.add(new int[]{r + 1, c - 1});
                    killList.add(null);
                }

            }
            if(!(damathList.get(r + 1).get(c + 1) instanceof BlueDamath)){//if the lower right is not a redChecker
                if(damathList.get(r + 1).get(c + 1) instanceof PinkDamath){//if the lower right is a black checker
                    if ((r + 2 <= 7) && (c + 2 <= 7)) {
                        if (damathList.get(r + 2).get(c + 2) instanceof NullChecker) {//if the lower right of the lower right is not null
                            possibleMove.add(new int[]{r + 2, c + 2});//add the position to the possibleMove
                            //INSERT the operation here the "if" and then insert the killList after the else//
                            //if(damathList...){
                            killList.add(new int[]{r + 1, c + 1});
                            //}else{
                                //killList.add(null);
                            //}
                            //recursiveSearch(r + 2, c + 2, checkerList);//start a new search
                        }
                    }

                }
                else{
                    possibleMove.add(new int[]{r + 1, c + 1});
                    killList.add(null);
                }

            }
        }
        else{
            if(c == 7 && r < 7) {
                if (!(damathList.get(r + 1).get(c - 1) instanceof BlueDamath)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                    if (damathList.get(r + 1).get(c - 1) instanceof PinkDamath) {
                        if (r + 2 <= 7) {
                            if (damathList.get(r + 2).get(c - 2) instanceof NullChecker) {
                                possibleMove.add(new int[]{r + 2, c - 2});
                                killList.add(new int[]{r + 1, c - 1});
                                //recursiveSearch(r + 2, c - 2, checkerList);
                            }
                        }
                    } else {
                        possibleMove.add(new int[]{r + 1, c - 1});
                        killList.add(null);
                    }
                }
            }
        }
    }



    @Override
    public ArrayList<int[]> getMove2(List<List<Damath>> damathList){

        possibleMove = new ArrayList<>();
        killList = new ArrayList<>();

        recursiveSearch2(getRow(), getColumn(), damathList);
        Log.d("PossibleMoves",String.valueOf(possibleMove.size()));
        return possibleMove;
    }

    public void recursiveSearch2(int r, int c, List<List<Damath>> damathList){
        if(isKingStatus() == false) {
            recursiveSearch2NotKing(r, c, damathList);
        }
        else{//else for crown status
            recursiveSearch2NotKing(r, c, damathList);
            recursiveSearch2King(r, c, damathList);
        }
    }

    public void recursiveSearch2NotKing(int r, int c, List<List<Damath>> damathList){
        if(c == 0 && r > 0) {
            if (!(damathList.get(r - 1).get(c + 1) instanceof BlueDamath)) {
                if(damathList.get(r - 1).get(c + 1) instanceof PinkDamath){
                    if(r - 2 >= 0) {
                        if (damathList.get(r - 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);
                        }
                    }
                }
            }

        }
        else if(c > 0 && c < 7 && r > 0){
            if(!(damathList.get(r - 1).get(c - 1) instanceof BlueDamath)){
                if(damathList.get(r - 1).get(c - 1) instanceof PinkDamath){
                    if ((r - 2 >= 0) && (c - 2 >= 0)) {
                        if (damathList.get(r - 2).get(c - 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c - 2});
                            killList.add(new int[]{r - 1, c - 1});
                            //recursiveSearch(r - 2, c - 2, checkerList);
                        }
                    }

                }

            }
            if(!(damathList.get(r - 1).get(c + 1) instanceof BlueDamath)){
                if(damathList.get(r - 1).get(c + 1) instanceof PinkDamath){
                    if ((r - 2 >= 0) && (c + 2 <= 7)) {
                        if (damathList.get(r - 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                               killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);//start a new search
                        }
                    }

                }

            }
        }
        else{
            if(c == 7 && r > 0) {
                if (!(damathList.get(r - 1).get(c - 1) instanceof BlueDamath)) {
                    if (damathList.get(r - 1).get(c - 1) instanceof PinkDamath) {
                        if (r + 2 >= 0) {
                            if (damathList.get(r - 2).get(c - 2) instanceof NullChecker) {
                                possibleMove.add(new int[]{r - 2, c - 2});
                                killList.add(new int[]{r - 1, c - 1});
                                //recursiveSearch(r - 2, c - 2, checkerList);
                            }
                        }
                    }
                }
            }
        }
    }

    public void recursiveSearch2King(int r, int c, List<List<Damath>> damathList){
        if (c == 0 && r < 7) {
            if (!(damathList.get(r + 1).get(c + 1) instanceof BlueDamath)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                if (damathList.get(r + 1).get(c + 1) instanceof PinkDamath) {
                    if (r + 2 <= 7) {
                        if (damathList.get(r + 2).get(c + 2) instanceof NullChecker) {
                            possibleMove.add(new int[]{r + 2, c + 2});
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);
                        }
                    }
                }
            }

        } else if (c > 0 && c < 7 && r < 7) {
            if (!(damathList.get(r + 1).get(c - 1) instanceof BlueDamath)) {//if the lower left is not a redChecker
                if (damathList.get(r + 1).get(c - 1) instanceof PinkDamath) {//if the lower left is a black checker
                    if ((r + 2 <= 7) && (c - 2 >= 0)) {
                        if (damathList.get(r + 2).get(c - 2) instanceof NullChecker) {//if the lower left of the lower left is not null
                            possibleMove.add(new int[]{r + 2, c - 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c - 1});
                            //recursiveSearch(r + 2, c - 2, checkerList);//start a new search
                        }
                    }

                }

            }
            if (!(damathList.get(r + 1).get(c + 1) instanceof BlueDamath)) {//if the lower right is not a redChecker
                if (damathList.get(r + 1).get(c + 1) instanceof PinkDamath) {//if the lower right is a black checker
                    if ((r + 2 <= 7) && (c + 2 <= 7)) {
                        if (damathList.get(r + 2).get(c + 2) instanceof NullChecker) {//if the lower right of the lower right is not null
                            possibleMove.add(new int[]{r + 2, c + 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);//start a new search
                        }
                    }

                }

            }
        } else {
            if (c == 7 && r < 7) {
                if (!(damathList.get(r + 1).get(c - 1) instanceof BlueDamath)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                    if (damathList.get(r + 1).get(c - 1) instanceof PinkDamath) {
                        if (r + 2 <= 7) {
                            if (damathList.get(r + 2).get(c - 2) instanceof NullChecker) {
                                possibleMove.add(new int[]{r + 2, c - 2});
                                killList.add(new int[]{r + 1, c - 1});
                                //recursiveSearch(r + 2, c - 2, checkerList);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList <int[]> getKillList(){
        return killList;
    }

}