package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText CurrentPassword;
    EditText newpass;
    EditText confirmpass;
    Button savebtn;
    String OldPwd;
    ProgressDialog progressDialog;
    String NPwd;
    String CPwd;
    String userid;
    private  String URL ="";
    private AppStorage objApp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        setContentView(R.layout.activity_change_password);

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        userid = objApp.getUserId();

        CurrentPassword =(EditText)findViewById(R.id.CurrentPassword);
        newpass =(EditText)findViewById(R.id.newpass);
        confirmpass =(EditText)findViewById(R.id.confirmpass);

        TextView cancelbtnpass = (TextView) findViewById(R.id.cancelbtnpass);
        cancelbtnpass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        savebtn =(Button)findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OldPwd =CurrentPassword.getText().toString().trim();
                NPwd =newpass.getText().toString().trim();
                CPwd =confirmpass.getText().toString().trim();



                if(OldPwd.isEmpty()){
                    CurrentPassword.setError("This field is required.");
                    CurrentPassword.requestFocus();
                }
                else if (NPwd.isEmpty()){
                    newpass.setError("This field is required.");
                    newpass.requestFocus();

                }else if (CPwd.isEmpty()){
                    confirmpass.setError("This field is required.");
                    confirmpass.requestFocus();
                }
              else{
                    progressDialog = new ProgressDialog(ChangePasswordActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    UpdatePassword(OldPwd,NPwd,CPwd,userid);
                }
            }
        });


    }

    private void UpdatePassword(String oldPwd, String nPwd, String cPwd,String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"changePassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");

                            if(success.equals("1")){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangePasswordActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else if(success.equals("0")){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"PassWord Error !"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Registration Error !"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("new_pass",nPwd);
                params.put("old_pass",oldPwd);
                params.put("confirm_pass",cPwd);
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}