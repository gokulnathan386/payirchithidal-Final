package com.payirchithidal.Model;

public class PayoutList {

    private String club_name;
    private String amount;
    private String reason;
    private String KitId;


    public PayoutList(String club_name, String amount, String reason) {
        this.club_name = club_name;
        this.amount = amount;
        this.reason=reason;
    }

    public String getclubname() {
        return club_name;
    }

    public String getamount() {
        return amount;
    }

    public String getreason() {
        return reason;
    }

}
