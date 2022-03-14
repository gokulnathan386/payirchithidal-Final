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
import com.payirchithidal.CustomerAdapter.CustomAdapterPlay;
import com.payirchithidal.Model.PlayList;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Playerslistactivity extends AppCompatActivity {
    ListView list;
    ImageView backbtn;
    private  String URL ="";
    private AppStorage objApp = null;
    ProgressDialog progressDialog;
    SearchView search_edt;
    ArrayList<PlayList> list_play;
    CustomAdapterPlay customAdapter;
    String userid;
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_players_list);
        progressDialog = new ProgressDialog(Playerslistactivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        userid = objApp.getUserId();
        Playerlist(userid);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        backbtn=(ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Playerslistactivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView addbtn=(TextView) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Playerslistactivity.this, AddPlayersActivity.class);
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
                ArrayList<PlayList> filtered = new ArrayList<PlayList>();
                for (int i = 0; i < list_play.size() ; i++){
                    String Name = String.valueOf(list_play.get(i).getplayername());
                    if (Name.contains(newText)){
                        PlayList contents = new PlayList(list_play.get(i).getprofile(),
                                list_play.get(i).getplayername(),list_play.get(i).getmobile(),
                                list_play.get(i).getemail(),
                                list_play.get(i).getPlayerId()
                                );
                        filtered.add(contents);
                    }
                    customAdapter = new CustomAdapterPlay(getApplicationContext(),R.layout.player_list,filtered);
                    list.setAdapter(customAdapter);
                }
                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void Playerlist(String userid) {
        list = (ListView)findViewById(R.id.list);
        list_play = new ArrayList<>();
        customAdapter = new CustomAdapterPlay(getApplicationContext(),R.layout.player_list,list_play);
        list.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPlayerList",
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
                                PlayList playerList = new PlayList(object.getString("profile"),
                                        object.getString("player_name"),
                                        object.getString("mobile"),
                                        object.getString("email"),
                                        object.getString("player_id")
                                );
                                list_play.add(playerList);
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
        Intent intent = new Intent(Playerslistactivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}