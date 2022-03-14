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

public class ForgotPwdUpdate extends AppCompatActivity {
    Button UpdatePwd;
    EditText NewPwd,ConfirmPwd;
    private AppStorage objApp = null;
    private  String URL ="";
    String ForgotUserId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password Update");
        setContentView(R.layout.activity_forgot_pwd_update);
        ForgotUserId = objApp.getForgotPwdId();
        NewPwd =(EditText)findViewById(R.id.NewPwd);
        ConfirmPwd =(EditText)findViewById(R.id.ConfirmPwd);
        UpdatePwd =(Button)findViewById(R.id.UpdatePwd);
        UpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PwdNew = NewPwd.getText().toString().trim();
                String PwdConfirm = ConfirmPwd.getText().toString().trim();
               if(PwdNew.length() < 8 || PwdConfirm.length() <8){
                   NewPwd.setError("Password required minimum 8  characters");
                   NewPwd.requestFocus();
                   ConfirmPwd.setError("Password required minimum 8  characters");
                   ConfirmPwd.requestFocus();
                }
                else if(!PwdNew.equals(PwdConfirm)){
                   ConfirmPwd.setError("The password and confirmation password do not match.");
                   ConfirmPwd.requestFocus();
                }
               else{
                   progressDialog = new ProgressDialog(ForgotPwdUpdate.this);
                   progressDialog.show();
                   progressDialog.setCancelable(false);
                   progressDialog.setContentView(R.layout.progress_dialog);
                   progressDialog.getWindow().setBackgroundDrawableResource(
                           android.R.color.transparent
                   );
                   ResetPwd(PwdNew,PwdConfirm,ForgotUserId);
               }

            }
        });
    }

    private void ResetPwd(String pwdNew, String pwdConfirm, String forgotUserId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"reset_exe",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPwdUpdate.this,MainActivity.class);
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
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",forgotUserId);
                params.put("password",pwdNew);
                params.put("cpassword",pwdConfirm);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}