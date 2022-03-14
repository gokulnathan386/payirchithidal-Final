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
import android.widget.AutoCompleteTextView;
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


public class AddBatchActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    Spinner branch_dropdown;
    Spinner  starthrs;
    Button savebtn;
    Spinner startmin;
    Spinner endhrs;
    Spinner endmint;
    Spinner sports;
    Spinner amounttye;
    Spinner batchtype;
    Spinner ampm;
    EditText name;
    EditText amount;
    String SportsId;
    String nameBatch;
    String  branchid;
    String userid;
    String hrs;
    String strathrs;
    String Startmin;
    String Startm;
    String endhr;
    String endhou;
    String weekenddayId;
    String endmi;
    String endm;
    String amandpm;
    String pmam;
    String Amt;
    String typeamount;
    String Sportidpass;
    String branchidpass;
    String amountid;
    String Batchid;
    ProgressDialog progressDialog;
    AutoCompleteTextView sports1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Batch Form");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_batch_add);


        branch_dropdown = (Spinner) findViewById(R.id.branch);
        userid = objApp.getUserId();
        Branchdropdownlist(userid);

        starthrs =(Spinner) findViewById(R.id.starthrs);
        startmin =(Spinner) findViewById(R.id.startmin);
        endhrs =(Spinner) findViewById(R.id.endhrs);
        endmint =(Spinner) findViewById(R.id.endmint);
        batchtype =(Spinner) findViewById(R.id.batchtype);
        amounttye =(Spinner) findViewById(R.id.amtype);
        sports=(Spinner)findViewById(R.id.sports);

        savebtn =(Button)findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name =(EditText) findViewById(R.id.name);
                amount  =(EditText) findViewById(R.id.amount);

                nameBatch =name.getText().toString().trim();
                Amt =amount.getText().toString().trim();
                strathrs= String.valueOf(hrs);
                Startm= String.valueOf(Startmin);
                endhou= String.valueOf(endhr);
                endm= String.valueOf(endmi);
                pmam= String.valueOf(amandpm);
                weekenddayId= String.valueOf(Batchid);
                typeamount = String.valueOf(amountid);
                branchidpass = String.valueOf(branchid);
                Sportidpass = String.valueOf(SportsId);

                if(nameBatch.isEmpty()){
                    name.setError("This field is required.");
                    name.requestFocus();
                }
                else if(strathrs.equals("hrs")){
                    TextView errorText = (TextView)starthrs.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("hrs");
                    starthrs.requestFocus();
                }
                else if(Startm.equals("Min")){
                    TextView errorText = (TextView)startmin.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Min");
                    startmin.requestFocus();
                }
                else if(endhou.equals("hrs")){
                    TextView errorText = (TextView)endhrs.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("hrs");
                    endhrs.requestFocus();
                }
                else if(endm.equals("Min")){
                    TextView errorText = (TextView)endmint.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Min");
                    endmint.requestFocus();
                   }
                else if(weekenddayId.isEmpty()){
                    TextView errorText = (TextView)batchtype.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Batch Type");
                    batchtype.requestFocus();
                }
                else if(Amt.isEmpty()){
                    amount.setError("This field is required.");
                    amount.requestFocus();
                }else if(typeamount.isEmpty()){
                    TextView errorText = (TextView)amounttye.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Amount Type");
                    amounttye.requestFocus();
                }  else if(branchid.isEmpty()){
                    TextView errorText = (TextView)branch_dropdown.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                    branch_dropdown.requestFocus();
                }else if(Sportidpass.isEmpty()){
                    TextView errorText = (TextView)sports.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Sports");
                    sports.requestFocus();
                }
                else{
                    progressDialog = new ProgressDialog(AddBatchActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    AddBatchForm(nameBatch,strathrs,Startm,endhou,endm,pmam,weekenddayId,Amt,typeamount,branchidpass,Sportidpass,userid);
                }
            }
        });

        ArrayList<String> hours = new ArrayList<String>();
        hours.add("hrs");
        for(int i=0;i<=12;i++){
            if(i<=9){
                hours.add("0"+i);
            }else{
                hours.add(String.valueOf(i));
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.my_selected_item, hours);
        dataAdapter.setDropDownViewResource(R.layout.my_dropdown_item);

        starthrs.setAdapter(dataAdapter);
        starthrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_position = String.valueOf(id);
                int positonInt = Integer.valueOf(item_position);
                hrs=hours.get(positonInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> min = new ArrayList<String>();
        min.add("Min");
        for(int i=0;i<=59;i++){
            if(i<=9){
                min.add("0"+i);
            }else{
                min.add(String.valueOf(i));
            }
        }


        ArrayAdapter<String> dataAdapterstartmin = new ArrayAdapter<String>(this,R.layout.my_selected_item, min);
        dataAdapterstartmin.setDropDownViewResource(R.layout.my_dropdown_item);
        startmin.setAdapter(dataAdapterstartmin);
        startmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_position = String.valueOf(id);
                int positonInt = Integer.valueOf(item_position);
                Startmin =min.get(positonInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> endhours = new ArrayList<String>();
        endhours.add("hrs");
        for(int i=0;i<=12;i++){
            if(i<=9){
                endhours.add("0"+i);
            }else{
                endhours.add(String.valueOf(i));
            }
        }

        ArrayAdapter<String> dataAdapterendhrs = new ArrayAdapter<String>(this,R.layout.my_selected_item,endhours);
        dataAdapterendhrs.setDropDownViewResource(R.layout.my_dropdown_item);
        endhrs.setAdapter(dataAdapterendhrs);
        endhrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_position = String.valueOf(id);
                int positonInt = Integer.valueOf(item_position);
                endhr = endhours.get(positonInt);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> endmin = new ArrayList<String>();
        endmin.add("Min");
        for(int i=0;i<=59;i++){
            if(i<=9){
                endmin.add("0"+i);
            }else{
                endmin.add(String.valueOf(i));
            }
        }

        ArrayAdapter<String> dataAdapterendmin = new ArrayAdapter<String>(this,R.layout.my_selected_item,endmin);
        dataAdapterendmin.setDropDownViewResource(R.layout.my_dropdown_item);
        endmint.setAdapter(dataAdapterendmin);
        endmint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item_position = String.valueOf(id);

                int positonInt = Integer.valueOf(item_position);
                 endmi= endmin.get(positonInt);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> AMPM = new ArrayList<String>();
        AMPM.add("AM");
        AMPM.add("PM");
        ampm =(Spinner) findViewById(R.id.ampm);
        ArrayAdapter<String> dataAdapterAMPM = new ArrayAdapter<String>(this,R.layout.my_selected_item,AMPM);
        dataAdapterAMPM.setDropDownViewResource(R.layout.my_dropdown_item);
        ampm.setAdapter(dataAdapterAMPM);
        ampm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item_position = String.valueOf(id);

                int positonInt = Integer.valueOf(item_position);
                amandpm= AMPM.get(positonInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<AdapterListData> BatchList = new ArrayList<>();
        BatchList.add(new AdapterListData("", "Select Batch Type"));
        BatchList.add(new AdapterListData("1", "Everyday"));
        BatchList.add(new AdapterListData("2", "Weekend"));


        ArrayAdapter<AdapterListData> spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(AddBatchActivity.this,R.layout.my_selected_item,BatchList);
        spinnerAdapterBatch.setDropDownViewResource(R.layout.my_dropdown_item);
        batchtype.setAdapter(spinnerAdapterBatch);

        batchtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                Batchid =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayList<AdapterListData> AmountList = new ArrayList<>();
        AmountList.add(new AdapterListData("", "Select Amount Type"));
        AmountList.add(new AdapterListData("1", "Fixed Amount"));
        AmountList.add(new AdapterListData("2", "Monthly Amount"));
        AmountList.add(new AdapterListData("3", "Yearly Amount"));

        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddBatchActivity.this,R.layout.my_selected_item,AmountList);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        amounttye.setAdapter(spinnerAdapter);
        amounttye.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                 amountid =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button cancelbtnbatch = (Button) findViewById(R.id.cancelbtnbatch);
            cancelbtnbatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddBatchActivity.this, BatchlistActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    private void AddBatchForm(String nameBatch, String strathrs, String startm, String endhou, String endm, String pmam, String weekenddayId, String amt, String typeamount, String branchidpass, String sportidpass, String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addBatch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddBatchActivity.this,BatchlistActivity.class);
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
                params.put("player_name",nameBatch);
                params.put("start_time_hrs",strathrs);
                params.put("start_time_min",startm);
                params.put("end_time_hrs",endhou);
                params.put("end_time_min",endm);
                params.put("time_period",pmam);
                params.put("batch_type",weekenddayId);
                params.put("amt",amt);
                params.put("amt_type",typeamount);
                params.put("branch_id",branchidpass);
                params.put("sports_id",sportidpass);
                params.put("user_id",userid);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Branchdropdownlist(String userid) {
        ArrayList<AdapterListData> branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select Branch "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddBatchActivity.this,R.layout.my_selected_item,branchlist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        branch_dropdown.setAdapter(spinnerAdapter);
        branch_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData id1 = (AdapterListData)parent.getItemAtPosition(position);
                branchid =id1.id;
                Sportsdropdown(branchid);
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

    private void Sportsdropdown(String branchid) {
        ArrayList<AdapterListData> Sports_list = new ArrayList<AdapterListData>();
        Sports_list.add(new AdapterListData("","Select Sports"));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddBatchActivity.this,R.layout.my_selected_item,Sports_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        sports.setAdapter(spinnerAdapter);
        sports.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                SportsId =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getSportsByBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)

                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("sports_id"),
                                        object.getString("sports_name"));
                                Sports_list.add(dropdown);
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("branch_id",branchid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
}
