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
import com.payirchithidal.CustomerAdapter.CustomAdapterkit;
import com.payirchithidal.Model.KitList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KitlistActivity<deletebtn> extends AppCompatActivity {


    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    ListView kitlist;
    CustomAdapterkit customAdapter;
    ArrayList<KitList> list_kit;
    String userid;
    SearchView search_edt;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_kitlist);

        progressDialog = new ProgressDialog(KitlistActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        userid = objApp.getUserId();
        kitlist = (ListView)findViewById(R.id.kitlist);
        RecordNotFound=(TextView)findViewById(R.id.RecordNotFound);
        ImageView backbtnkit = (ImageView) findViewById(R.id.backbtnkit);
        backbtnkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KitlistActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView addbtnkit=(TextView)findViewById(R.id.addbtnkit);
        addbtnkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(KitlistActivity.this, AddKitActivity.class);
                startActivity(intent);
            }
        });

        kitlist(userid);
        search_edt = (SearchView) findViewById(R.id.search_edt);
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
                ArrayList<KitList> filtered = new ArrayList<KitList>();
                for (int i = 0; i < list_kit.size() ; i++){
                    String Name = String.valueOf(list_kit.get(i).getkitname());
                    if (Name.contains(newText)){
                        KitList contents = new KitList(list_kit.get(i).getkitname(),
                                list_kit.get(i).getBranchname(),list_kit.get(i).getKitimg(),list_kit.get(i).getKitId());
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapterkit(getApplicationContext(),R.layout.kitlist_design,filtered);
                    kitlist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    public void kitlist(String userid){
        list_kit = new ArrayList<>();
        customAdapter = new CustomAdapterkit(getApplicationContext(),R.layout.kitlist_design,list_kit);
        kitlist.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getKitList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("kit_list");
                            progressDialog.dismiss();
                            if(0==jsonarray.length()){
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                KitList kitList = new KitList(object.getString("kit_name"),
                                        object.getString("branch_name"),
                                        object.getString("kit_img"),
                                        object.getString("kit_id"));
                                list_kit.add(kitList);
                            }
                            }
                            customAdapter.notifyDataSetChanged();

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
    public void onBackPressed() {
        Intent  intentBackPress = new Intent(KitlistActivity.this,HomeActivity.class);
        startActivity(intentBackPress);
        finish();
    }
}