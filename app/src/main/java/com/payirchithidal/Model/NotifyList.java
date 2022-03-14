package com.payirchithidal.Model;

public class NotifyList {

    private String profile;
    private String first_name;
    private String branch_name;

    public NotifyList(String profile, String first_name, String branch_name) {

        this.profile=profile;
        this.first_name = first_name;
        this.branch_name = branch_name;

    }

    public String getprofile() {
        return profile;
    }

    public String getfirstname() {
        return first_name;
    }

    public String getbranchname() {
        return branch_name;
    }

}
