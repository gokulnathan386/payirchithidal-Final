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

public class EditAccountDetails extends AppCompatActivity {
    Intent intent;
    String accId;
    ProgressDialog progressDialog;
    Button celbtnacc;
    private  String URL ="";
    private AppStorage objApp = null;
    EditText accHolder,accNo,ifscCode,branchName;
    Spinner bankName;
    String holdName,bankId,accountNumber,AccifscNO,BranchName;
    String BankNameID;
    Button savebtn;
    ArrayList<AdapterListData> Bank_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Update Form");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_edit_account_details);
        progressDialog = new ProgressDialog(EditAccountDetails.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        intent = getIntent();
        accId = intent.getStringExtra("accountId");
        accHolder =(EditText) findViewById(R.id.accHolder);
        accNo =(EditText) findViewById(R.id.accName);
        bankName =(Spinner) findViewById(R.id.bankName);
        ifscCode =(EditText) findViewById(R.id.ifscCode);
        branchName =(EditText) findViewById(R.id.branchName);


        celbtnacc =(Button) findViewById(R.id.celbtnacc);
        celbtnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accHolder.getText().toString().trim().isEmpty()) {
                    accHolder.setError("This field is required.");
                    accHolder.requestFocus();
                } else if (BankNameID.isEmpty()) {
                    TextView errorText = (TextView) bankName.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Your Bank Name");
                } else if (accNo.getText().toString().trim().isEmpty()) {
                    accNo.setError("This field is required.");
                    accNo.requestFocus();
                } else if (ifscCode.getText().toString().trim().isEmpty()) {
                    ifscCode.setError("This field is required.");
                    ifscCode.requestFocus();
                }else if (ifscCode.getText().toString().trim().length() < 11) {
                    ifscCode.setError("Enter Valid Ifsc Code");
                    ifscCode.requestFocus();
                } else if (branchName.getText().toString().trim().isEmpty()) {
                    branchName.setError("This field is required.");
                    branchName.requestFocus();
                } else {
                    progressDialog = new ProgressDialog(EditAccountDetails.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    updateBankAcc(accId, accHolder.getText().toString().trim(), BankNameID, accNo.getText().toString().trim(), ifscCode.getText().toString().trim(), branchName.getText().toString().trim());
                }
            }
        });
        new BackgroundTask(EditAccountDetails.this) {
            @Override
            public void doInBackground() {
                viewAccDetail(accId);
            }

            @Override
            public void onPostExecute() {

            }
        }.execute();
    }

    private void updateBankAcc(String accId, String accholder, String bankNameID, String accno, String ifsccode, String branchname) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"updateBankAcc",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Intent intent = new Intent(EditAccountDetails.this,AccountDetials.class);
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
            public Map<String, String> getParams() throws AuthFailureError  {
                Map<String,String> params = new HashMap<>();
                params.put("holder_name",accholder);
                params.put("bank_id",bankNameID);
                params.put("acc_number",accno);
                params.put("ifsc_code",ifsccode);
                params.put("branch_name",branchname);
                params.put("account_id",accId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void bankNameList() {
        Bank_List = new ArrayList<AdapterListData>();
        Bank_List.add(new AdapterListData("","Select Your Bank Name "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditAccountDetails.this,R.layout.my_selected_item,Bank_List);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        bankName.setAdapter(spinnerAdapter);
        bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            int PositionBankName = getDropDownIndex(Bank_List,bankId);
                            bankName.setSelection(PositionBankName);
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

    public int getDropDownIndex(ArrayList objList,String id)
    {
     Log.d("name", "\n" +objList + "  " +"\n"+id);
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
    private void viewAccDetail(String accId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBankAcc",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                holdName = data.getString("holder_name");
                                accHolder.setText(holdName);
                                bankId = data.getString("bank_id");
                                accountNumber = data.getString("acc_number");
                                accNo.setText(accountNumber);
                                AccifscNO = data.getString("ifsc_code");
                                ifscCode.setText(AccifscNO);
                                BranchName = data.getString("branch_name");
                                branchName.setText(BranchName);

                                bankNameList();
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
                params.put("id",accId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }

}