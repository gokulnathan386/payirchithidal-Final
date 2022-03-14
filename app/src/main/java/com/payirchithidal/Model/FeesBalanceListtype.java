package com.payirchithidal.Model;

public class FeesBalanceListtype {
    private String payment_date;
    private String fees_date;
    private String credit;
    private String debit;
    public FeesBalanceListtype(String payment_date, String fees_date, String credit,String debit) {
        this.payment_date=payment_date;
        this.fees_date=fees_date;
        this.credit=credit;
        this.debit=debit;

    }

    public String getpayment_date() {
        return payment_date;
    }

    public String getFeesdate() {
        return fees_date;
    }

    public String getcredit() {
        return credit;
    }

    public String getdebit() {
        return debit;
    }
}
