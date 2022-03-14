package com.payirchithidal.Model;

public class FeesList {

    private String player_name;
    private String branch;
    private String credit;
    private String debit;
    private String payment_date;
    private String fees_date;
    private String profile;
    private String FeesId;




    public FeesList(String player_name, String branch, String credit,String debit, String payment_date,String fees_date, String profile, String fessId) {
        this.player_name=player_name;
        this.branch=branch;
        this.credit=credit;
        this.debit=debit;
        this.payment_date=payment_date;
        this.fees_date = fees_date;
        this.profile = profile;
        this.FeesId = fessId;
    }

    public String getplayername() {
        return player_name;
    }

    public String getbranch() {
        return branch;
    }

    public String getCredit() {
        return credit;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public String getProfile() {
        return profile;
    }

    public String getFeesId() {
        return FeesId;
    }

    public String getFees_date() {
        return fees_date;
    }

    public String getDebit() {
        return debit;
    }

}
