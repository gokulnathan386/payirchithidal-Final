package com.payirchithidal;

import static java.lang.Integer.parseInt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.payirchithidal.Model.FeesBalanceList;
import com.payirchithidal.Model.FeesBalanceListtype;
import com.payirchithidal.databinding.ActivityHomeBinding;
import com.payirchithidal.CustomerAdapter.CustomAdapterplayer;
import com.payirchithidal.CustomerAdapter.CustomAdapterplayertype;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private AppStorage objApp = null;
    private  String URL ="";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    ActionBarDrawerToggle toggle;
    AutoCompleteTextView PlayerList;
    private long pressedTime;
    String PlayId=null;
    String userid;
    int type = 0;
    LinearLayout getfees;
    ProgressDialog progressDialog;
    LinearLayout inputbox;
    LinearLayout FeesTitle;
    LinearLayout MonthlyAmountAndYearlyAmountTitle;
    ArrayList<AdapterListData> Play_list;
    ArrayAdapter<AdapterListData> spinnerAdapter;
    View customLayout;
    ListView balancelist;
    CustomAdapterplayertype Fixedamount;
    CustomAdapterplayer customAdapter1;
    String Currentdateformatted;
    EditText payamount;
    ArrayList<Integer> feesArray;
    String balance;
    TextView FeesPendingAmount,NoRecordFound;
    AlertDialog.Builder builder;
    JSONArray jsonarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        progressDialog = new ProgressDialog(HomeActivity.this);
        setContentView(binding.getRoot());

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nvDrawer=(NavigationView)findViewById(R.id.nav_view);
        mDrawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        userid = objApp.getUserId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView clubname = (TextView) hView.findViewById(R.id.clubname);
        clubname.setText(objApp.getUserName());
        toggle=new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.dashboard :
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_ClubBankAccount :
                        Intent Accdet = new Intent(HomeActivity.this, AccountDetials.class);
                        startActivity(Accdet);
                        finish();
                        break;
                    case R.id.nav_feesbtn:
                        Intent feesbtn = new Intent(HomeActivity.this, Feeslistactivity.class);
                        startActivity(feesbtn);
                        break;
                    case R.id.nav_Attendance:
                        Intent attendlist =new Intent(HomeActivity.this, AttendanceListActivity.class);
                        startActivity(attendlist);
                        break;
                    case R.id.nac_player:
                         Intent playerlist = new Intent(HomeActivity.this, Playerslistactivity.class);
                         startActivity(playerlist);
                        break;
                    case R.id.nav_Notification:
                        Intent notifylist = new Intent(HomeActivity.this, NotificationListActivity.class);
                        startActivity(notifylist);
                        break;
                    case R.id.nav_batch:
                        Intent batchlist = new Intent(HomeActivity.this, BatchlistActivity.class);
                        startActivity(batchlist);
                        break;
                    case R.id.nav_Kit:
                        Intent kitlist = new Intent(HomeActivity.this, KitlistActivity.class);
                        startActivity(kitlist);
                        break;
                    case R.id.nav_branch:
                        Intent branchlist = new Intent(HomeActivity.this, BranchlistActivity.class);
                        startActivity(branchlist);
                        break;
                    case R.id.nav_Sports:
                        Intent sportslist = new Intent(HomeActivity.this, SportsListActivity.class);
                        startActivity(sportslist);
                        break;
                    case R.id.nav_Payout:
                        Intent payoutlist = new Intent(HomeActivity.this, PayoutListActivity.class);
                        startActivity(payoutlist);
                        break;
                    case R.id.nav_Users:
                        Intent userlist = new Intent(HomeActivity.this, UserslistActivity.class);
                        startActivity(userlist);
                        break;
                    case R.id.nav_Role:
                        Intent rolelist = new Intent(HomeActivity.this, RoleActivityList.class);
                        startActivity(rolelist);
                        break;
                    case R.id.nav_ChangePassword:
                        Intent changepwd = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                        startActivity(changepwd);
                        break;
                    case R.id. nav_PendingFessList:
                        Intent PendingFees = new Intent(HomeActivity.this, PendingFeesList.class);
                        startActivity(PendingFees);
                        break;
                    case R.id. nav_Logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle);
                        builder.setTitle("Confirm!").
                                setMessage("Do you want logout?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"logout",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try{
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String success = jsonObject.getString("status");
                                                            String message = jsonObject.getString("msg");
                                                            if(success.equals("1")){
                                                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                            }
                                        })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String,String> params = new HashMap<>();
                                                params.put("userId",userid);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);
                                    }
                                });
                        builder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }
        });





        final Calendar cldr = Calendar.getInstance();
        Currentdateformatted = String.valueOf(DateFormat.format("yyyy-MM-dd", cldr.getTime()));
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void Feesbtn(View v) {
        customLayout = getLayoutInflater().inflate(R.layout.dashboardlayout, null);
        PlayerList = customLayout.findViewById(R.id.sportsname);
        getfees = customLayout.findViewById(R.id.getfees);
        inputbox = customLayout.findViewById(R.id.inputbox);
        payamount = customLayout.findViewById(R.id.payamount);
        FeesPendingAmount = customLayout.findViewById(R.id.FeesPendingAmount);
        FeesTitle = customLayout.findViewById(R.id.FeesTitle);
        MonthlyAmountAndYearlyAmountTitle = customLayout.findViewById(R.id.MonthlyAmountAndYearlyAmountTitle);
        NoRecordFound = customLayout.findViewById(R.id.NoRecordFound);
        getfees.setVisibility(View.GONE);
        getfees.setEnabled(false);
        Play_list = new ArrayList<AdapterListData>();
        spinnerAdapter = new ArrayAdapter<AdapterListData>(HomeActivity.this, android.R.layout.select_dialog_item, Play_list);
        PlayerList.setThreshold(1);
        PlayerList.setAdapter(spinnerAdapter);
        PlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                AdapterListData idpass = (AdapterListData) parent.getItemAtPosition(position);
                PlayId = idpass.id;
                BalanceAmount(PlayId);
                //Toast.makeText(HomeActivity.this,PlayId + " Selected",Toast.LENGTH_LONG).show();
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "getPlayerList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("player_id"),
                                        object.getString("player_name"));
                                Play_list.add(dropdown);
                            }
                            spinnerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);



        PlayerList.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                PlayId=null;
                getfees.setVisibility(View.GONE);
                MonthlyAmountAndYearlyAmountTitle.setVisibility(View.GONE);
                FeesTitle.setVisibility(View.GONE);
                NoRecordFound.setVisibility(View.GONE);

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });



        builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setView(customLayout);


        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });




        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {





             /*   feesArray = objApp.getArrayList();

                if (PlayId == null) {
                    Toast.makeText(getApplicationContext(), "Please Select Your Valid User", Toast.LENGTH_SHORT).show();
                } else {
                    if (type == 1) {
                        if (payamount.getText().toString().trim().isEmpty()) {
                            payamount.setError("This field is required.");
                            payamount.requestFocus();
                        } else if (parseInt(payamount.getText().toString().trim()) > parseInt(balance)) {
                            Toast.makeText(getApplicationContext(), "Payable amount should not be greater than balance amount", Toast.LENGTH_SHORT).show();
                        } else if (parseInt(payamount.getText().toString()) == 0) {
                            Toast.makeText(getApplicationContext(), "Pending amount not available", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            //feesArray = objApp.getArrayList();
                            CollectFees(userid, Currentdateformatted, PlayId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                            objApp.removeAll();
                        }
                    } else if (type == 2 || type == 3) {

                        if (jsonarray.length() > 0) {
                            if (String.valueOf(feesArray.size()).equals("0")) {
                                Toast.makeText(HomeActivity.this, "Select atleast one payment", Toast.LENGTH_LONG).show();
                            } else {

                                progressDialog.show();
                                progressDialog.setCancelable(false);
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                //feesArray = objApp.getArrayList();
                                //Toast.makeText(getApplicationContext(), "Count:"+feesArray.size(), Toast.LENGTH_SHORT).show();
                                CollectFees(userid, Currentdateformatted, PlayId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                                objApp.removeAll();
                            }
                        }

                    }
                }*/
            }
        });


        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String  Paymentpattern = "^[1-9][0-9]*$";  // Start Zero Not Allow

                Boolean wantToNoCloseDialog = true;

                feesArray = objApp.getArrayList();

                if (PlayId == null && wantToNoCloseDialog) {
                   Toast.makeText(getApplicationContext(), "Please Select Your Valid User", Toast.LENGTH_SHORT).show();
                } else {
                    if (type == 1 && wantToNoCloseDialog) {
                        if (jsonarray.length() > 0 && wantToNoCloseDialog) {
                            if (payamount.getText().toString().trim().isEmpty()) {
                                payamount.setError("This field is required.");
                                payamount.requestFocus();
                            } else if (parseInt(payamount.getText().toString().trim()) > parseInt(balance) && wantToNoCloseDialog) {
                                Toast.makeText(getApplicationContext(), "Payable amount should not be greater than balance amount", Toast.LENGTH_SHORT).show();
                            } else if (parseInt(payamount.getText().toString()) == 0 && wantToNoCloseDialog) {
                                Toast.makeText(getApplicationContext(), "Enter number greater than 0!", Toast.LENGTH_SHORT).show();
                            } else if(!payamount.getText().toString().trim().matches(Paymentpattern)){
                                payamount.setError("Invalid Amount");
                                payamount.requestFocus();
                            }else {
                                progressDialog.show();
                                progressDialog.setCancelable(false);
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );

                                //feesArray = objApp.getArrayList();
                                CollectFees(userid, Currentdateformatted, PlayId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                                objApp.removeAll();
                                dialog.dismiss();
                            }
                      }
                    } else if (type == 2 || type == 3 && wantToNoCloseDialog) {

                        if (jsonarray.length() > 0 && wantToNoCloseDialog) {
                            if (String.valueOf(feesArray.size()).equals("0") && wantToNoCloseDialog) {
                                Toast.makeText(HomeActivity.this, "Select atleast one payment", Toast.LENGTH_LONG).show();
                            } else {

                                progressDialog.show();
                                progressDialog.setCancelable(false);
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                //feesArray = objApp.getArrayList();
                                //Toast.makeText(getApplicationContext(), "Count:"+feesArray.size(), Toast.LENGTH_SHORT).show();
                                CollectFees(userid, Currentdateformatted, PlayId, payamount.getText().toString().trim(), String.valueOf(feesArray));
                                objApp.removeAll();
                                dialog.dismiss();
                            }
                        }

                    }
                }



            }
        });




    }

    private void CollectFees(String userid, String currentdateformatted, String playid, String payamount,String feesArray)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"collectFees",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("response",response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            progressDialog.dismiss();
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",userid);
                params.put("payment_date",currentdateformatted);
                params.put("amount",payamount);
                params.put("player_id",playid);
                params.put("fees_id",feesArray);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void BalanceAmount(String playId) {
        balancelist= customLayout.findViewById(R.id.balancelist);
        ArrayList<FeesBalanceList>  list_Fees = new ArrayList<>();
        ArrayList<FeesBalanceListtype>  list_Feestype = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getFessData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            type = jsonobject.getInt("type");
                            balance = jsonobject.getString("balance");
                            payamount.setText(balance);
                            jsonarray = jsonobject.getJSONArray("fees_list");


                                if(type == 1){

                                      if(balance.equals("0")){

                                          MonthlyAmountAndYearlyAmountTitle.setVisibility(View.GONE);
                                          MonthlyAmountAndYearlyAmountTitle.setEnabled(false);
                                          FeesTitle.setVisibility(View.GONE);
                                          Fixedamount = new CustomAdapterplayertype(getApplicationContext(),R.layout.dashboardlayout,list_Feestype);
                                          balancelist.setAdapter(Fixedamount);
                                          FeesPendingAmount.setText(balance);
                                          inputbox.setVisibility(View.GONE);
                                          getfees.setVisibility(View.GONE);
                                          NoRecordFound.setVisibility(View.VISIBLE);
                                      }else{

                                          NoRecordFound.setVisibility(View.GONE);
                                          MonthlyAmountAndYearlyAmountTitle.setVisibility(View.GONE);
                                          MonthlyAmountAndYearlyAmountTitle.setEnabled(false);
                                          FeesTitle.setVisibility(View.VISIBLE);
                                          Fixedamount = new CustomAdapterplayertype(getApplicationContext(),R.layout.dashboardlayout,list_Feestype);
                                          balancelist.setAdapter(Fixedamount);
                                          FeesPendingAmount.setText(balance);
                                          for (int i = 0; i < jsonarray.length(); i++) {
                                              getfees.setVisibility(View.VISIBLE);
                                              getfees.setEnabled(true);
                                              inputbox.setVisibility(View.VISIBLE);
                                              inputbox.setEnabled(true);
                                              JSONObject fees_list = jsonarray.getJSONObject(i);
                                              FeesBalanceListtype Feeslist=new FeesBalanceListtype(fees_list.getString("payment_date"),
                                                      fees_list.getString("fees_date"),
                                                      fees_list.getString("credit"),fees_list.getString("debit"));

                                              list_Feestype.add(Feeslist);
                                          }
                                          Collections.reverse(list_Feestype);//Reverse listview as message display
                                          Fixedamount.notifyDataSetChanged();
                                      }
                                } else if(type == 2 || type == 3){

                                          if(jsonarray.length() == 0){
                                              NoRecordFound.setVisibility(View.VISIBLE);
                                              MonthlyAmountAndYearlyAmountTitle.setVisibility(View.GONE);
                                              inputbox.setVisibility(View.GONE);
                                              inputbox.setEnabled(false);
                                              FeesTitle.setVisibility(View.GONE);
                                              FeesTitle.setEnabled(false);
                                              getfees.setVisibility(View.GONE);
                                              getfees.setEnabled(false);
                                          }else{
                                              NoRecordFound.setVisibility(View.GONE);
                                              MonthlyAmountAndYearlyAmountTitle.setVisibility(View.VISIBLE);
                                              inputbox.setVisibility(View.GONE);
                                              inputbox.setEnabled(false);
                                              FeesTitle.setVisibility(View.GONE);
                                              FeesTitle.setEnabled(false);
                                              getfees.setVisibility(View.VISIBLE);
                                              getfees.setEnabled(true);
                                              customAdapter1 = new CustomAdapterplayer(getApplicationContext(),R.layout.dashboardlayout,list_Fees);
                                              balancelist.setAdapter(customAdapter1);
                                              for (int i = 0; i < jsonarray.length(); i++) {
                                                  JSONObject fees_list = jsonarray.getJSONObject(i);
                                                  FeesBalanceList Feeslist=new FeesBalanceList(fees_list.getString("fees_id"),
                                                          fees_list.getString("fees_date"),
                                                          fees_list.getString("credit"),jsonobject.getString("type"));
                                                  list_Fees.add(Feeslist);
                                              }
                                              customAdapter1.notifyDataSetChanged();
                                          }

                                }
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
                params.put("player",playId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringRequest);
    }



    public void Player(View v) {
        Intent intent = new Intent(this, Playerslistactivity.class);
        startActivity(intent);
    }
    public void scan(View v) {
       Intent intent = new Intent(this, QrscanActivity.class);
       startActivity(intent);
    }
    public void nav_branch(MenuItem item) {
        Intent intent = new Intent(this, BranchlistActivity.class);
        startActivity(intent);
    }
    public void attendancebtn(View v){
        Intent intent = new Intent(HomeActivity.this, AttendanceListActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
           // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
    public void notifymsg(View view) {
        Intent intent =new Intent(getApplicationContext(),NotificationPopupList.class);
        startActivity(intent);
    }
    public void dashBoardPlayerList(View view) {
        Intent intent =new Intent(getApplicationContext(),Playerslistactivity.class);
        startActivity(intent);
    }
    public void dashBoardBatchList(View view) {
        Intent intent =new Intent(getApplicationContext(),BatchlistActivity.class);
        startActivity(intent);
    }
    public void dashBoardKitList(View view) {
        Intent intent =new Intent(getApplicationContext(),KitlistActivity.class);
        startActivity(intent);
    }
}