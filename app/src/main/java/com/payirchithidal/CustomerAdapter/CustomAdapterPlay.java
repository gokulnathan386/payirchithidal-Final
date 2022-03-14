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
import android.widget.RelativeLayout;
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
import com.payirchithidal.EditplayersActivity;
import com.payirchithidal.Model.PlayList;
import com.payirchithidal.Playerslistactivity;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterPlay extends ArrayAdapter<PlayList> {

    ArrayList<PlayList> list_play;
    Context context;
    int resource;
    private  String URL ="";
    private AppStorage objApp = null;
    public CustomAdapterPlay(@NonNull Context context, int resource, @NonNull  ArrayList<PlayList> list_play) {
        super(context, resource, list_play);
        this.list_play=list_play;
        this.context=context;
        this.resource=resource;
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.player_list,null,true);
        }

        PlayList pr=getItem(position);

        ImageView payImg = (ImageView) convertView.findViewById(R.id.payImg);
        TextView playername =(TextView) convertView.findViewById(R.id.playername);
        TextView playernumber =(TextView) convertView.findViewById(R.id.playernumber);
        TextView branch =(TextView) convertView.findViewById(R.id.branch);
        ImageView editplay =(ImageView) convertView.findViewById(R.id.editplay);
        ImageView delplay =(ImageView) convertView.findViewById(R.id.delplay);
        RelativeLayout ProfileView =(RelativeLayout)  convertView.findViewById(R.id.ProfileView);
        View finalConvertView = convertView;
        ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerProfile.class);
                intent.putExtra("PlayerId",pr.getPlayerId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        delplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getRootView().getContext(), R.style.MyAlertDialogStyle);
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        delPlay(pr.getPlayerId());
                        Intent intent = new Intent(context, Playerslistactivity.class);;
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

        editplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EditPlayer(pr.getPlayerId());
            }
        });


        playername.setText(pr.getplayername());
        playernumber.setText(pr.getmobile());

        if (pr.getprofile().equals(objApp.apiImageUrl)) {
            payImg.setImageResource(R.drawable.pimg);
        } else {
            Picasso.get().load(pr.getprofile()).into(payImg);
        }

        return convertView;
    }
    private void delPlay(String playerId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"deletePlayer",
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
                params.put("id",playerId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(stringRequest);

    }

    private void EditPlayer(String playerId) {
        Intent intent = new Intent(context, EditplayersActivity.class);
        intent.putExtra("Player_Id", playerId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
