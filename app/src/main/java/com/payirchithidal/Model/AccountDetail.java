package com.payirchithidal.Model;

public class AccountDetail {
    private String account_id;
    private String club_name;
    private String holder_name;
    private String bank_name;
    private String acc_number;
    private String ifsc_code;
    private String branch_name;
    public AccountDetail(String account_id, String club_name, String holder_name, String bank_name, String acc_number, String ifsc_code, String branch_name) {
        this.account_id=account_id;
        this.club_name=club_name;
        this.holder_name=holder_name;
        this.bank_name=bank_name;
        this.acc_number=acc_number;
        this.ifsc_code=ifsc_code;
        this.branch_name=branch_name;
    }
    public String getAccount_id() {
        return account_id;
    }

    public String getCulbName() {
        return club_name;
    }

    public String getHolderName() {
        return holder_name;
    }

    public String getBankName() {
        return bank_name;
    }

    public String getAccountName() {
        return acc_number;
    }

    public String getIfscCode() {
        return ifsc_code;
    }

    public String getBranchName() {
        return branch_name;
    }

}
