package com.payirchithidal.session;

import java.util.ArrayList;

public class AppStorage {
    private static AppStorage objAppStorage = null;
    public String apiUrl = "http://payirchithidal.com/public/api/";
    public String userName = "";
    public String user_id="";
    public String clubId = String.valueOf(0);
    public String apiImageUrl="http://payirchithidal.com/public/upload";
    public String ForgotPwdId="";
    public String Playercount="";
    public  String KitCount ="";
    public  String Batchcount="";
    public ArrayList<Integer> feedId = new ArrayList<>();
    public static AppStorage getInstance()
    {
        if (objAppStorage == null) {
            objAppStorage = new AppStorage();
        }

        return objAppStorage;
    }

    public String getApiUrl(){
        return this.apiUrl;
    }

    public String getImageUrl(){
        return this.apiImageUrl;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setClubId(String clubId){
        this.clubId = clubId;
    }
    public String getClubId(){
        return this.clubId;
    }
    public void setUserId(String userId){
        this.user_id = userId;
    }
    public String getUserId(){
        return this.user_id;
    }

    public void setForgotPwdId(String ForgotPwd){
        this.ForgotPwdId = ForgotPwd;
    }
    public String getForgotPwdId(){
        return this.ForgotPwdId;
    }

    public void addItem(int id)
    {
        feedId.add(id);
    }
    public void removeItem(int id)
    {
        feedId.remove(Integer.valueOf(id));
       // feedId.remove(id);
    }
    public void removeAll()
    {
        feedId.clear();
    }
    public ArrayList getArrayList()
    {
        return feedId;
    }

}
