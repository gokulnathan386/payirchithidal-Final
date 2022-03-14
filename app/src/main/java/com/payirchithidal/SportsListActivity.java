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
import com.payirchithidal.CustomerAdapter.CustomAdaptersports;
import com.payirchithidal.Model.SportsList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SportsListActivity extends AppCompatActivity {
    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    ImageView backbtnsports;
    SearchView search_edt;
    ArrayList<SportsList> list_sports;
    CustomAdaptersports customAdapter;
    ListView sportslist;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_sports_list);

        backbtnsports = (ImageView) findViewById(R.id.backbtnsports);
        progressDialog = new ProgressDialog(SportsListActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        String userid = objApp.getUserId();
        sportslist(userid);
        RecordNotFound=(TextView) findViewById(R.id.RecordNotFound);
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
                ArrayList<SportsList> filtered = new ArrayList<SportsList>();
                for (int i = 0; i < list_sports.size() ; i++){
                    String Name = String.valueOf(list_sports.get(i).getsportsname());
                    if (Name.contains(newText)){
                        SportsList contents = new SportsList(list_sports.get(i).getsportsname(),
                                list_sports.get(i).geticon(),list_sports.get(i).getsportId());
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdaptersports(getApplicationContext(),R.layout.sportslist_design,filtered);
                    sportslist.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
        TextView addlist=(TextView) findViewById(R.id.addlist);
        addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SportsListActivity.this, AddSportsListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        backbtnsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(SportsListActivity.this,HomeActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }
    private void sportslist(String userid) {
        sportslist = (ListView)findViewById(R.id.sportslist);
        list_sports = new ArrayList<>();
        customAdapter = new CustomAdaptersports(getApplicationContext(),R.layout.sportslist_design,list_sports);
        sportslist.setAdapter(customAdapter);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getSportsList",
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
                                    SportsList sportsList = new SportsList(object.getString("sports_name"),
                                            object.getString("icon"), object.getString("sports_id"));
                                    list_sports.add(sportsList);

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
        Intent intent = new Intent(SportsListActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();

    }

}