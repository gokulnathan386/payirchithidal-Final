package com.payirchithidal.Model;

public class FeesListPlayer {
    private String credit;
    private String status;
    private String fees_date;
    private String payment_date;
    private  String debit;


    public FeesListPlayer( String credit,String debit, String status, String fees_date, String payment_date) {
        this.credit=credit;
        this.status=status;
        this.fees_date=fees_date;
        this.payment_date=payment_date;
        this.debit=debit;
    }

    public String getCredit() {
        return credit;
    }

    public String getDebit() {
        return debit;
    }

    public String getstatus() {
        return status;
    }

    public String getFeesdate(){
        return fees_date;
    }

    public String getPaymentDate() {
        return payment_date;
    }


}
