package com.payirchithidal;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
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
import com.payirchithidal.CustomerAdapter.CustomAdapterFeesPending;
import com.payirchithidal.Model.FeesPending;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingFeesList extends AppCompatActivity {
    ProgressDialog progressDialog;
    private  String URL ="";
    private AppStorage objApp = null;
    String userid;
    ListView FeesPendingList;
    ArrayList<FeesPending>  fees_pending_list;
    CustomAdapterFeesPending customAdapterFeesPending;
    SearchView search_edt;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pending Fees List");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_pending_fees_list);
        progressDialog = new ProgressDialog(PendingFeesList.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        userid = objApp.getUserId();
        FeesPendingList = (ListView)findViewById(R.id.FeesPendingList);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        search_edt = (SearchView)findViewById(R.id.search_edt);
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
                ArrayList<FeesPending> filtered = new ArrayList<FeesPending>();
                for (int i = 0; i < fees_pending_list.size() ; i++){
                    String Name = String.valueOf(fees_pending_list.get(i).get_FirstName());
                    if (Name.contains(newText)){
                        FeesPending contents = new FeesPending(fees_pending_list.get(i).get_PlayerId(),
                                fees_pending_list.get(i).get_Total(),
                                fees_pending_list.get(i).get_Paid(),
                                fees_pending_list.get(i).get_Balance(),
                                fees_pending_list.get(i).get_FirstName(),
                                fees_pending_list.get(i).get_Player()
                                );
                        filtered.add(contents);
                    }
                    customAdapterFeesPending = new CustomAdapterFeesPending(getApplicationContext(),R.layout.pendinglistdesign,filtered);
                    FeesPendingList.setAdapter(customAdapterFeesPending);
                }
                customAdapterFeesPending.notifyDataSetChanged();
                return false;
            }
        });
        PendingListFees(userid);
    }
    private void PendingListFees(String userid) {
        fees_pending_list = new ArrayList<>();
        customAdapterFeesPending = new CustomAdapterFeesPending(getApplicationContext(),R.layout.pendinglistdesign,fees_pending_list);
        FeesPendingList.setAdapter(customAdapterFeesPending);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPendingFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            progressDialog.dismiss();
                            if (0 == jsonarray.length()) {
                                RecordNotFound.setVisibility(View.VISIBLE);
                            } else{
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    FeesPending FeesList = new FeesPending(
                                            object.getString("player_id"),
                                            object.getString("total"),
                                            object.getString("paid"),
                                            object.getString("balance"),
                                            object.getString("first_name"),
                                            object.getString("player")
                                    );
                                    fees_pending_list.add(FeesList);
                                }
                        }
                            customAdapterFeesPending.notifyDataSetChanged();
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
}