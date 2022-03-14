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

public class AddBankAccount extends AppCompatActivity {
     Button cancelbtnuser;
     Button savebtn;
    private  String URL ="";
    private AppStorage objApp = null;
    Spinner bankname;
    String BankNameID,userid;
    EditText Holdername,AccNumber,BankIfsc,Bankbranch;
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
        getSupportActionBar().setTitle("Add Bank Account");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_add_bank_account);

        userid = objApp.getUserId();
        Holdername = (EditText) findViewById(R.id.Holdername);
        AccNumber = (EditText) findViewById(R.id.AccNumber);
        BankIfsc = (EditText) findViewById(R.id.BankIfsc);
        Bankbranch = (EditText) findViewById(R.id.Bankbranch);
        bankname=(Spinner)findViewById(R.id.bankname);
        cancelbtnuser =(Button) findViewById(R.id.cancelbtnuser);
        cancelbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBankAccount.this,AccountDetials.class);
                startActivity(intent);
                finish();
            }
        });
        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String holderName = Holdername.getText().toString().trim();
                String accNo = AccNumber.getText().toString().trim();
                String bankIfsc =  BankIfsc.getText().toString().trim();
                String bankBranch = Bankbranch.getText().toString().trim();
                if (holderName.isEmpty()) {
                    Holdername.setError("This field is required.");
                    Holdername.requestFocus();
                } else if (BankNameID.isEmpty()) {
                    TextView errorText = (TextView) bankname.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Your Bank Name");
                } else if (accNo.isEmpty()) {
                    AccNumber.setError("This field is required.");
                    AccNumber.requestFocus();
                } else if (bankIfsc.isEmpty()) {
                    BankIfsc.setError("This field is required.");
                    BankIfsc.requestFocus();
                } else if (bankBranch.isEmpty()) {
                    Bankbranch.setError("This field is required.");
                    Bankbranch.requestFocus();
                } else if (bankIfsc.length() < 11) {
                    BankIfsc.setError("Enter Valid Ifsc Code.");
                    BankIfsc.requestFocus();
                }else {
                    progressDialog = new ProgressDialog(AddBankAccount.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    addBankAccountForm(holderName,BankNameID,accNo,bankIfsc,bankBranch,userid);
                }

            }
        });
        bankNameList();
    }
    private void addBankAccountForm(String holderName, String bankNameID, String accNo, String bankIfsc, String bankBranch, String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addBankAcc",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddBankAccount.this,AccountDetials.class);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("holder_name",holderName);
                params.put("bank_id",bankNameID);
                params.put("acc_number",accNo);
                params.put("ifsc_code",bankIfsc);
                params.put("branch_name",bankBranch);
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void bankNameList() {
        ArrayList<AdapterListData> Bank_List = new ArrayList<AdapterListData>();
        Bank_List.add(new AdapterListData("","Select Your Bank Name "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddBankAccount.this,R.layout.my_selected_item,Bank_List);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        bankname.setAdapter(spinnerAdapter);
        bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                BankNameID =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL+"getBankName",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)

                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("id"),
                                        object.getString("Bank_Name"));
                                Bank_List.add(dropdown);
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
                });
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
}