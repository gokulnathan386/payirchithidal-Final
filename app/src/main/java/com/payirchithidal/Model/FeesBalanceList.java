package com.payirchithidal.Model;

public class FeesBalanceList {
    private String Fees_id;
    private String Fees_date;
    private String Credit;
    private String type;



    public FeesBalanceList(String fees_id, String fees_date, String credit,String type) {
        this.Fees_id=fees_id;
        this.Fees_date=fees_date;
        this.Credit=credit;
        this.type=type;

    }

    public String getFeesId() {
        return Fees_id;
    }

    public String getFeesDate() {
        return Fees_date;
    }

    public String getCredit() {
        return Credit;
    }

    public String gettype() {
        return type;
    }


}
