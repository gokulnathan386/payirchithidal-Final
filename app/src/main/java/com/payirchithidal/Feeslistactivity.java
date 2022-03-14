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
import com.payirchithidal.CustomerAdapter.CustomAdapterFees;
import com.payirchithidal.Model.FeesList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Feeslistactivity extends AppCompatActivity {
    ListView listfees;

    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView  search_edt;
    ArrayList<FeesList> list_fees;
    CustomAdapterFees customAdapter;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_fees_list);

        progressDialog = new ProgressDialog(Feeslistactivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        String userid = objApp.getUserId();
        Feeslist(userid);
        RecordNotFound=(TextView) findViewById(R.id.RecordNotFound);
        ImageView backbtnfees=(ImageView) findViewById(R.id.backbtnfees);
        backbtnfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feeslistactivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView addbtnfees=(TextView) findViewById(R.id.addbtnfees);
        addbtnfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feeslistactivity.this, AddFeesActivity.class);
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
                ArrayList<FeesList> filtered = new ArrayList<FeesList>();
                for (int i = 0; i < list_fees.size() ; i++){
                    String Name = String.valueOf(list_fees.get(i).getplayername());
                    if (Name.contains(newText)){
                        FeesList contents = new FeesList(
                                list_fees.get(i).getplayername(),
                                list_fees.get(i).getbranch(),
                                list_fees.get(i).getCredit(),
                                list_fees.get(i).getDebit(),
                                list_fees.get(i).getPayment_date(),
                                list_fees.get(i).getFees_date(),
                                list_fees.get(i).getProfile(),
                                list_fees.get(i).getFeesId());
                        filtered.add(contents);
                    }

                    customAdapter = new CustomAdapterFees(getApplicationContext(),R.layout.feeslist,filtered);
                    listfees.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Feeslist(String userid) {
        listfees = (ListView)findViewById(R.id.listfees);
        list_fees = new ArrayList<>();
        customAdapter = new CustomAdapterFees(getApplicationContext(),R.layout.feeslist,list_fees);

        listfees.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getFeesList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            progressDialog.dismiss();
                            if(0==jsonarray.length()){
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    FeesList sportsList = new FeesList(
                                            object.getString("player_name"),
                                            object.getString("branch"),
                                            object.getString("credit"),
                                            object.getString("debit"),
                                            object.getString("payment_date"),
                                            object.getString("fees_date"),
                                            object.getString("profile"),
                                            object.getString("fees_id")
                                    );
                                    list_fees.add(sportsList);
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
        Intent intent = new Intent(Feeslistactivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}