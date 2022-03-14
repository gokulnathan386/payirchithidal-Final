package com.payirchithidal.CustomerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
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
import com.google.zxing.WriterException;
import com.payirchithidal.session.AppStorage;
import com.payirchithidal.Model.FeesListPlayer;
import com.payirchithidal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PlayerProfile extends AppCompatActivity {
    ImageView profileimg;
    Intent intent;
    String playid;
    private  String URL ="";
    private AppStorage objApp = null;
    String playerFirstName,playerLastName,playerimg,playerMobileNo;
    TextView Profilname,mobileno,mail,playAddress,paidamt,pendingamt;
    String playermail,playerAddress,playPaidFees;
    String playPendingFees,playerPresent,playerAbsent,playDate;
    TextView playpresent,playabsent,joindate,PlayStatus,playbatch;
    String playStatus,qrCode,playBatchName,playBranchName,playAmountType;
    ImageView playqrcode;
    int width;
    int height;
    int dimen;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    TextView playbranch,playamttype;
    ListView feesListPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_player_profile);
        intent = getIntent();
        playid = intent.getStringExtra("PlayerId");
        playerProfileView(playid);
        Profilname =(TextView) findViewById(R.id.Profilname);
        profileimg =(ImageView) findViewById(R.id.profileimg);
        mobileno =(TextView) findViewById(R.id.mobileno);
        mail = (TextView) findViewById(R.id.mail);
        playAddress = (TextView) findViewById(R.id.address);
        paidamt = (TextView) findViewById(R.id.paidamt);
        pendingamt = (TextView) findViewById(R.id.pendingamt);
        playpresent = (TextView) findViewById(R.id.playpresent);
        playabsent = (TextView) findViewById(R.id.playabsent);
        joindate = (TextView) findViewById(R.id.joindate);
        PlayStatus = (TextView) findViewById(R.id.PlayStatus);
        playqrcode =(ImageView) findViewById(R.id.playqrcode);
        playbatch =(TextView) findViewById(R.id.playbatch);
        playbranch=(TextView) findViewById(R.id.playbranch);
        playamttype=(TextView) findViewById(R.id.playamttype);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;
        dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

    }

    private void playerProfileView(String playid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("1")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                Log.d("data", String.valueOf(data));
                                playerimg = data.getString("profile");

                                if (playerimg.equals(objApp.apiImageUrl)) {
                                    profileimg.setImageResource(R.drawable.pimg);
                                } else {
                                    Picasso.get().load(playerimg).into(profileimg);
                                }


                                playerFirstName = data.getString("first_name");
                                playerLastName = data.getString("last_name");
                                Profilname.setText(playerFirstName+" "+playerLastName);
                                playerMobileNo = data.getString("mobile");
                                mobileno.setText(playerMobileNo);
                                playermail = data.getString("email");

                                if(playermail.equals("null")){
                                    mail.setText("NA");
                                }else{
                                    mail.setText(playermail);
                                }


                                playerAddress = data.getString("address");
                                playAddress.setText(playerAddress);
                                playPaidFees = data.getString("paid_fees");
                                playPendingFees = data.getString("pending_fess");
                                pendingamt.setText(playPaidFees);
                                paidamt.setText(playPendingFees);
                                playerPresent = data.getString("present");
                                playpresent.setText(playerPresent);
                                playerAbsent = data.getString("absent");
                                playabsent.setText(playerAbsent);
                                playDate = data.getString("join_date");
                                joindate.setText(playDate);
                                playStatus = data.getString("status");
                                int PlayerStatus = data.getInt("player_status");
                                if(PlayerStatus ==1){
                                    PlayStatus.setText("Active");
                                }else if(PlayerStatus==2){
                                    PlayStatus.setText("In Active");
                                }else{
                                    PlayStatus.setText("Discontinue");
                                }
                                qrCode = data.getString("qr_code");
                                qrgEncoder = new QRGEncoder(qrCode, null, QRGContents.Type.TEXT, dimen);
                                try {
                                    bitmap = qrgEncoder.encodeAsBitmap();
                                    playqrcode.setImageBitmap(bitmap);
                                } catch (WriterException e) {

                                }
                                playBatchName = data.getString("batch_name");
                                playbatch.setText(playBatchName);
                                playBranchName = data.getString("branch_name");
                                playbranch.setText(playBranchName);
                                playAmountType = data.getString("amt_type");
                                if(playAmountType.equals("1")){
                                    playamttype.setText("Fixed Amount");
                                }else if(playAmountType.equals("2")){
                                    playamttype.setText("Monthly Amount");
                                }else{
                                    playamttype.setText("Yearly Amount");
                                }


                                feesListPlayer = (ListView)findViewById(R.id.feesListPlayer);
                                ArrayList<FeesListPlayer>  list_fees = new ArrayList<>();
                                PlayerFeesList playerFeesList = new PlayerFeesList(getApplicationContext(),R.layout.playerfeeslistlayout,list_fees);
                                feesListPlayer.setAdapter(playerFeesList);
                                try {
                                    JSONArray  FeesListPlay = data.getJSONArray("fees_list");
                                    for (int i = 0; i < FeesListPlay.length(); i++) {
                                        JSONObject feesList = FeesListPlay.getJSONObject(i);
                                        FeesListPlayer FeesList = new FeesListPlayer(
                                                feesList.getString("credit"),
                                                feesList.getString("debit"),
                                                feesList.getString("status"),
                                                feesList.getString("fees_date"),
                                                feesList.getString("payment_date")
                                                );
                                        list_fees.add(FeesList);
                                    }
                                    playerFeesList.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",playid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
}