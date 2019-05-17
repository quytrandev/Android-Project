package com.quytran.battletext;

public class ResponseMessage {
    //vars
    String message;
    boolean isPlayer;           //check xem nguoi gui message la player hay bot

    //Constructor
    public ResponseMessage(String message, boolean isPlayer) {
        this.message = message;
        this.isPlayer = isPlayer;
    }



    //Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }






}
