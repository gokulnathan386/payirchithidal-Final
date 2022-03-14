
package com.payirchithidal;
public class AdapterListData {
    public String id;
    public String name;

    public AdapterListData(String id, String name){
        this.id=id;
        this.name=name;
    }
    @Override
    public String toString(){
        return name;
    }

}