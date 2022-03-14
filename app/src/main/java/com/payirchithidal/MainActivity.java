package com.payirchithidal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private  String URL ="";
    private AppStorage objApp = null;
    EditText txtPass,txtEmail;
    String emailPattern,SharedKeyUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_main);
        TextView tvForgotPwd = (TextView) findViewById(R.id.forgottxt);
        txtEmail = (EditText) findViewById(R.id.emailedit);
        txtPass = (EditText) findViewById(R.id.passwordedit);
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });
        Button btLogin = (Button) findViewById(R.id.loginbtn);
        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if (txtEmail.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
                    } else if (txtPass.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                    } else if(!txtEmail.getText().toString().trim().matches(emailPattern)){
                        Toast.makeText(MainActivity.this, "Invalid email address", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );


                        dashboard(txtEmail.getText().toString().trim(),txtPass.getText().toString().trim());
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Please enable internet data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void dashboard(String empId,String Pass) {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,URL+"login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            String Message = jsonobject.getString("msg");
                            if (success.equals("true")) {
                                JSONObject login = jsonobject.getJSONObject("user_details");
                                String firstName = login.getString("first_name");
                                String club_id = login.getString("club_id");
                                String user_id = login.getString("user_id");
                                objApp.setUserName(firstName);
                                objApp.setClubId(club_id);
                                objApp.setUserId(user_id);

                                Intent intent  = new Intent(MainActivity.this,HomeActivity.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this,Message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", empId);
                params.put("password",Pass);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);

    }
    private boolean validateEmail() {
        String emailInput = txtEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            txtEmail.setError("This field is required.");
            txtEmail.requestFocus();
            return false;
        } else {
            txtEmail.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String pedInput = txtPass.getText().toString().trim();
        if (pedInput.isEmpty()) {
            txtPass.setError("This field is required.");
            txtPass.requestFocus();
            return false;
        } else {
            txtPass.setError(null);
            return true;
        }

    }
    public void confirmLogin(View v) {

        if(!validateEmail() |!validatePassword())
        {
            return;
        }

    }
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}