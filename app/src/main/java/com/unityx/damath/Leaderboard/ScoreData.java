package com.unityx.damath.Leaderboard;

public class ScoreData {

    String username;
    long WinRate;

    public ScoreData(String username, long winRate) {
        this.username = username;
        WinRate = winRate;
    }

    public ScoreData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getWinRate() {
        return WinRate;
    }

    public void setWinRate(long winRate) {
        WinRate = winRate;
    }
}
