package com.payirchithidal.Model;

public class FeesPending {
    private String player_id;
    private String total;
    private String paid;
    private String balance;
    private String first_name;
    private String player;
    public FeesPending(String player_id, String total, String paid, String balance, String first_name, String player) {
        this.player_id=player_id;
        this.total = total;
        this.paid=paid;
        this.balance=balance;
        this.first_name = first_name;
        this.player=player;
    }
    public String get_PlayerId() {
        return player_id;
    }

    public String get_Total() {
        return total;
    }
    public String get_Paid() {
        return paid;
    }
    public String get_Balance() {
        return balance;
    }

    public String get_FirstName() {
        return first_name;
    }
    public String get_Player() {
        return player;
    }
}
