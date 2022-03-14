package com.payirchithidal.Model;

public class RoleList {

    private String role_id;
    private String role_name;


    public RoleList(String roleid, String rolename) {
        this.role_id=roleid;
        this.role_name = rolename;

    }

    public String getRoleId() {
        return role_id;
    }

    public String getRoleName() {
        return role_name;
    }


}
