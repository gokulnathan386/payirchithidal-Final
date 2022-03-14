package com.payirchithidal.Model;

public class  BranchList {
    private String branch_name;
    private String address;
    private String address1;
    private String city;
    private String branch_id;


    public BranchList(String branch_name, String address, String address1, String city, String branchId) {
        this.branch_name=branch_name;
        this.address=address;
        this.address1=address1;
        this.city=city;
        this.branch_id=branchId;
    }

    public String getbranch_name() {
        return branch_name;
    }

    public String getaddress() {
        return address;
    }

    public String getaddress1() {
        return address1;
    }

    public String getcity() {
        return city;
    }

    public String  getBranchId() {
        return branch_id;
    }

}

