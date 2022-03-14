package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.CustomerAdapter.CustomAdapterpopupicon;
import com.payirchithidal.Model.PopupIcon;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationPopupList extends AppCompatActivity {
    ListView poplist;
    String userid;
    private AppStorage objApp = null;
    private  String URL ="";
    TextView RecordNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_notification_popup_list);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification List");
        userid = objApp.getUserId();
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        poplist =(ListView) findViewById(R.id.poplist);
        ArrayList<PopupIcon> list_notify = new ArrayList<>();
        CustomAdapterpopupicon customAdapterpopup = new CustomAdapterpopupicon(getApplicationContext(),R.layout.popupnotifymsg,list_notify);
        poplist.setAdapter(customAdapterpopup);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getNotification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            if (0 == jsonarray.length()) {
                                RecordNotFound.setVisibility(View.VISIBLE);
                            }else {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                PopupIcon msglist = new PopupIcon(object.getString("profile"),
                                        object.getString("name"), object.getString("message"),
                                        object.getString("created_at"));
                                list_notify.add(msglist);
                            }
                            }
                            customAdapterpopup.notifyDataSetChanged();

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
}