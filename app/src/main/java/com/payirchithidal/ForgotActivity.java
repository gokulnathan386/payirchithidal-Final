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

public class ForgotActivity extends AppCompatActivity {

    Button savebtn;
    EditText forgottxt;
    String Forgotpwd;
    private  String URL ="";
    private AppStorage objApp = null;
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
        getSupportActionBar().setTitle("Forgot Password");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_forgot);
         Button Forgotpass = (Button) findViewById(R.id.cancelbtnfotgot);
        forgottxt =(EditText) findViewById(R.id.forgottxt);

        Forgotpass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Forgotpwd = forgottxt.getText().toString().trim();

              if(Forgotpwd.isEmpty()){
                  forgottxt.setError("This field is required.");
                  forgottxt.requestFocus();
              }else{
                  progressDialog = new ProgressDialog(ForgotActivity.this);
                  progressDialog.show();
                  progressDialog.setCancelable(false);
                  progressDialog.setContentView(R.layout.progress_dialog);
                  progressDialog.getWindow().setBackgroundDrawableResource(
                          android.R.color.transparent
                  );
                  ForgotPassword(Forgotpwd);
              }
            }

        });

    }
    private void ForgotPassword(String forgotpwd) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"forgotpassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            progressDialog.dismiss();
                            if(success.equals("1")){
                                String ForgotIdUser = jsonObject.getString("user_id");
                                objApp.setForgotPwdId(ForgotIdUser);
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotActivity.this,ForgotOtpActivity.class);
                                intent.putExtra("Gmail", forgotpwd);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Invalid Email Id",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
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
                params.put("email",forgotpwd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}