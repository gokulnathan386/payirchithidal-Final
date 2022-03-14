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
import com.payirchithidal.CustomerAdapter.CustomAdapterbatch;
import com.payirchithidal.Model.BatchList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BatchlistActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ListView  batchlist;
    ArrayList<BatchList> list_batch;
    CustomAdapterbatch customAdapter;
    String userid;
    TextView RecordNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_batchlist);

        progressDialog = new ProgressDialog(BatchlistActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        userid = objApp.getUserId();
        Batchlist(userid);
        RecordNotFound =(TextView)findViewById(R.id.RecordNotFound);
        ImageView backbtnbatch =(ImageView) findViewById(R.id.backbtnbatch);
        backbtnbatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BatchlistActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView addbtnbatch =(TextView) findViewById(R.id.addbtnbatch);
        addbtnbatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(BatchlistActivity.this, AddBatchActivity.class);
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

                ArrayList<BatchList> filtered = new ArrayList<BatchList>();
                for (int i = 0; i < list_batch.size() ; i++){
                    String Name = String.valueOf(list_batch.get(i).getbatch_name());
                    if (Name.contains(newText)){
                        BatchList contents = new BatchList(list_batch.get(i).getbatch_name(),
                                 list_batch.get(i).getbatch_type(),list_batch.get(i).getstart_time(),
                                 list_batch.get(i).getend_time(),list_batch.get(i).getBatchId());
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapterbatch(getApplicationContext(),R.layout.batch_listdesign,filtered);
                    batchlist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Batchlist(String userid) {
        batchlist = (ListView)findViewById(R.id.batchlist);
        list_batch = new ArrayList<>();
        customAdapter = new CustomAdapterbatch(getApplicationContext(),R.layout.batch_listdesign,list_batch);
        batchlist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBatchList",
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
                                    BatchList batchList = new BatchList(object.getString("batch_name"),
                                            object.getString("batch_type"),
                                            object.getString("start_time"),
                                            object.getString("end_time"),
                                            object.getString("batch_id")
                                    );
                                    list_batch.add(batchList);
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
    public void onBackPressed() {
        Intent  intentBackPress = new Intent(BatchlistActivity.this,HomeActivity.class);
        startActivity(intentBackPress);
        finish();
    }
}

