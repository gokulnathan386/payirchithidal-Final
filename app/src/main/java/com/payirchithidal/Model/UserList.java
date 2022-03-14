package com.payirchithidal.Model;

public class UserList {
    private String first_name;
    private String email;
    private String role_name;
    private String  UserID;
    private String  club;

    public UserList(String first_name, String email, String role_name, String user_id, String club) {
        this.first_name=first_name;
        this.email=email;
        this.role_name=role_name;
        this.UserID=user_id;
        this.club=club;

    }

    public String getfirstname() {
        return first_name;
    }

    public String getemail() {
        return email;
    }

    public String getrolename() {
        return role_name;
    }

    public String getUserId() {
        return UserID;
    }

    public String getClub() {
        return club;
    }

}
