package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUserlistActivity extends AppCompatActivity {

    Spinner branch_dropdown;
    Spinner roles;
    String branchid;
    String roleid;
    private  String URL ="";
    private AppStorage objApp = null;
    Button savebtn;
    String userid;
    EditText firstname;
    EditText lastname;
    EditText email;
    String fname;
    String lname;
    String emil;
    String RoleId;
    String BranchId;
    String emailPattern;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add User Form");
        setContentView(R.layout.activity_add_userlist);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();


        roles = (Spinner) findViewById(R.id.roles);
        branch_dropdown = (Spinner) findViewById(R.id.branch);
        userid = objApp.getUserId();

        savebtn = (Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname =(EditText) findViewById(R.id.firstname);
                lastname =(EditText) findViewById(R.id.lastname);
                email =(EditText) findViewById(R.id.email);

                fname =firstname.getText().toString().trim();
                lname =lastname.getText().toString().trim();
                emil =email.getText().toString().trim();
                RoleId= String.valueOf(roleid);
                BranchId= String.valueOf(branchid);
                emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(fname.isEmpty()){
                    firstname.setError("This field is required.");
                    firstname.requestFocus();
                }
                else if (lname.isEmpty()){
                    lastname.setError("This field is required.");
                    lastname.requestFocus();

                }else if (emil.isEmpty()){
                    email.setError("This field is required.");
                    email.requestFocus();
                }else if(!emil.matches(emailPattern)){
                    email.setError("Invalid email address.");
                    email.requestFocus();
                }else if (RoleId.isEmpty()){
                    TextView errorText = (TextView)roles.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Role");
                }
                else if (BranchId.isEmpty()){
                    TextView errorText = (TextView)branch_dropdown.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                }else{
                    progressDialog = new ProgressDialog(AddUserlistActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    AdduserForm(emil,fname,lname,BranchId,RoleId,userid);
                }
            }
        });


        Branchdropdownlist(userid);
        Rolesdropdownlist(userid);


        Button cancelbtnuser=(Button) findViewById(R.id.cancelbtnuser);
        cancelbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddUserlistActivity.this, UserslistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AdduserForm(String emil,String fname,String lname,String BranchId,String RoleId,String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Add User Success",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddUserlistActivity.this,UserslistActivity.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("email",emil);
                params.put("first_name",fname);
                params.put("last_name",lname);
                params.put("branch",BranchId);
                params.put("role_id",RoleId);
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Rolesdropdownlist(String userid) {
        ArrayList<AdapterListData> roleslist = new ArrayList<AdapterListData>();
        roleslist.add(new AdapterListData("","Select Role "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddUserlistActivity.this,R.layout.my_selected_item,roleslist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        roles.setAdapter(spinnerAdapter);
        roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                roleid =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getRolesList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("role_id"),
                                        object.getString("role_name"));
                                roleslist.add(dropdown);
                            }
                            spinnerAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
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
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }

    private void Branchdropdownlist(String userid) {
        ArrayList<AdapterListData> branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select  Branch "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddUserlistActivity.this,R.layout.my_selected_item,branchlist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        branch_dropdown.setAdapter(spinnerAdapter);
        branch_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData id1 = (AdapterListData)parent.getItemAtPosition(position);
                branchid =id1.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("branch_id"),
                                        object.getString("branch_name"));
                                branchlist.add(dropdown);
                            }
                            spinnerAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
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
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
}