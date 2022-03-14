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
import com.payirchithidal.CustomerAdapter.CustomAdapterRole;
import com.payirchithidal.Model.RoleList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoleActivityList extends AppCompatActivity {
    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ListView rolelist;
    ArrayList<RoleList> list_Role;
    CustomAdapterRole customAdapter;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_role_list);

        progressDialog = new ProgressDialog(RoleActivityList.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        ImageView backbtnrole = (ImageView)findViewById(R.id.backbtnrole);
        backbtnrole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView addlist = (TextView) findViewById(R.id.addlist);
        addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RoleActivityList.this,AddRoleListActivity.class);
                startActivity(intent);
            }
        });

        search_edt =(SearchView)findViewById(R.id.search_edt);
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
                ArrayList<RoleList> filtered = new ArrayList<RoleList>();
                for (int i = 0; i < list_Role.size() ; i++){
                    String Name = String.valueOf(list_Role.get(i).getRoleName());
                    if (Name.contains(newText)){
                        RoleList contents = new RoleList(list_Role.get(i).getRoleId(),
                             list_Role.get(i).getRoleName());
                        filtered.add(contents);
                    }

                    customAdapter = new CustomAdapterRole(getApplicationContext(),R.layout.attendancelist_design,filtered);
                    rolelist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
        String userid = objApp.getUserId();
        Rolelist(userid);

    }
    private void Rolelist(String userid) {
        rolelist = (ListView)findViewById(R.id.rolelist);
        list_Role = new ArrayList<>();
        customAdapter = new CustomAdapterRole(getApplicationContext(),R.layout.attendancelist_design,list_Role);
        rolelist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getRolesList",
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
                                    RoleList attendanceList = new RoleList(object.getString("role_id"),
                                            object.getString("role_name"));
                                    list_Role.add(attendanceList);

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
}