package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAttendanceActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    EditText datapick;
    ProgressDialog progressDialog;
    DatePickerDialog picker;
    Button cancelbtnuser;
    Spinner Attendspinner;
    String AttendIdPass;
    String userid;
    ArrayList<AdapterListData> Play_list;
    Button savebtn;
    AutoCompleteTextView Plyname;
    ArrayAdapter<AdapterListData> spinnerAdapter;
    EditText editdatapick;
    EditText temperature;
    String PlayName;
    String DataPick;
    String Tmp;
    String AttendId;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    String PlayId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Attendance Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_attendance_add);
        userid = objApp.getUserId();

        datapick =(EditText)findViewById(R.id.datapick);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        datapick.setText(DateFormat.format("yyyy-MM-dd", cldr.getTime()));
        datapick.setInputType(InputType.TYPE_NULL);

        datapick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new DatePickerDialog(AddAttendanceActivity.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //datapick.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }
                                datapick.setText(year + "-" + (monthString) + "-" + dayString);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Attendspinner =(Spinner)findViewById(R.id.Attendspinner);
        ArrayList<AdapterListData> AttendList = new ArrayList<>();
        AttendList.add(new AdapterListData("", "Select Attendance"));
        AttendList.add(new AdapterListData("1", "Present"));
        AttendList.add(new AdapterListData("2", "Absent"));



        ArrayAdapter<AdapterListData> spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(AddAttendanceActivity.this,R.layout.my_selected_item,AttendList);
        spinnerAdapterBatch.setDropDownViewResource(R.layout.my_dropdown_item);
        Attendspinner.setAdapter(spinnerAdapterBatch);
        Attendspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                AttendIdPass =idpass.id;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                Plyname =(AutoCompleteTextView) findViewById(R.id.Plyname);
                Play_list = new ArrayList<AdapterListData>();
                spinnerAdapter = new ArrayAdapter<AdapterListData>(AddAttendanceActivity.this, android.R.layout.select_dialog_item,Play_list);
                Plyname.setThreshold(1);
                Plyname.setAdapter(spinnerAdapter);
                Plyname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                        AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                        PlayId =idpass.id;
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



        savebtn = (Button)findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editdatapick  =(EditText) findViewById(R.id.datapick);
                temperature =(EditText) findViewById(R.id.Temperature);

                PlayName =PlayId;
                DataPick =editdatapick.getText().toString().trim();
                Tmp =temperature.getText().toString().trim();
                AttendId = String.valueOf(AttendIdPass);

            if(PlayId == null){
                    Plyname.setError("Please Select Your Valid User");
                    Plyname.requestFocus();
                }else if(DataPick.isEmpty()){
                    editdatapick.setError("This field is required.");
                    editdatapick.requestFocus();
                } else if(AttendId.isEmpty()){
                    TextView errorText = (TextView)Attendspinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Attendance");
                    Attendspinner.requestFocus();
                } else{
                progressDialog = new ProgressDialog(AddAttendanceActivity.this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                    AddAttendanceForm(userid,PlayName,DataPick,Tmp,AttendId);
                }
            }
        });

        cancelbtnuser =(Button) findViewById(R.id.cancelbtnuser);
        cancelbtnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(AddAttendanceActivity.this,AttendanceListActivity.class);
                startActivity(intent);
                finish();
            }
        });



        Plyname.addTextChangedListener(new TextWatcher() {
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

    private void AddAttendanceForm(String userid, String playName, String dataPick, String tmp, String AttendId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addAttendance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddAttendanceActivity.this,AttendanceListActivity.class);
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
                params.put("player_id",playName);
                params.put("attendence_date",dataPick);
                params.put("temperature",tmp);
                params.put("attendence",AttendId);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}