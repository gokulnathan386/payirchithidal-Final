package com.payirchithidal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.CustomerAdapter.CustomAdapteruser;
import com.payirchithidal.Model.UserList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserslistActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ListView userlist;
    ArrayList<UserList> list_user;
    CustomAdapteruser customAdapter;
    TextView RecordNotFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_userslist);

        progressDialog = new ProgressDialog(UserslistActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        String userid = objApp.getUserId();
        Userlist(userid);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        ImageView backbtnuser=(ImageView)findViewById(R.id.backbtnuser);
        backbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView addbtnuser =(TextView) findViewById(R.id.addbtnuser);
        addbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(UserslistActivity.this, AddUserlistActivity.class);
                startActivity(intent);
            }
        });
        search_edt = (SearchView)findViewById(R.id.search_edt);
        search_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edt.onActionViewExpanded();
            }
        });
        search_edt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<UserList> filtered = new ArrayList<UserList>();
                for (int i = 0; i < list_user.size() ; i++){
                    String Name = String.valueOf(list_user.get(i).getfirstname());
                    if (Name.contains(newText)){
                        UserList contents = new UserList(list_user.get(i).getfirstname(),
                                list_user.get(i).getemail(),list_user.get(i).getrolename(),list_user.get(i).getUserId(),
                               list_user.get(i).getClub()
                        );
                        filtered.add(contents);
                    }

                    customAdapter = new CustomAdapteruser(getApplicationContext(),R.layout.userlist_design,filtered);
                    userlist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Userlist(String userid) {
        userlist= (ListView)findViewById(R.id.userlist);
        list_user = new ArrayList<>();
        customAdapter = new CustomAdapteruser(getApplicationContext(),R.layout.userlist_design,list_user);
        userlist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getUserList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            progressDialog.dismiss();
                            if(0==jsonarray.length()){
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                UserList userlist = new UserList(object.getString("first_name"),
                                        object.getString("email"),
                                        object.getString("role_name"),
                                        object.getString("user_id"),
                                        object.getString("club_name")
                                );
                                list_user.add(userlist);
                            }
                            }
                            customAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    public void onBackPressed(){
        Intent intent = new Intent(UserslistActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}