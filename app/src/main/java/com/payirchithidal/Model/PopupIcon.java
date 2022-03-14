package com.payirchithidal.Model;

public class PopupIcon {
    private String profile;
    private String name;
    private String message;
    private String created_at;

    public PopupIcon(String profile, String name, String message , String created_at) {

        this.profile=profile;
        this.name = name;
        this.message = message;
        this.created_at = created_at;

    }

    public String getprofile() {
        return profile;
    }

    public String getname() {
        return name;
    }

    public String getmessage() {
        return message;
    }

    public String getcreated_at() {
        return created_at;
    }
}
