package com.payirchithidal;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.zxing.WriterException;
import com.payirchithidal.session.AppStorage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddPlayersActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText  firstname;
    EditText  lastname;
    String  userid;
    Spinner Batch;
    String batchId;
    private  String URL ="";
    private AppStorage objApp = null;
    ImageView userpic;
    Spinner Branchspinner;
    String branchid;
    ArrayList<Integer> PlayKitList;
    Spinner countryply;
    Spinner stateply;
    Spinner cityply;
    String countryid;
    String StateId;
    String CityId;
    Button savebtn;
    String msg;
    androidx.appcompat.app.AlertDialog alertDialog;
    static final int CAPTURE_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 10;
    private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";
    Bitmap bt = null;
    File file1;
    String mCurrentPhotoPath;
    File photoFile = null;
    int resStatus;
    TextView mutlikit;
    String PlayState;
    String PlayBranchID,PlayCity,currenttime;
    String PlayBatchId,PlayCountry;
    EditText mobile,email,address;
    String PlayKitId;
    EditText playdate;
    ImageView Qrcode;
    int width;
    int height;
    int dimen;
    String emailPattern;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
   ProgressDialog progressDialog;
    String currentItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Player Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();

        setContentView(R.layout.activity_add_players);
        userid = objApp.getUserId();
        savebtn =(Button) findViewById(R.id.savebtn);
        Qrcode =(ImageView) findViewById(R.id.Qrcode);
        countryply = (Spinner) findViewById(R.id.countryply);
        stateply=(Spinner)findViewById(R.id.stateply);
        cityply=(Spinner)findViewById(R.id.cityply);
        Branchspinner = (Spinner) findViewById(R.id.Branchspinner);
        Branchdropdownlist(userid);
        Batch = (Spinner) findViewById(R.id.Batch);
        firstname = (EditText)findViewById(R.id.firstname);
        lastname =(EditText)findViewById(R.id.lastname);
        mobile =(EditText)findViewById(R.id.mobile);
        email =(EditText)findViewById(R.id.email);
        address =(EditText)findViewById(R.id.address);
        userpic =(ImageView)findViewById(R.id.playimg);
        playdate = (EditText) findViewById(R.id.playdate);
        PlayBranchID= String.valueOf(branchid);
        PlayBatchId= String.valueOf(batchId);
        PlayKitId= String.valueOf(PlayKitList);
        PlayCountry= String.valueOf(countryid);
        PlayState= String.valueOf(StateId);
        PlayCity= String.valueOf(CityId);
        PlayKitList = new ArrayList<Integer>();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        playdate.setText(DateFormat.format("yyyy-MM-dd", cldr.getTime()));
        playdate.setInputType(InputType.TYPE_NULL);
        playdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new DatePickerDialog(AddPlayersActivity.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //playdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                String monthString = String.valueOf(monthOfYear + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }
                                playdate.setText(year + "-" + (monthString) + "-" + dayString);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        TextView cancelbtn=(TextView) findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CountryList();
        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AddPlayersActivity.this,"Choose Icon");
            }
        });
        mutlikit = (TextView) findViewById(R.id.mutlikit);
        mutlikit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MutliselectKit();
            }
        });

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;
        dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hmmss");
        currenttime = sdf.format(c.getTime());
        qrgEncoder = new QRGEncoder("PT_"+currenttime, null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            Qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(firstname.getText().toString().trim().isEmpty()){
                      firstname.setError("This field is required.");
                      firstname.requestFocus();
                  }else if(lastname.getText().toString().trim().isEmpty()){
                      lastname.setError("This field is required.");
                      lastname.requestFocus();
                  }else if(branchid.isEmpty()){
                      TextView errorText = (TextView)Branchspinner.getSelectedView();
                      errorText.setError("");
                      errorText.setTextColor(Color.RED);
                      errorText.setText("Select Attendance");
                      Branchspinner.requestFocus();
                  }else if(batchId.isEmpty()){
                      TextView errorText = (TextView)Batch.getSelectedView();
                      errorText.setError("");
                      errorText.setTextColor(Color.RED);
                      errorText.setText("Select Batch");
                      Batch.requestFocus();
                }else if(mobile.getText().toString().isEmpty()){
                      mobile.setError("This field is required.");
                      mobile.requestFocus();
                  }else if(10 > mobile.getText().length()){
                      mobile.setError("This field is required.");
                      mobile.requestFocus();
                }else if(address.getText().toString().trim().isEmpty()){
                      address.setError("This field is required.");
                      address.requestFocus();
                  }else if(countryid.isEmpty()){
                      TextView errorText = (TextView)countryply.getSelectedView();
                      errorText.setError("");
                      errorText.setTextColor(Color.RED);
                      errorText.setText("Select Country");
                      countryply.requestFocus();
                  }
                  else if(StateId.isEmpty()){
                      TextView errorText = (TextView)stateply.getSelectedView();
                      errorText.setError("");
                      errorText.setTextColor(Color.RED);
                      errorText.setText("Select Your State");
                      stateply.requestFocus();
                  }else if(CityId.isEmpty()){
                      TextView errorText = (TextView)cityply.getSelectedView();
                      errorText.setError("");
                      errorText.setTextColor(Color.RED);
                      errorText.setText("Select Your city");
                      cityply.requestFocus();
                  }else{
                      progressDialog = new ProgressDialog(AddPlayersActivity.this);
                      progressDialog.show();
                      progressDialog.setCancelable(false);
                      progressDialog.setContentView(R.layout.progress_dialog);
                      progressDialog.getWindow().setBackgroundDrawableResource(
                              android.R.color.transparent
                      );
                      new SaveAadhar().execute();

                  }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == Activity.RESULT_OK) {
                        bt = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        userpic.setImageBitmap(bt);
                        file1 = photoFile;
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String rname = ImageFilePath.getPath(AddPlayersActivity.this,selectedImage);

                        if (selectedImage != null) {
                            try{
                                bt = getBitmapFromUri(selectedImage);
                            }catch (Exception o)
                            {

                            }
                            userpic.setImageBitmap(bt);
                            file1 = new File(rname);
                        }

                    }
                    break;
            }
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private void selectImage(Context context, String title) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (ActivityCompat.checkSelfPermission(AddPlayersActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.CAMERA,
                                MY_CAMERA_REQUEST_CODE);
                    }else{
                        getCapture();

                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (ActivityCompat.checkSelfPermission(AddPlayersActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                MY_GALLERY_REQUEST_CODE);
                    }else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void getCapture()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            captureImage();
        }
        else
        {
            captureImage2();
        }
    }

    private void captureImage()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                Log.i("Murugan",photoFile.getAbsolutePath());
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            AddPlayersActivity.this.getPackageName() + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }
        }
    }
    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if(photoFile!=null)
            {
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(AddPlayersActivity.this,"Camera is not available."+e.toString());
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private File createImageFile4()
    {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(AddPlayersActivity.this,"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;

    }
    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(AddPlayersActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddPlayersActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(AddPlayersActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCapture();
            }
            else {
                Toast.makeText(AddPlayersActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == MY_GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
            else {
                Toast.makeText(AddPlayersActivity.this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void CountryList() {
        ArrayList<AdapterListData> countrylist = new ArrayList<AdapterListData>();
        countrylist.add(new AdapterListData("","Select Country "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddPlayersActivity.this,R.layout.my_selected_item,countrylist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        countryply.setAdapter(spinnerAdapter);
        countryply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                countryid =idpass.id;
                StateList(countryid);
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

    private void StateList(String countryid) {
        ArrayList<AdapterListData> State_list = new ArrayList<AdapterListData>();
        State_list.add(new AdapterListData("","Select Your State "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddPlayersActivity.this,R.layout.my_selected_item,State_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        stateply.setAdapter(spinnerAdapter);
        stateply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddPlayersActivity.this,R.layout.my_selected_item,City_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        cityply.setAdapter(spinnerAdapter);
        cityply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    private void Branchdropdownlist(String userid) {
        ArrayList<AdapterListData> branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select Player Branch "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddPlayersActivity.this,R.layout.my_selected_item,branchlist);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        Branchspinner.setAdapter(spinnerAdapter);
        Branchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData id1 = (AdapterListData)parent.getItemAtPosition(position);
                branchid =id1.id;
                BatchId(branchid);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("branch_id"),
                                        object.getString("branch_name"));
                                branchlist.add(dropdown);
                            }
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
                params.put("user_id",userid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }


    private void BatchId(String branchid) {

        ArrayList<AdapterListData>  batch_list = new ArrayList<AdapterListData>();
        batch_list.add(new AdapterListData("","Select Your Batch "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddPlayersActivity.this,R.layout.my_selected_item,batch_list);
        spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        Batch.setAdapter(spinnerAdapter);
        Batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                batchId =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getBatchByBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("batch_id"),
                                        object.getString("name"));
                                batch_list.add(dropdown);
                            }
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
                params.put("branch",branchid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    private void MutliselectKit() {
        ArrayList<AdapterListData> KitList = new ArrayList<AdapterListData>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getKitByBranch",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)

                    {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                AdapterListData dropdown = new AdapterListData(object.getString("kit_id"),
                                        object.getString("kit_name"));
                                KitList.add(dropdown);
                            }
                            AlertDialog(KitList);
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
                params.put("branch",branchid);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    private void AlertDialog(ArrayList<AdapterListData> KitList) {

        String[] listItems = new String[KitList.size()];

        final List<String> selectedId = new ArrayList<>();//temp


        final List<String> selectedItems = new ArrayList<>();

        final boolean[] checkedItems = new boolean[KitList.size()];

        for(AdapterListData s : KitList) {
            selectedItems.add(s.name);
            selectedId.add(s.id);
        }
        listItems = selectedItems.toArray(listItems);

        for(String s : listItems)
            System.out.println(s);




        mutlikit.setText(null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddPlayersActivity.this,R.style.MyAlertDialogStyleMultiSelect);
        builder.setTitle("Select Your Sports");

        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
                currentItem = selectedId.get(which);

                if(isChecked){
                    PlayKitList.add(Integer.valueOf(currentItem));
                }else{
                    PlayKitList.remove(Integer.valueOf(currentItem));
                }

                //Toast.makeText(AddPlayersActivity.this,   currentItem + " ",Toast.LENGTH_SHORT).show();
            }
        });


        for(int i = 0;i<selectedId.size();i++) {
            for(int k=0;k<PlayKitList.size();k++){
                if(selectedId.get(i).equals(PlayKitList.get(k).toString())){
                    checkedItems[i] = true;
                }
            }
        }

        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        mutlikit.setText(mutlikit.getText() + selectedItems.get(i) + ", ");
                       // PlayKitList.add(Integer.valueOf(selectedId.get(i)));
                        Log.d("run",selectedId.get(i));
                    }
                }

            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PlayKitList.clear();
            }
        });


        builder.create();
        alertDialog = builder.create();
        alertDialog.show();


    }

    class SaveAadhar extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            Log.d("status","on progress");
            PostAadhar(strings);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            displayMessage(AddPlayersActivity.this,msg);
            //PlayKitList.clear();
            Intent intent =new Intent(AddPlayersActivity.this,Playerslistactivity.class);
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }

    }

    private void PostAadhar(String[] strings) {
        try
        {
            String url = objApp.getApiUrl()+"addPlayer";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            String boundary = "-------------" + System.currentTimeMillis();
            httpPost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
            Log.d("response","post data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (file1 != null) {
                builder.addBinaryBody("profile", file1);
            }
            builder.addTextBody("user_id",userid);
            builder.addTextBody("batch", batchId);
            builder.addTextBody("branch_id", branchid);
            builder.addTextBody("city_id", CityId);
            builder.addTextBody("state_id", StateId);
            builder.addTextBody("country_id", countryid);
            builder.addTextBody("address",address.getText().toString().trim());
            builder.addTextBody("mobile", mobile.getText().toString().trim());
            builder.addTextBody("email",email.getText().toString().trim());
            builder.addTextBody("first_name",firstname.getText().toString().trim());
            builder.addTextBody("last_name",lastname.getText().toString().trim());
            builder.addTextBody("qr_code", "PT_"+currenttime);
            builder.addTextBody("joining_date", playdate.getText().toString().trim());
            builder.addTextBody("kit_id", String.valueOf(PlayKitList));
            builder.setBoundary(boundary);
            httpPost.setEntity(builder.build());
            HttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            msg = String.valueOf(status);
            Log.d("status", String.valueOf(status));
            if(status == 200) {
                HttpEntity entity1 = response.getEntity();
                String data = EntityUtils.toString(entity1);
                JSONObject jsono = new JSONObject(data);
                Log.d("response22", String.valueOf(jsono));
                resStatus = jsono.getInt("status");
                msg = "Add  Player  Successfully";
            }else{
                msg = "Failed to connect with server";
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}