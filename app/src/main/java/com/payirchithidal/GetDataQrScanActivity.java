package com.payirchithidal;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.payirchithidal.CustomerAdapter.CustomAdapterplayer;
import com.payirchithidal.CustomerAdapter.CustomAdapterplayertype;
import com.payirchithidal.Model.FeesBalanceList;
import com.payirchithidal.Model.FeesBalanceListtype;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GetDataQrScanActivity extends AppCompatActivity {
    Intent intent;
    String qrcode;
    ProgressDialog progressDialog;
    ArrayList<Integer> feesArray;
    private String URL = "";
    private AppStorage objApp = null;
    ImageView BackButtonScanData;
    TextView PlayerFirstName;
    TextView PlayerLastName;
    EditText AttendDataPick;
    DatePickerDialog picker;
    Button AttendButton, FeesButton;
    Spinner Attendspinner;
    String Currentdateformatted;
    String AttendIdPass;
    Button AttendPlayerButton;
    String userid, PlayerId;
    EditText Temperature;
    EditText payamount;
    String type;
    CustomAdapterplayertype Fixedamount;
    LinearLayout AttendDesign, FeesDesign;
    ListView balancelist;
    LinearLayout inputbox;
    LinearLayout getfees;
    LinearLayout   MonthlyAmountAndYearlyAmountTitleScan;
    CustomAdapterplayer customAdapter1;
    Button plyamount;
    String balance;
    LinearLayout FeesTitleScan;
    TextView PlayerBatch,PlayerBranch,EnterYourPaidAmount;
    TextView NoRecordFoundScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_get_data_qr_scan);
        userid = objApp.getUserId();
        intent = getIntent();
        qrcode = intent.getStringExtra("PlayerCode");
        Log.d("qrcode",qrcode);
        PlayerDataView(qrcode);
        FeesTitleScan = findViewById(R.id.FeesTitleScan);
        FeesDesign = findViewById(R.id.FeesDesign);
        AttendDesign =  findViewById(R.id.AttendDesign);
        AttendButton = (Button) findViewById(R.id.AttendButton);
        plyamount = (Button) findViewById(R.id.plyamount);
        PlayerFirstName = (TextView) findViewById(R.id.PlayerFirstName);
        PlayerLastName = (TextView) findViewById(R.id.PlayerLastName);
        FeesButton = (Button) findViewById(R.id.FeesButton);
        Temperature = (EditText) findViewById(R.id.Temperature);
        payamount = (EditText) findViewById(R.id.payamount);
        PlayerBatch =(TextView) findViewById(R.id.PlayerBatch);
        PlayerBranch =(TextView) findViewById(R.id.PlayerBranch);
        NoRecordFoundScan =(TextView) findViewById(R.id.NoRecordFoundScan);
        EnterYourPaidAmount =(TextView) findViewById(R.id.EntreYourPaidAmount);
        balancelist = (ListView) findViewById(R.id.balancelist);
        MonthlyAmountAndYearlyAmountTitleScan =findViewById(R.id.MonthlyAmountAndYearlyAmountTitleScan);
        getfees = findViewById(R.id.getfees);
        inputbox = findViewById(R.id.inputbox);
        AttendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                AttendDesign.setVisibility(View.VISIBLE);
                FeesDesign.setVisibility(View.GONE);
            }
        });
        FeesButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                FeesDesign.setVisibility(View.VISIBLE);
                AttendDesign.setVisibility(View.GONE);
            }
        });
        BackButtonScanData = (ImageView) findViewById(R.id.BackButtonScanData);
        BackButtonScanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDataQrScanActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AttendDataPick = (EditText) findViewById(R.id.AttendDataPick);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        AttendDataPick.setText(DateFormat.format("yyyy-MM-dd", cldr.getTime()));
        AttendDataPick.setInputType(InputType.TYPE_NULL);

        Currentdateformatted = (String) DateFormat.format("yyyy-MM-dd", cldr.getTime());
        plyamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feesArray = objApp.getArrayList();
                String  Paymentpattern = "^[1-9][0-9]*$";
                if(type.equals("1")){
                        if(payamount.getText().toString().trim().isEmpty()){
                            payamount.setError("This field is required.");
                            payamount.requestFocus();
                        }else if(parseInt(payamount.getText().toString().trim()) > parseInt(balance)){
                            Toast.makeText(getApplicationContext(), "Please Check Your amount", Toast.LENGTH_SHORT).show();
                        }else if(!(parseInt(payamount.getText().toString()) > 0)){
                            Toast.makeText(getApplicationContext(), "Payment must be atleast 1Rs", Toast.LENGTH_SHORT).show();
                        }else if(!payamount.getText().toString().trim().matches(Paymentpattern)){
                            payamount.setError("Invalid Amount");
                            payamount.requestFocus();
                        }else {

                            progressDialog = new ProgressDialog(GetDataQrScanActivity.this);
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            //feesArray = objApp.getArrayList();
                            CollectFees(userid, Currentdateformatted, PlayerId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                            objApp.removeAll();
                        }



                }else{

                     if(String.valueOf(feesArray.size()).equals("0")){
                         Toast.makeText(GetDataQrScanActivity.this,"Please Click Your Fees CheckBox", Toast.LENGTH_LONG).show();
                     }else {
                         progressDialog = new ProgressDialog(GetDataQrScanActivity.this);
                         progressDialog.show();
                         progressDialog.setCancelable(false);
                         progressDialog.setContentView(R.layout.progress_dialog);
                         progressDialog.getWindow().setBackgroundDrawableResource(
                                 android.R.color.transparent
                         );
                         //feesArray = objApp.getArrayList();
                         CollectFees(userid, Currentdateformatted, PlayerId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                         objApp.removeAll();
                     }
                }


            }

        });


        AttendDataPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new DatePickerDialog(GetDataQrScanActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                               // AttendDataPick.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }
                                AttendDataPick.setText(year + "-" + (monthString) + "-" + dayString);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        Attendspinner = (Spinner) findViewById(R.id.Attendspinner);
        ArrayList<AdapterListData> AttendList = new ArrayList<>();
        AttendList.add(new AdapterListData("", "Select Attendance"));
        AttendList.add(new AdapterListData("1", "Present"));
        AttendList.add(new AdapterListData("2", "Absent"));
        ArrayAdapter<AdapterListData> spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(GetDataQrScanActivity.this, android.R.layout.simple_spinner_dropdown_item, AttendList);
        Attendspinner.setAdapter(spinnerAdapterBatch);
        Attendspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData) parent.getItemAtPosition(position);
                AttendIdPass = idpass.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AttendPlayerButton = (Button) findViewById(R.id.AttendPlayerButton);
        AttendPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AttendIdPass.isEmpty()){
                    TextView errorText = (TextView)Attendspinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Attendance");
                    Attendspinner.requestFocus();
                }else {
                    progressDialog = new ProgressDialog(GetDataQrScanActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    AddAttendance();
                }
            }
        });

    }

    private void AddAttendance() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+"addAttendance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (success.equals("1")) {
                                AttendDesign.setVisibility(View.GONE);
                                finish();
                                startActivity(getIntent());
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                params.put("player_id", PlayerId);
                params.put("attendence_date", AttendDataPick.getText().toString());
                params.put("temperature", Temperature.getText().toString());
                params.put("attendence", AttendIdPass);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void PlayerDataView(String qrcode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "getPlayerByBarcode",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                String playfirstname = data.getString("first_name");
                                PlayerFirstName.setText(playfirstname);
                                String PlayLastName = data.getString("last_name");
                                PlayerLastName.setText(PlayLastName);
                                PlayerId = data.getString("player_id");
                                String BatchName = data.getString("name");
                                PlayerBatch.setText(BatchName);
                                String BranchName = data.getString("branch_name");
                                PlayerBranch.setText(BranchName);
                                BalanceAmount(PlayerId);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qr_code", qrcode);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }

    private void BalanceAmount(String playerId) {
        ArrayList<FeesBalanceList> list_Fees = new ArrayList<>();
        ArrayList<FeesBalanceListtype> list_Feestype = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "getFessData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            type = jsonobject.getString("type");
                            balance = jsonobject.getString("balance");
                            payamount.setText(balance);
                            JSONArray jsonarray = jsonobject.getJSONArray("fees_list");


                            if (type.equals("1")) {

                                if(balance.equals("0")){
                                    NoRecordFoundScan.setVisibility(View.VISIBLE);
                                    getfees.setVisibility(View.GONE);
                                    plyamount.setVisibility(View.GONE);
                                }else{
                                    NoRecordFoundScan.setVisibility(View.GONE);
                                    FeesTitleScan.setVisibility(View.VISIBLE);
                                    Fixedamount = new CustomAdapterplayertype(getApplicationContext(), R.layout.balanceamount, list_Feestype);
                                    balancelist.setAdapter(Fixedamount);
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        getfees.setVisibility(View.VISIBLE);
                                        getfees.setEnabled(true);
                                        inputbox.setVisibility(View.VISIBLE);
                                        inputbox.setEnabled(true);
                                        EnterYourPaidAmount.setText(balance);
                                        JSONObject fees_list = jsonarray.getJSONObject(i);
                                        FeesBalanceListtype Feeslist = new FeesBalanceListtype(fees_list.getString("payment_date"),
                                                fees_list.getString("fees_date"),
                                                fees_list.getString("credit"), fees_list.getString("debit"));

                                        list_Feestype.add(Feeslist);
                                    }
                                    Collections.reverse(list_Feestype);//Reverse listview as message display
                                    Fixedamount.notifyDataSetChanged();
                                }

                            } else {
                                   if(jsonarray.length() == 0){
                                       NoRecordFoundScan.setVisibility(View.VISIBLE);
                                       plyamount.setVisibility(View.GONE);
                                       MonthlyAmountAndYearlyAmountTitleScan.setVisibility(View.GONE);
                                       getfees.setVisibility(View.GONE);
                                       getfees.setEnabled(false);
                                   }else{
                                       MonthlyAmountAndYearlyAmountTitleScan.setVisibility(View.VISIBLE);
                                       NoRecordFoundScan.setVisibility(View.GONE);
                                       inputbox.setVisibility(View.GONE);
                                       inputbox.setEnabled(false);
                                       getfees.setVisibility(View.VISIBLE);
                                       getfees.setEnabled(true);
                                       customAdapter1 = new CustomAdapterplayer(getApplicationContext(), R.layout.balanceamount, list_Fees);
                                       balancelist.setAdapter(customAdapter1);
                                       for (int i = 0; i < jsonarray.length(); i++) {
                                           JSONObject fees_list = jsonarray.getJSONObject(i);
                                           FeesBalanceList Feeslist = new FeesBalanceList(fees_list.getString("fees_id"),
                                                   fees_list.getString("fees_date"),
                                                   fees_list.getString("credit"), jsonobject.getString("type"));

                                           list_Fees.add(Feeslist);
                                       }
                                       customAdapter1.notifyDataSetChanged();
                                   }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                params.put("player", PlayerId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);
    }

    private void CollectFees(String userid, String currentdateformatted, String playid, String payamount, String feesArray) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "collectFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("return",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (success.equals("1")) {
                                 FeesDesign.setVisibility(View.GONE);
                                 finish();
                                 startActivity(getIntent());
                                 Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                params.put("payment_date", currentdateformatted);
                params.put("amount", payamount);
                params.put("player_id", playid);
                params.put("fees_id", feesArray);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}