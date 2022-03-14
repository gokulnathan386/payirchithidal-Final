package com.payirchithidal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.CustomerAdapter.CustomAdapternotify;
import com.payirchithidal.Model.NotifyList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationListActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ListView notifylist;
    ArrayList<NotifyList> list_notify;
    CustomAdapternotify customAdapter;
    String userid;
    TextView RecordNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_notification_list);

        progressDialog = new ProgressDialog(NotificationListActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        userid = objApp.getUserId();
        Notifylist(userid);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        ImageView backbtnn =(ImageView) findViewById(R.id.backbtnn);
        backbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView addbtnn =(TextView)findViewById(R.id.addbtnn);
        addbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationListActivity.this, AddNotificationListActivity.class);
                startActivity(intent);
            }
        });

        search_edt =(SearchView)findViewById(R.id.search_edt);
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
                ArrayList<NotifyList> filtered = new ArrayList<NotifyList>();
                for (int i = 0; i < list_notify.size() ; i++){
                    String Name = String.valueOf(list_notify.get(i).getfirstname());
                    if (Name.contains(newText)){
                        NotifyList contents = new NotifyList(list_notify.get(i).getprofile(),
                                list_notify.get(i).getfirstname(),list_notify.get(i).getbranchname());
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapternotify(getApplicationContext(),R.layout.notificationlist_design,filtered);
                    notifylist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Notifylist(String userid) {
        notifylist = (ListView)findViewById(R.id.notifylist);
        list_notify = new ArrayList<>();
        customAdapter = new CustomAdapternotify(getApplicationContext(),R.layout.notificationlist_design,list_notify);
        notifylist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getNotifyList",
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
                                    NotifyList sportsList = new NotifyList(object.getString("profile"),
                                            object.getString("first_name"),
                                            object.getString("branch_name"));
                                    list_notify.add(sportsList);

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
        Intent intent = new Intent(NotificationListActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}