package com.payirchithidal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.payirchithidal.CustomerAdapter.CustomAdapterPayout;
import com.payirchithidal.Model.PayoutList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayoutListActivity extends AppCompatActivity {


    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ListView payoutlist;
    ArrayList<PayoutList> list_payout;
    CustomAdapterPayout customAdapter;
    TextView addbtnpayout;
    TextView RecordNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_payout_list);
        progressDialog = new ProgressDialog(PayoutListActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String userid = objApp.getUserId();
        payoutlist(userid);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        addbtnpayout =(TextView) findViewById(R.id.addbtnpayout);
        addbtnpayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(PayoutListActivity.this,PaymentRequest.class);
             startActivity(intent);
            }
        });
        ImageView backbtnn =(ImageView) findViewById(R.id.backbtnpayout);
        backbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                ArrayList<PayoutList> filtered = new ArrayList<PayoutList>();
                for (int i = 0; i < list_payout.size() ; i++){
                    String Name = String.valueOf(list_payout.get(i).getclubname());
                    if (Name.contains(newText)){

                        PayoutList contents = new PayoutList(list_payout.get(i).getclubname(),
                                list_payout.get(i).getamount(),list_payout.get(i).getreason());
                        filtered.add(contents);
                    }

                    customAdapter = new CustomAdapterPayout(getApplicationContext(),R.layout.payout_list_design,filtered);
                    payoutlist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void payoutlist(String userid) {

        payoutlist = (ListView)findViewById(R.id.payoutlist);
        list_payout = new ArrayList<>();
        customAdapter = new CustomAdapterPayout(getApplicationContext(),R.layout.payout_list_design,list_payout);
        payoutlist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPayoutList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            Log.d("gokulnathan",jsonarray.toString());
                            progressDialog.dismiss();
                            if(0==jsonarray.length()){
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                PayoutList payoutList = new PayoutList(object.getString("club_name"),
                                        object.getString("amount"),
                                        object.getString("reason"));
                                list_payout.add(payoutList);
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
}