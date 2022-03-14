package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.EditUserActivity;
import com.payirchithidal.R;
import com.payirchithidal.Model.UserList;
import com.payirchithidal.UserslistActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapteruser extends ArrayAdapter<UserList> {

    ArrayList<UserList> userList;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapteruser(@NonNull Context context, int resource, @NonNull  ArrayList<UserList> userList) {
        super(context, resource, userList);
        this.userList=userList;
        this.context=context;
        this.resource=resource;
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.userlist_design,null,true);
        }

        UserList pr=getItem(position);

        TextView firstname = (TextView) convertView.findViewById(R.id.firstname);
        TextView club =(TextView) convertView.findViewById(R.id.club);
        TextView kitid =(TextView) convertView.findViewById(R.id.kitid);

        ImageView userdel =(ImageView) convertView.findViewById(R.id.userdel);
        View finalConvertView = convertView;
        userdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        userDel(pr.getUserId());
                        Intent intent = new Intent(context, UserslistActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        ImageView edituser =(ImageView) convertView.findViewById(R.id.edituser);
        edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUser(pr.getUserId());
            }
        });


        firstname.setText(pr.getfirstname());
        club.setText(pr.getClub());
        return convertView;
    }
    private void userDel(String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",userId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);


    }
    private void EditUser(String userId) {
        Intent intent = new Intent(context, EditUserActivity.class);
        intent.putExtra("User_Id", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
