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
import com.payirchithidal.CustomerAdapter.CustomAdapterbranch;
import com.payirchithidal.Model.BranchList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchlistActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    CustomAdapterbranch customAdapter;
    ArrayList<BranchList> list_branch;
    ListView  branchlist;
    String userid;
    TextView RecordNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_branchlist);

        progressDialog = new ProgressDialog(BranchlistActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        userid = objApp.getUserId();
        Branchlist(userid);
        RecordNotFound =(TextView)  findViewById(R.id.RecordNotFound);
        ImageView backbtnbranch =(ImageView) findViewById(R.id. backbtnbranch);
        backbtnbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(BranchlistActivity.this,HomeActivity.class);
                startActivity((intent));
                finish();
            }
        });
        TextView addbtnbranch=(TextView) findViewById(R.id.addbtnbranch);
        addbtnbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(BranchlistActivity.this, AddBranchActivity.class);
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
                ArrayList<BranchList> filtered = new ArrayList<BranchList>();
                for (int i = 0; i < list_branch.size() ; i++){
                    String Name = String.valueOf(list_branch.get(i).getbranch_name());
                    if (Name.contains(newText)){
                        BranchList contents = new BranchList(list_branch.get(i).getbranch_name(),
                                list_branch.get(i).getaddress(),list_branch.get(i).getaddress1(),
                                list_branch.get(i).getcity(),list_branch.get(i).getBranchId());
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapterbranch(getApplicationContext(),R.layout.branchlist,filtered);
                    branchlist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void Branchlist(String userid) {
        branchlist = (ListView)findViewById(R.id.branchlist);
        list_branch = new ArrayList<>();
        customAdapter = new CustomAdapterbranch(getApplicationContext(),R.layout.branchlist,list_branch);
        branchlist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBranchList",
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
                                BranchList branchList = new BranchList(object.getString("branch_name"),
                                        object.getString("address"),
                                        object.getString("address1"),
                                        object.getString("city"),
                                        object.getString("branch_id")
                                );
                                list_branch.add(branchList);
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
        Intent intent = new Intent(BranchlistActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}