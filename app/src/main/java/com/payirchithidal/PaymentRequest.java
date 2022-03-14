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

public class PaymentRequest extends AppCompatActivity {
    TextView pendingmmount;
    private  String URL ="";
    private AppStorage objApp = null;
    String userid;
    Button cancelbtn,savebtn;
    EditText requestamount;
    String amtrequest,amtb,cmt;
    EditText comment;
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
        getSupportActionBar().setTitle("Payment Request");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_payment_request);
        userid = objApp.getUserId();
        pendingmmount =(TextView) findViewById(R.id.pendingmmount);
        viewPayoutAmount(userid);
        cancelbtn =(Button) findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestamount = (EditText) findViewById(R.id.requestamount);
                comment = (EditText) findViewById(R.id.comment);
                cmt =  comment.getText().toString().trim();
                amtb = pendingmmount.getText().toString().trim();
                amtrequest = requestamount.getText().toString().trim();
                if(amtrequest.isEmpty()){
                    requestamount.setError("This field is required.");
                    requestamount.requestFocus();
                }else if(Integer.parseInt(amtrequest) > Integer.parseInt(pendingmmount.getText().toString())){
                    requestamount.setError("This field is required.");
                    requestamount.requestFocus();
                }else{
                    progressDialog = new ProgressDialog(PaymentRequest.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    addPaymentRequest(userid,amtrequest,cmt);
                }
            }
        });
    }

    private void addPaymentRequest(String userid, String amtrequest, String cmt) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addRequest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            if(success.equals("true")){
                                Intent intent = new Intent(PaymentRequest.this,PayoutListActivity.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
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
                params.put("user_id",userid);
                params.put("amount",amtrequest);
                params.put("comments",cmt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void viewPayoutAmount(String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPayoutAmount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            if(success.equals("true")){
                                String Data = jsonObject.getString("data");
                                pendingmmount.setText(Data);
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
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}