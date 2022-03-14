package com.payirchithidal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBranchActivity extends AppCompatActivity {

    private  String URL ="";
    private AppStorage objApp = null;
    String countryid;
    Spinner CountryList;
    Spinner StateList;
    Spinner CityList;
    String StateId;
    String CityId;
    String userid;
    String SportsId;
    Button  savebtn;
    EditText BranchName;
    EditText Address;
    EditText Address1;
    String AddBranchName;
    String Add_Address;
    String Add_Address1;
    String CountryIdpass;
    String StateIdpass;
    ArrayList<Integer> ServerKitList;
    String CityIdpass;
    String SportsIdpass;
    String BranchId;
    TextView branchSports;
    ArrayList<Integer> GetSportsId;
    String branch_name;
    String country_id ;
    String state_id ;
    String city;
    String address;
    String addressget;
    String clubidget,BranchSportsId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Branch Update Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_edit_branch);

        progressDialog = new ProgressDialog(EditBranchActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        Intent intent = getIntent();
        BranchId = intent.getStringExtra("Branch_Id");
        userid = objApp.getUserId();

        BranchName =(EditText) findViewById(R.id.BranchName);
        Address =(EditText) findViewById(R.id.Address);
        Address1 =(EditText) findViewById(R.id.Address1);
        CountryList =(Spinner) findViewById(R.id.country);
        StateList =(Spinner) findViewById(R.id.State);
        CityList =(Spinner) findViewById(R.id.city);
        branchSports = (TextView) findViewById(R.id.branchSports);
        branchSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SportsMultiselect(userid);
            }
        });

        Button cancelbtnbranch =(Button) findViewById(R.id.cancelbtnbranch);
        cancelbtnbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        savebtn = (Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBranchName =BranchName.getText().toString().trim();
                Add_Address =Address.getText().toString().trim();
                Add_Address1 =Address1.getText().toString().trim();
                CountryIdpass= String.valueOf(countryid);
                StateIdpass= String.valueOf(StateId);
                CityIdpass= String.valueOf(CityId);
                SportsIdpass= String.valueOf(SportsId);

                if(AddBranchName.isEmpty()){
                    BranchName.setError("This field is required.");
                    BranchName.requestFocus();
                }
                else if (Add_Address.isEmpty()){
                    Address.setError("This field is required.");
                    Address.requestFocus();

                }else if (Add_Address1.isEmpty()){
                    Address1.setError("This field is required.");
                    Address1.requestFocus();
                }
                else if (CountryIdpass.isEmpty()){
                    TextView errorText = (TextView)CountryList.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Country");
                }else if (StateIdpass.isEmpty()){
                    TextView errorText = (TextView)StateList.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Your State");
                }
                else if (CityIdpass.isEmpty()){
                    TextView errorText = (TextView)CityList.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Your City");
                } else{
                    progressDialog = new ProgressDialog(EditBranchActivity.this);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    UpadateBranch(BranchId,AddBranchName,CountryIdpass,StateIdpass,CityIdpass,Add_Address,Add_Address1,userid, String.valueOf(GetSportsId));
                }
            }
        });
        new BackgroundTask(EditBranchActivity.this) {
            @Override
            public void doInBackground() {
                BranchView(BranchId);
            }

            @Override
            public void onPostExecute() {

            }
        }.execute();
    }

    private void SportsMultiselect(String userid) {
        ArrayList<AdapterListData> stockList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getSportsList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)

                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("sports_id"),
                                        object.getString("sports_name"));
                                stockList.add(dropdown);

                            }

                            AlertDialog(stockList);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditBranchActivity.this,error.toString(), Toast.LENGTH_LONG).show();

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

    private void AlertDialog(ArrayList<AdapterListData> stockList) {

        String[] listItems = new String[stockList.size()];
        final List<String> selectedId = new ArrayList<>();
        final List<String> selectedItems = new ArrayList<>();
        Log.d("nathan9", String.valueOf(selectedItems));

        final boolean[] checkedItems = new boolean[stockList.size()];
        for(AdapterListData s : stockList) {
            selectedItems.add(s.name);
            selectedId.add(s.id);
            Log.d("dporsId",s.id);
        }
        listItems = selectedItems.toArray(listItems);

        for(String s : listItems)
            System.out.println(s);


        branchSports.setText(null);
        AlertDialog.Builder builder = new AlertDialog.Builder(EditBranchActivity.this,R.style.MyAlertDialogStyleMultiSelect);
        builder.setTitle("Select Your Sports");
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
                String currentItem = selectedId.get(which);
            }
        });

        for(int i = 0;i<selectedId.size();i++) {
            for(int k=0;k<ServerKitList.size();k++){
                if(selectedId.get(i).equals(ServerKitList.get(k).toString())){
                    checkedItems[i] = true;
                }
            }
        }

        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
               GetSportsId = new ArrayList<Integer>();
                Log.d("dataIdpass", String.valueOf(GetSportsId));
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        branchSports.setText(branchSports.getText() + selectedItems.get(i) + " ");
                        GetSportsId.add(Integer.valueOf(selectedId.get(i)));
                        Log.d("run",selectedId.get(i));
                    }
                }
            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                }*/
            }
        });

        builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void BranchView(String BranchId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBranchById",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("branch_data");
                                branch_name = data.getString("branch_name");
                                BranchName.setText(branch_name);
                                country_id = data.getString("country_id");
                                state_id = data.getString("state_id");
                                city = data.getString("city");
                                address = data.getString("address");
                                Address.setText(address);
                                addressget = data.getString("address1");
                                Address1.setText(addressget);
                                clubidget = data.getString("club_id");
                                BranchSportsId= data.getString("sports_id");

                                String source = BranchSportsId;
                                String[] integersAsText = source.split(",");
                                int[] results = new int[ integersAsText.length ];

                                ServerKitList = new ArrayList<>();

                                int i = 0;
                                for ( String textValue : integersAsText ) {
                                    results[i] = Integer.parseInt( textValue );
                                    ServerKitList.add(results[i]);
                                    i++;
                                }
                                CountryList();
                                progressDialog.dismiss();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(EditBranchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", BranchId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);

    }

    private void UpadateBranch(String BranchId,String addBranchName, String countryIdpass, String stateIdpass, String cityIdpass, String add_address, String add_address1,String userid, String GetSportsId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"updateBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditBranchActivity.this,BranchlistActivity.class);
                                progressDialog.dismiss();
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
            public Map<String, String> getParams() throws AuthFailureError  {
                Map<String,String> params = new HashMap<>();
                params.put("branch_id",BranchId);
                params.put("branch_name",addBranchName);
                params.put("country_id",countryIdpass);
                params.put("state_id",stateIdpass);
                params.put("city_id",cityIdpass);
                params.put("address",add_address);
                params.put("address1",add_address1);
                params.put("user_id",userid);
                params.put("sports_id",GetSportsId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CountryList() {
        ArrayList<AdapterListData> countrylist = new ArrayList<AdapterListData>();
        countrylist.add(new AdapterListData("","Select Country "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBranchActivity.this,R.layout.my_selected_item,countrylist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        CountryList.setAdapter(spinnerAdapter);
        CountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                countryid =idpass.id;
                Statelist(countryid);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL+"getCountry",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("id"),
                                        object.getString("name"));

                                countrylist.add(dropdown);
                            }
                            int positionCountry = getDropDownIndex(countrylist,country_id);
                            CountryList.setSelection(positionCountry);
                            spinnerAdapter.notifyDataSetChanged();

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
                });
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }

    public int getDropDownIndex(ArrayList objList,String id)
    {
        int index = 0;
        for(int i = 0;i < objList.size();i++)
        {
            AdapterListData objData = (AdapterListData) objList.get(i);
            if(objData.id.equals(id))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private void Statelist(String countryid) {
        ArrayList<AdapterListData> State_list = new ArrayList<AdapterListData>();
        State_list.add(new AdapterListData("","Select Your State "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBranchActivity.this,R.layout.my_selected_item,State_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        StateList.setAdapter(spinnerAdapter);

        StateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                StateId =idpass.id;
                CityList(StateId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getStateByCountry",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("state_id"),
                                        object.getString("state_name"));

                                State_list.add(dropdown);


                            }
                            int positionState = getDropDownIndex(State_list,state_id);
                            StateList.setSelection(positionState);
                            spinnerAdapter.notifyDataSetChanged();

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
                params.put("country_id",countryid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    private void CityList(String stateId) {
        ArrayList<AdapterListData> City_list = new ArrayList<AdapterListData>();
        City_list.add(new AdapterListData("","Select Your City "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditBranchActivity.this, R.layout.my_selected_item,City_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        CityList.setAdapter(spinnerAdapter);

        CityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                CityId =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getCityByState",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)

                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("city_id"),
                                        object.getString("city_name"));
                                City_list.add(dropdown);
                            }
                            int positionCity = getDropDownIndex(City_list,city);
                            CityList.setSelection(positionCity);
                            spinnerAdapter.notifyDataSetChanged();

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
                params.put("state_id",stateId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }


}