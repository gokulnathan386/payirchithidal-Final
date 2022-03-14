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

public class EditBatchActivity extends AppCompatActivity {
    String amandpm,bATCHsPORTiD;
    Spinner starthrs;
    String hrs;
    ArrayList<AdapterListData> branchlist;
    Spinner startmin;
    String Startmin,BatchStartTime;
    Spinner sports;
    String endhr,bATCHtIMEpERIOD;
    Spinner endhrs;
    Spinner endmint;
    String endmi;
    String Batchid,BatchName,BatchEnfTime;
    Spinner ampm;
    Spinner batchtype;
    String amountid;
    Spinner amounttye;
    ArrayList<AdapterListData> Sports_list;
    ArrayAdapter<AdapterListData> spinnerAdapter;
    String SportsId;
    String branchid;
    Spinner branch;
    String BranchId;
    String userid,bATCHtYPE,bATCHRup,bRANCHiD;
    Button savebtn;
    Intent intent;
    Spinner endhour;
    Spinner endmin;
    Spinner PMAM;
    Spinner amtype;
    EditText btachname;
    Spinner strthrs;
    Spinner strtmin;
    Spinner batpe;
    TextView rup;
    Spinner branchValue;
    Spinner sportsValue;
    ProgressDialog progressDialog;
    String id,bATCHaMOUNT;

    private  String URL ="";
    private AppStorage objApp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Batch Update Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_edit_batch);
        intent = getIntent();
        id = intent.getStringExtra("Batch_Id");
        progressDialog = new ProgressDialog(EditBatchActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        userid = objApp.getUserId();
        branch = (Spinner) findViewById(R.id.branch);
        sports = (Spinner) findViewById(R.id.sports);
        btachname =(EditText) findViewById(R.id.name);
        strthrs =(Spinner) findViewById(R.id.starthrs);
        strtmin =(Spinner) findViewById(R.id.startmin);
        endhour =(Spinner) findViewById(R.id.endhrs);
        endmin =(Spinner) findViewById(R.id.endmint);
        PMAM =(Spinner) findViewById(R.id.ampm);
        batpe =(Spinner) findViewById(R.id.batchtype);
        rup =(TextView) findViewById(R.id.amount);
        amtype =(Spinner) findViewById(R.id.amtype);
        branchValue =(Spinner) findViewById(R.id.branch);
        sportsValue =(Spinner) findViewById(R.id.sports);
        savebtn = (Button) findViewById(R.id.savebtn);
        Button cancelbtnbatch = (Button) findViewById(R.id.cancelbtnbatch);
        cancelbtnbatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        starthrs =(Spinner) findViewById(R.id.starthrs);
        ArrayList<String> hours = new ArrayList<String>();
        hours.add("hrs");
        for(int i=0;i<=12;i++){
            if(i<=9){
                hours.add("0"+i);
            }else{
                hours.add(String.valueOf(i));
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item, hours);
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

        startmin = (Spinner) findViewById(R.id.startmin);
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


        endhrs = (Spinner) findViewById(R.id.endhrs);
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
        endmint = (Spinner) findViewById(R.id.endmint);
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

        ampm =(Spinner) findViewById(R.id.ampm);
        ArrayList<String> AMPM = new ArrayList<String>();
        AMPM.add("AM");
        AMPM.add("PM");
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

        batchtype =(Spinner) findViewById(R.id.batchtype);
        ArrayList<AdapterListData> BatchList = new ArrayList<>();
        BatchList.add(new AdapterListData("", "Select Batch Type"));
        BatchList.add(new AdapterListData("1", "Everyday"));
        BatchList.add(new AdapterListData("2", "Weekend"));
        ArrayAdapter<AdapterListData> spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(EditBatchActivity.this, R.layout.my_selected_item,BatchList);
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

        amounttye = (Spinner) findViewById(R.id.amtype);

        ArrayList<AdapterListData> AmountList = new ArrayList<>();
        AmountList.add(new AdapterListData("", "Select Amount Type"));
        AmountList.add(new AdapterListData("1", "Fixed Amount"));
        AmountList.add(new AdapterListData("2", "Monthly Amount"));
        AmountList.add(new AdapterListData("3", "Yearly Amount"));

        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBatchActivity.this,R.layout.my_selected_item,AmountList);
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



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btachname.getText().toString().trim().isEmpty()){
                    btachname.setError("This field is required.");
                    btachname.requestFocus();
                }
                else if(hrs.equals("hrs")){
                    TextView errorText = (TextView)starthrs.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("hrs");
                    starthrs.requestFocus();
                }
                else if(Startmin.equals("Min")){
                    TextView errorText = (TextView)startmin.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Min");
                    startmin.requestFocus();
                }
                else if(endhr.equals("hrs")){
                    TextView errorText = (TextView)endhrs.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("hrs");
                    endhrs.requestFocus();
                }
                else if(endmi.equals("Min")){
                    TextView errorText = (TextView)endmint.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Min");
                    endmint.requestFocus();
                }
                else if(Batchid.isEmpty()){
                    TextView errorText = (TextView)batchtype.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Batch Type");
                    batchtype.requestFocus();
                }
                else if(rup.getText().toString().trim().isEmpty()){
                    rup.setError("This field is required.");
                    rup.requestFocus();
                }else if(amountid.isEmpty()){
                    TextView errorText = (TextView)amounttye.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Amount Type");
                    amounttye.requestFocus();
                }
                else if(branchid.isEmpty()){
                    TextView errorText = (TextView)branch.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                    branch.requestFocus();
                }else if(SportsId.isEmpty()){
                    TextView errorText = (TextView)sports.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Sports");
                    sports.requestFocus();
                }
                else{
                    progressDialog = new ProgressDialog(EditBatchActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    uPATEbATCH(id,btachname.getText().toString().trim(),hrs,Startmin,endhr,endmi,amandpm,Batchid,rup.getText().toString().trim(),amountid,branchid,SportsId);
                }
            }
        });
        new BackgroundTask(EditBatchActivity.this) {
            @Override
            public void doInBackground() {
                ViewBatch(id);
            }

            @Override
            public void onPostExecute() {

            }
        }.execute();

    }

    private void uPATEbATCH(String id, String btachname, String hrs, String startmin, String endhr, String endmi, String amandpm, String batchid, String rup, String amountid, String branchid, String sportsId) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"updateBatch",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("status");
                                String message = jsonObject.getString("msg");
                                if(success.equals("1")){
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditBatchActivity.this,BatchlistActivity.class);
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

                    params.put("batch_id",id);
                    params.put("batch_name",btachname);
                    params.put("start_time_hrs",hrs);
                    params.put("start_time_min",startmin);
                    params.put("end_time_hrs",endhr);
                    params.put("end_time_min",endmi);
                    params.put("time_period",amandpm);
                    params.put("batch_type",batchid);
                    params.put("amt",rup);
                    params.put("amt_type",amountid);
                    params.put("branch_id",branchid);
                    params.put("sports_id",sportsId);
                    return params;

                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    private void ViewBatch(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBatch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                Log.d("data", String.valueOf(data));
                                BatchName = data.getString("name");
                                btachname.setText(BatchName);
                                BatchStartTime = data.getString("start_time");
                                String [] strathrs =  BatchStartTime.split(":");
                                String starthr = strathrs[0];
                                strthrs.setSelection(Integer.parseInt(starthr)+1);
                                String startminb = strathrs[1];
                                strtmin.setSelection(Integer.parseInt(startminb)+1);
                                BatchEnfTime = data.getString("end_time");
                                String [] batchendhrs =  BatchEnfTime.split(":");
                                String  bATCHeND= batchendhrs[0];
                                endhour.setSelection(Integer.parseInt(bATCHeND)+1);
                                String bATCHmin = batchendhrs[1];
                                endmin.setSelection(Integer.parseInt(bATCHmin)+1);

                                bATCHtIMEpERIOD = data.getString("time_period");
                                if(bATCHtIMEpERIOD.equals("AM")){
                                    ampm.setSelection(0);
                                } else {
                                    ampm.setSelection(1);
                                }

                                bATCHtYPE = data.getString("batch_type");
                                batpe.setSelection(Integer.parseInt(bATCHtYPE));
                                bATCHRup = data.getString("amt");
                                rup.setText(bATCHRup);
                                bATCHaMOUNT = data.getString("amt_type");
                                amtype.setSelection(Integer.parseInt(bATCHaMOUNT));
                                BranchId = data.getString("branch_id");
                                bATCHsPORTiD = data.getString("sports_id");
                                Branchdropdownlist(userid);
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
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);

    }

    public int getDropDownIndex(ArrayList objList,String id)
    {
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
    private void Branchdropdownlist(String userid) {

        branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select Branch "));
        spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBatchActivity.this, R.layout.my_selected_item,branchlist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        branch.setAdapter(spinnerAdapter);
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            int positionBranch = getDropDownIndex(branchlist,BranchId);
                            branch.setSelection(positionBranch);
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

        Sports_list = new ArrayList<AdapterListData>();
        Sports_list.add(new AdapterListData("","Select Sports"));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBatchActivity.this,R.layout.my_selected_item,Sports_list);
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
                            int positionSports = getDropDownIndex(Sports_list,bATCHsPORTiD);
                            sports.setSelection(positionSports);
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