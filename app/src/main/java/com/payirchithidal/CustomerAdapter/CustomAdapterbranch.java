package com.payirchithidal.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.BranchList;
import com.payirchithidal.BranchlistActivity;
import com.payirchithidal.EditBranchActivity;
import com.payirchithidal.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterbranch extends ArrayAdapter<BranchList> {
    ArrayList<BranchList> list_branch;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterbranch(@NonNull Context context, int resource, @NonNull ArrayList<BranchList> list_branch) {
        super(context, resource,list_branch);
        this.list_branch = list_branch;
        this.context = context;
        this.resource = resource;
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.branchlist, null, true);
        }

        BranchList pr = getItem(position);
        TextView branchname = (TextView) convertView.findViewById(R.id.branchname);
        TextView branchaddress = (TextView) convertView.findViewById(R.id.branchaddress);
        TextView branchcity = (TextView) convertView.findViewById(R.id.branchcity);
        ImageView editbranch =(ImageView) convertView.findViewById(R.id.editbranch);
        editbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBranch(pr.getBranchId());
            }
        });
        ImageView brdel =(ImageView) convertView.findViewById(R.id.brdel);
        View finalConvertView = convertView;
        brdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        Branchdel(pr.getBranchId());
                        Intent intent = new Intent(context, BranchlistActivity.class);;
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        branchname.setText(pr.getbranch_name());
        branchaddress.setText(pr.getaddress() +','+pr.getaddress1());
        branchcity.setText(pr.getcity());
        return convertView;
    }

    private void EditBranch(String branchId) {
        Intent intent = new Intent(context, EditBranchActivity.class);
        intent.putExtra("Branch_Id", branchId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void Branchdel(String branchId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(success.equals("1")){
                                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
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
                params.put("id",branchId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);
    }

}
