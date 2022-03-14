package com.payirchithidal.Model;

public class KitList {
    private String kitname;
    private String Branchname;
    private String kitimg;
    private String KitId;


    public KitList(String kitname, String Branchname, String kitimg, String kitid) {
        this.kitname = kitname;
        this.Branchname = Branchname;
        this.kitimg=kitimg;
        this.KitId=kitid;
    }

    public String getkitname() {
        return kitname;
    }

    public String getBranchname() {
        return Branchname;
    }

     public String getKitimg() {
        return kitimg;
    }

    public String getKitId() {
        return KitId;
    }

}
