package com.payirchithidal.Model;

public class PlayList {
    private String profile;
    private String player_name;
    private String mobile;
    private String email;
    private String playerId;



    public PlayList(String profile, String player_name, String mobile, String email, String playerId ) {
        this.profile=profile;
        this.player_name=player_name;
        this.mobile=mobile;
        this.email=email;
        this.playerId=playerId;


    }

    public String getprofile() {
        return profile;
    }

    public String getplayername() {
        return player_name;
    }

    public String getmobile() {
        return mobile;
    }

    public String getemail() {
        return email;
    }

    public String getPlayerId() {
        return playerId;
    }

}
