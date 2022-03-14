package com.payirchithidal.Model;

public class SportsList {

    private String sports_name;
    private String icon;
    private String SportsId;



    public SportsList(String Sportsname, String Icon, String SportsId) {

        this.sports_name=Sportsname;
        this.icon = Icon;
        this.SportsId=SportsId;

    }


    public String getsportsname() {
        return sports_name;
    }

    public String geticon() {
        return icon;
    }
    public String getsportId() {
        return SportsId;
    }


}
