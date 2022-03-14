package com.payirchithidal;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddFeesActivity extends AppCompatActivity {
    EditText feesamount;
    DatePickerDialog picker;
    Button savebtn;
    ProgressDialog progressDialog;
    AutoCompleteTextView playername;
    ArrayList<AdapterListData> Play_list;
    ArrayAdapter<AdapterListData> spinnerAdapter;
    String PlayId=null;
    String  userid;
    private  String URL ="";
    private AppStorage objApp = null;
    String Amount;
    String Date;
    EditText feesdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Fees Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_add_fees);
        userid = objApp.getUserId();
        playername =(AutoCompleteTextView)findViewById(R.id.playername);
        feesamount=(EditText)findViewById(R.id.feesamount);
        TextView cancelbtn=(TextView) findViewById(R.id. cancelbtnfees);
        savebtn =(Button)findViewById(R.id.savebtn);
        feesdate = (EditText)findViewById(R.id.feesdate);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        feesdate.setText(DateFormat.format("yyyy-MM-dd", cldr.getTime()));
        feesdate.setInputType(InputType.TYPE_NULL);
        feesdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new DatePickerDialog(AddFeesActivity.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //feesdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }
                                feesdate.setText(year + "-" + (monthString) + "-" + dayString);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Play_list = new ArrayList<AdapterListData>();
        spinnerAdapter = new ArrayAdapter<AdapterListData>(AddFeesActivity.this, android.R.layout.select_dialog_item,Play_list);
        playername.setThreshold(1);
        playername.setAdapter(spinnerAdapter);
        playername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                PlayId = idpass.id;
                savebtn.setVisibility(View.VISIBLE);
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPlayerList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("player_id"),
                                        object.getString("player_name"));
                                Play_list.add(dropdown);
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
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount =feesamount.getText().toString().trim();
                Date =feesdate.getText().toString().trim();
                String  Paymentpattern = "^[1-9][0-9]*$";  // Start Zero Not Allow
                if(PlayId==null){
                    playername.setError("Please Select Your Valid User");
                    playername.requestFocus();
                }else if(Amount.isEmpty()){
                    feesamount.setError("This field is required.");
                    feesamount.requestFocus();
                }else if(parseInt(feesamount.getText().toString().trim()) == 0){
                    feesamount.setError("Enter number greater than 0!");
                    feesamount.requestFocus();
                }else if(!feesamount.getText().toString().trim().matches(Paymentpattern)){
                    feesamount.setError("Invalid Amount");
                    feesamount.requestFocus();
                }else{
                   progressDialog = new ProgressDialog(AddFeesActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );

                    AddFees(userid,Date,PlayId,Amount);


                }

            }
        });

        playername.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                PlayId=null;

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
    }
    private void AddFees(String userid,String Date,String playId,String amount) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddFeesActivity.this,Feeslistactivity.class);
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
                params.put("user_id",userid);
                params.put("payment_date",Date);
                params.put("player_id",playId);
                params.put("amount",amount);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}