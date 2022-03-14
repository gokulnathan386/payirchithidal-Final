package com.payirchithidal;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.CustomerAdapter.AccountDetailsAdapter;
import com.payirchithidal.Model.AccountDetail;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class AccountDetials extends AppCompatActivity {
    private  String URL ="";
    private AppStorage objApp = null;
    String userid;
    ListView accountdetails;
    TextView Addaccount,RecordNotFound;
    ImageView backbtnaccdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_account_detials);
        userid = objApp.getUserId();
        viewAccountDetails(userid);
        RecordNotFound =(TextView) findViewById(R.id.RecordNotFound);
        backbtnaccdetails =(ImageView) findViewById(R.id.backbtnaccdetails);
        backbtnaccdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AccountDetials.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        Addaccount =(TextView) findViewById(R.id.Addaccount);
        Addaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(AccountDetials.this,AddBankAccount.class);
               startActivity(intent);
            }
        });

    }
    private void viewAccountDetails(String userid) {
        accountdetails = (ListView)findViewById(R.id.accountdetails);
        ArrayList<AccountDetail>  accdetail = new ArrayList<>();
        AccountDetailsAdapter ClubBankAccount= new AccountDetailsAdapter(getApplicationContext(),R.layout.accountdetails,accdetail);
        accountdetails.setAdapter(ClubBankAccount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getAccDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            if(0==jsonarray.length()){
                                Addaccount.setVisibility(View.VISIBLE);
                                RecordNotFound.setVisibility(View.VISIBLE);
                              }else{
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    AccountDetail attendanceList=new AccountDetail(
                                            object.getString("account_id"),
                                            object.getString("club_name"),
                                            object.getString("holder_name"),
                                            object.getString("bank_name"),
                                            object.getString("acc_number"),
                                            object.getString("ifsc_code"),
                                            object.getString("branch_name")
                                    );
                                    accdetail.add(attendanceList);

                                }
                            }

                            ClubBankAccount.notifyDataSetChanged();

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
    public  void onBackPressed(){
       Intent intent =new Intent(AccountDetials.this,HomeActivity.class);
       startActivity(intent);
       finish();
    }
}