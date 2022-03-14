package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

public class EditUserActivity extends AppCompatActivity {
    Intent intent;
    String id;
    EditText firstname;
    private  String URL ="";
    private AppStorage objApp = null;
    String UserFirstName,UserLastName,UserMail;
    EditText lastname,email;
    String userid;
    Spinner roles,DropdownBranch;
    String roleid,branchid,UserRoleID,UserBranchID;
    Button savebtn;
    String emailPattern;
    ArrayList<AdapterListData> roleslist;
    ProgressDialog progressDialog;
    String FirstNameUser,LastNameUser,EmailUser,UserBranchId,RoleIDUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Update Form");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_edit_user);
        progressDialog = new ProgressDialog(EditUserActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        intent = getIntent();
        id = intent.getStringExtra("User_Id");

        userid = objApp.getUserId();
        firstname =(EditText)findViewById(R.id.firstname);
        lastname =(EditText) findViewById(R.id.lastname);
        email =(EditText)findViewById(R.id.email);
        roles =(Spinner) findViewById(R.id.roles);
        DropdownBranch = (Spinner) findViewById(R.id.branch);

        Button celbtnuser= (Button) findViewById(R.id.celbtnuser);
        celbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirstNameUser=firstname.getText().toString().trim();
                LastNameUser=lastname.getText().toString().trim();
                EmailUser=email.getText().toString().trim();
                UserBranchId = branchid;
                RoleIDUser = roleid;

                emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(FirstNameUser.isEmpty()){
                    firstname.setError("This field is required.");
                    firstname.requestFocus();
                }
                else if (LastNameUser.isEmpty()){
                    lastname.setError("This field is required.");
                    lastname.requestFocus();

                }else if (EmailUser.isEmpty()){
                    email.setError("This field is required.");
                    email.requestFocus();
                }else if(!EmailUser.matches(emailPattern)){
                    email.setError("Invalid email address.");
                    email.requestFocus();
                }else if (roleid.isEmpty()){
                    TextView errorText = (TextView)roles.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Role");
                }
                else if (branchid.isEmpty()){
                    TextView errorText = (TextView)DropdownBranch.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                }else{
                    progressDialog = new ProgressDialog(EditUserActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    updateUser(EmailUser,FirstNameUser,LastNameUser,UserBranchId,RoleIDUser,userid);
                }
            }
        });

        new BackgroundTask(EditUserActivity.this) {
            @Override
            public void doInBackground() {
                vIEWuSER(id);
            }

            @Override
            public void onPostExecute() {

            }
        }.execute();

    }
    private void updateUser(String emailUser, String firstNameUser, String lastNameUser, String userBranchId, String roleIDUser, String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"updateUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Update User Success",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditUserActivity.this,UserslistActivity.class);
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
                params.put("email",emailUser);
                params.put("first_name",firstNameUser);
                params.put("last_name",lastNameUser);
                params.put("branch",userBranchId);
                params.put("role_id",roleIDUser);
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Branchdropdownlist(String userid) {
        ArrayList<AdapterListData> branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select  Branch "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditUserActivity.this,R.layout.my_selected_item,branchlist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        DropdownBranch.setAdapter(spinnerAdapter);
        DropdownBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            int positionBranch = getDropDownIndex(branchlist,UserBranchID);
                            DropdownBranch.setSelection(positionBranch);
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



    private void Rolesdropdownlist(String userid) {
        roleslist = new ArrayList<AdapterListData>();
        roleslist.add(new AdapterListData("","Select Role "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditUserActivity.this,R.layout.my_selected_item,roleslist);
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
                            int positionRole = getDropDownIndex(roleslist,UserRoleID);
                            roles.setSelection(positionRole);
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
                        Toast.makeText(EditUserActivity.this,error.toString(), Toast.LENGTH_LONG).show();

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

    private void vIEWuSER(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            Log.d("data",response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                UserFirstName = data.getString("first_name");
                                firstname.setText(UserFirstName);
                                UserLastName = data.getString("last_name");
                                lastname.setText(UserLastName);
                                UserMail = data.getString("email");
                                email.setText(UserMail);
                                UserRoleID = data.getString("role_id");
                                UserBranchID = data.getString("branch_id");

                                Rolesdropdownlist(userid);
                                Branchdropdownlist(userid);
                                progressDialog.dismiss();
                            }

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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    public int getDropDownIndex(ArrayList objList,String id)
    {
        int index = 0;
        for(int i = 0;i < objList.size();i++)
        {
            AdapterListData objData = (AdapterListData) objList.get(i);
            if(objData.id.equals(id))
            {
                index = i;
                break;
            }
        }
        return index;
    }

}