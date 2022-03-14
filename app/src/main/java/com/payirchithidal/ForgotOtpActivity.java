package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ForgotOtpActivity extends AppCompatActivity {
    Button cancelbtnfotgot;
    TextView Otprtimer;
    private AppStorage objApp = null;
    private  String URL ="";
    String ForgotUserId ,Gmail;
    Button Verifybtn,resend;
    EditText otpone,otptwo,otpthree,otpfour;
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

        setContentView(R.layout.activity_forgot_otp);

        ForgotUserId = objApp.getForgotPwdId();
        Intent intent = getIntent();
        Gmail = intent.getStringExtra("Gmail");
        resend =(Button)findViewById(R.id.resend);
        otpone =(EditText) findViewById(R.id.otpone);
        otptwo =(EditText) findViewById(R.id.otptwo);
        otpthree =(EditText) findViewById(R.id.otpthree);
        otpfour =(EditText) findViewById(R.id.otpfour);

        Otprtimer =(TextView)findViewById(R.id.Otprtimer);

        CountDownTimerStart();

        resend.setVisibility(View.GONE);
        resend.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resend.setVisibility(View.VISIBLE);
                        resend.setEnabled(true);
                    }
                });
            }
        }, 300000);
        Verifybtn =(Button) findViewById(R.id.Verifybtn);
        Verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OtpOne = otpone.getText().toString().trim();
                String  OtpTwo = otptwo.getText().toString().trim();
                String OtpThree = otpthree.getText().toString().trim();
                String  OtpFour = otpfour.getText().toString().trim();
                if(OtpOne.isEmpty()){
                    otpone.requestFocus();
                }
                else if(OtpTwo.isEmpty()){
                    otptwo.requestFocus();
                }
                else if(OtpThree.isEmpty()){
                    otpthree.requestFocus();
                }
                else if(OtpFour.isEmpty()){
                    otpfour.requestFocus();
                }
                else{
                    progressDialog = new ProgressDialog(ForgotOtpActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    String OTP = OtpOne+OtpTwo+OtpThree+OtpFour;
                    UpdateForgotOtp(ForgotUserId,OTP);
                }
            }
        });



        otpone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    otptwo.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        otptwo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpthree.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                otpone.requestFocus();
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        otpthree.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpfour.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                otptwo.requestFocus();
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        otpfour.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpfour.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                otpthree.requestFocus();

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        cancelbtnfotgot = (Button) findViewById(R.id.cancelbtnfotgot);
        cancelbtnfotgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ForgotOtpActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
             ResendOtp(Gmail);
            }
        });
    }

    private void ResendOtp(String gmail) {
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
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                CountDownTimerStart();
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
                params.put("email",gmail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CountDownTimerStart() {
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                Otprtimer.setText(f.format(min) + ":" + f.format(sec));
            }
            public void onFinish() {
                Otprtimer.setText("00:00");

            }
        }.start();
    }

    private void UpdateForgotOtp(String forgotUserId, String otp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"passVerify",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(ForgotOtpActivity.this,ForgotPwdUpdate.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
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
                params.put("otp",otp);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}