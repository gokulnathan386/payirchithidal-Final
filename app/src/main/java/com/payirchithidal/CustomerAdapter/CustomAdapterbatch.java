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
import com.payirchithidal.Model.BatchList;
import com.payirchithidal.BatchlistActivity;
import com.payirchithidal.EditBatchActivity;
import com.payirchithidal.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterbatch extends ArrayAdapter<BatchList> {

    ArrayList<BatchList> list_batch;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterbatch(@NonNull Context context, int resource, @NonNull ArrayList<BatchList> list_batch) {
        super(context, resource, list_batch);
        this.list_batch = list_batch;
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
            convertView = layoutInflater.inflate(R.layout.batch_listdesign, null, true);
        }

        BatchList pr = getItem(position);
        TextView batchname = (TextView) convertView.findViewById(R.id.batchname);
        TextView batch_type = (TextView) convertView.findViewById(R.id.batchtype);
        TextView starttime = (TextView) convertView.findViewById(R.id.starttime);
        TextView EndTime = (TextView) convertView.findViewById(R.id.EndTime);
        ImageView editplay =(ImageView) convertView.findViewById(R.id.editplay);
        editplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPlay(pr.getBatchId());
            }
        });
        ImageView delbatch = (ImageView) convertView.findViewById(R.id.delbatch);
        View finalConvertView = convertView;
        delbatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        BatchDel(pr.getBatchId());
                        Intent intent = new Intent(context, BatchlistActivity.class);;
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
        batchname.setText(pr.getbatch_name());
        starttime.setText(pr.getstart_time());
        EndTime.setText(pr.getend_time());
        return convertView;
    }

    private void EditPlay(String batchId) {
        Intent intent = new Intent(context, EditBatchActivity.class);
        intent.putExtra("Batch_Id", batchId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void BatchDel(String batchId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deleteBatch",
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
                params.put("id",batchId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);
    }
}