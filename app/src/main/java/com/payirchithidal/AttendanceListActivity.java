package com.payirchithidal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.CustomerAdapter.CustomAdapterattendance;
import com.payirchithidal.Model.AttendeanceList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceListActivity extends AppCompatActivity {
    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView  search_edt;
    ListView attendancelist;
    ArrayList<AttendeanceList> list_attendance;
    CustomAdapterattendance customAdapter;
    String userid;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_attendance_list);

        progressDialog = new ProgressDialog(AttendanceListActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        userid = objApp.getUserId();
        Attendancelist(userid);
        RecordNotFound =(TextView)findViewById(R.id.RecordNotFound);
        ImageView backbtnattendance = (ImageView) findViewById(R.id.backbtnattendance);
        backbtnattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(AttendanceListActivity.this,HomeActivity.class);
               startActivity(intent);
               finish();
            }
        });
        TextView addbtnattendance=(TextView)findViewById(R.id.addbtnattendance);
        addbtnattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AttendanceListActivity.this, AddAttendanceActivity.class);
                startActivity(intent);
            }
        });
        search_edt =(SearchView) findViewById(R.id.search_edt);
        search_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edt.onActionViewExpanded();
            }
        });
        search_edt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<AttendeanceList> filtered = new ArrayList<AttendeanceList>();
                for (int i = 0; i < list_attendance.size() ; i++){
                    String Name = String.valueOf(list_attendance.get(i).getfirstname());
                    if (Name.contains(newText)){
                        AttendeanceList contents = new AttendeanceList(list_attendance.get(i).getprofile(),
                                list_attendance.get(i).getfirstname(),
                                list_attendance.get(i).getbranchname()
                        );
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapterattendance(getApplicationContext(),R.layout.attendancelist_design,filtered);
                    attendancelist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Attendancelist(String userid) {
        attendancelist = (ListView)findViewById(R.id.attendancelist);
        list_attendance = new ArrayList<>();
        customAdapter = new CustomAdapterattendance(getApplicationContext(),R.layout.attendancelist_design,list_attendance);
        attendancelist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getAttendenceList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            progressDialog.dismiss();
                            if (0 == jsonarray.length()) {
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    AttendeanceList attendanceList = new AttendeanceList(object.getString("profile"),
                                            object.getString("first_name"),
                                            object.getString("branch_name"));
                                    list_attendance.add(attendanceList);
                                }
                            }
                            customAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
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
    public void onBackPressed(){
        Intent intent = new Intent(AttendanceListActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}