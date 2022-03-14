package com.payirchithidal;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddNotificationListActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    String userid;
    AutoCompleteTextView playersname;
    EditText message;
    String Plynme;
    String msg;
    String  Frmd;
    String PlayId =null;
    ArrayList<AdapterListData> Play_list;
    ArrayAdapter<AdapterListData> spinnerAdapter;
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
        getSupportActionBar().setTitle("Remainder Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_add_notificationlist);
        userid = objApp.getUserId();
        Frmd ="Fees Reminder";
        playersname =(AutoCompleteTextView)findViewById(R.id.playersname);
        message =(EditText)findViewById(R.id.message);

        Play_list = new ArrayList<AdapterListData>();
        spinnerAdapter = new ArrayAdapter<AdapterListData>(AddNotificationListActivity.this, android.R.layout.select_dialog_item,Play_list);
        playersname.setThreshold(1);
        playersname.setAdapter(spinnerAdapter);
        playersname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        Button savebtn = (Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plynme =playersname.getText().toString().trim();
                msg =message.getText().toString().trim();
                if(PlayId == null){
                    playersname.setError("Please Select Your Valid User");
                    playersname.requestFocus();
                }else if(msg.isEmpty()){
                    message.setError("This field is required.");
                    message.requestFocus();
                }else{
                    progressDialog = new ProgressDialog(AddNotificationListActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    AddNotify(PlayId,msg,Frmd,userid);
                }
            }
        });
        Button cancelbtnnotify =(Button)findViewById(R.id.cancelbtnnotify);
        cancelbtnnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        playersname.addTextChangedListener(new TextWatcher() {
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

    private void AddNotify(String plynme, String msg, String frmd, String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"addNotification",
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
                params.put("player_id",plynme);
                params.put("message",msg);
                params.put("method",frmd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}