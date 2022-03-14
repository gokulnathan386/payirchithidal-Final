package com.payirchithidal;

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
import com.squareup.picasso.Picasso;

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

public class EditplayersActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText firstname;
    Intent intent;
    String plyerId;
    EditText lastname;
    Spinner Branchspinner,batch;
    ArrayList<Integer> PlayKitList;
    String playFirstName,playLastName,playMobile;
    String playEmail,playAddress;
    String playStatus,playQrCode,playDate1;
    EditText mobile,email,address;
    ImageView playimg;
    TextView kitplayer;
    ArrayList<AdapterListData> StatusList;
    Spinner countryply,stateply,cityply;
    Spinner status;
    EditText playdate;
    ArrayList<AdapterListData> KitList;
    ArrayList<Integer> ServerKitList;
    ImageView qrcode;
    List<String> selectedId;
    List<String> selectedItems;
    ArrayAdapter<AdapterListData> spinnerAdapterBatch;
    String countryId,StateId,CityId;
    ArrayList<AdapterListData> State_list;
    ArrayList<AdapterListData> branchlist;
    ArrayList<AdapterListData>  batch_list;
    String ListKit;
    private  String URL ="";
    private AppStorage objApp = null;
    int width;
    int height;
    int dimen;
    String playCity;
    String playCountry;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    String  PlayBatch,PlayerStatus;
    String profile,userid,branchId;
    ArrayAdapter<AdapterListData>  spinnerAdapterstate;
    ArrayAdapter<AdapterListData> spinnerAdapterCountry;
    String batchID,StatusId;
    static final int CAPTURE_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 10;
    Bitmap bt = null;
    File photoFile = null;
    String mCurrentPhotoPath;
    File file1;
    int resStatus;
    String msg,PlayBranch;
    Button savebtn;
    androidx.appcompat.app.AlertDialog.Builder builder;
    ArrayList<AdapterListData> City_list;
    ArrayAdapter<AdapterListData> spinnerAdapterBranch;
    ProgressDialog progressDialog;
    ArrayList<AdapterListData> countrylist;
    String  playState;
    private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0572ba"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Player");
        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_editplayers);
        progressDialog = new ProgressDialog(EditplayersActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        userid = objApp.getUserId();
        Branchspinner=(Spinner) findViewById(R.id.Branchspinner);

        countryply =(Spinner) findViewById(R.id.countryply);
        intent = getIntent();
        plyerId = intent.getStringExtra("Player_Id");

        firstname =(EditText) findViewById(R.id.firstname);
        lastname =(EditText) findViewById(R.id.lastname);

        batch =(Spinner) findViewById(R.id.batch);
        mobile =(EditText) findViewById(R.id.mobile);
        email =(EditText) findViewById(R.id.email);
        playimg=(ImageView) findViewById(R.id.playimg);
        kitplayer =(TextView) findViewById(R.id.kitplayer);
        address =(EditText) findViewById(R.id.address);
        stateply =(Spinner) findViewById(R.id.stateply);
        cityply =(Spinner) findViewById(R.id.cityply);
        status =(Spinner) findViewById(R.id.status);
        playdate =(EditText) findViewById(R.id.playdate);
        qrcode =(ImageView) findViewById(R.id.qrcode);

        kitplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MutliselectKit();
            }
        });

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        playdate.setText(DateFormat.format("dd/MM/yyyy", cldr.getTime()));
        playdate.setInputType(InputType.TYPE_NULL);
        playdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new DatePickerDialog(EditplayersActivity.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //playdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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

        playimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(EditplayersActivity.this,"Choose Icon");
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

        Button cancelbtn = (Button) findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        savebtn =(Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstname.getText().toString().trim().isEmpty()){
                    firstname.setError("This field is required.");
                    firstname.requestFocus();
                }else if(lastname.getText().toString().trim().isEmpty()){
                    lastname.setError("This field is required.");
                    lastname.requestFocus();
                }else if(branchId.isEmpty()){
                    TextView errorText = (TextView)Branchspinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                    Branchspinner.requestFocus();
                }else if(batchID.isEmpty()){
                    TextView errorText = (TextView)batch.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Batch");
                    batch.requestFocus();
                }else if(mobile.getText().toString().isEmpty()){
                    mobile.setError("This field is required.");
                    mobile.requestFocus();
                }else if(10 > mobile.getText().length()){
                    mobile.setError("This field is required.");
                    mobile.requestFocus();
                    progressDialog.dismiss();
                }else if(address.getText().toString().trim().isEmpty()){
                    address.setError("Invalid email address");
                    address.requestFocus();
                }else if(countryId.isEmpty()){
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
                    new SaveAadhar().execute();
                }

            }
        });
        StatusList = new ArrayList<>();
        StatusList.add(new AdapterListData("1", "Active"));
        StatusList.add(new AdapterListData("2", "In Active"));
        StatusList.add(new AdapterListData("3", "Discontinue"));
        ArrayAdapter<AdapterListData> spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(EditplayersActivity.this, R.layout.my_selected_item,StatusList);
        spinnerAdapterBatch.setDropDownViewResource(R.layout.my_dropdown_item);
        status.setAdapter(spinnerAdapterBatch);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                StatusId =idpass.id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        new BackgroundTask(EditplayersActivity.this) {
            @Override
            public void doInBackground() {
                viewPlayerDetail(plyerId);
            }

            @Override
            public void onPostExecute() {

            }
        }.execute();

    }

    private void selectImage(Context context, String title) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (ActivityCompat.checkSelfPermission(EditplayersActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.CAMERA,
                                MY_CAMERA_REQUEST_CODE);
                    }else{
                        getCapture();

                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (ActivityCompat.checkSelfPermission(EditplayersActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
    private void getCapture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            captureImage();
        }
        else
        {
            captureImage2();
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
            displayMessage(EditplayersActivity.this,"Camera is not available."+e.toString());
        }
    }
    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                Log.i("Murugan",photoFile.getAbsolutePath());
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            EditplayersActivity.this.getPackageName() + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }
        }

    }
    private File createImageFile4() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(EditplayersActivity.this,"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(EditplayersActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditplayersActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(EditplayersActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                    if (resultCode == Activity.RESULT_OK) {
                        bt = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        playimg.setImageBitmap(bt);
                        file1 = photoFile;
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String rname = ImageFilePath.getPath(EditplayersActivity.this,selectedImage);

                        if (selectedImage != null) {
                            try{
                                bt = getBitmapFromUri(selectedImage);
                            }catch (Exception o) {
                            }
                            playimg.setImageBitmap(bt);
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
    private void Branchdropdownlist(String userid) {
        branchlist = new ArrayList<AdapterListData>();
        branchlist.add(new AdapterListData("","Select Player Branch "));
        spinnerAdapterBranch= new ArrayAdapter<AdapterListData>(EditplayersActivity.this, R.layout.my_selected_item,branchlist);
        spinnerAdapterBranch.setDropDownViewResource(R.layout.my_dropdown_item);
        Branchspinner.setAdapter(spinnerAdapterBranch);
        Branchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData id1 = (AdapterListData)parent.getItemAtPosition(position);
                branchId =id1.id;
                BatchId(branchId);
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
                            int posBranchId = getDropDownIndex(branchlist,PlayBranch);
                            Branchspinner.setSelection(posBranchId);
                            spinnerAdapterBranch.notifyDataSetChanged();

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

    private void MutliselectKit() {
        KitList = new ArrayList<AdapterListData>();
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
                params.put("branch",branchId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }
    private void AlertDialog(ArrayList<AdapterListData> KitList) {
        String[] listItems = new String[KitList.size()];
         selectedId = new ArrayList<>();
         selectedItems = new ArrayList<>();
        Log.d("nathan9", String.valueOf(selectedItems));

        final boolean[] checkedItems = new boolean[KitList.size()];
        for(AdapterListData s : KitList) {
            selectedItems.add(s.name);
            selectedId.add(s.id);
            Log.d("dporsId",s.id);
        }
        listItems = selectedItems.toArray(listItems);



        for(String s : listItems)
            System.out.println(s);

        kitplayer.setText(null);
        builder = new androidx.appcompat.app.AlertDialog.Builder(EditplayersActivity.this,R.style.MyAlertDialogStyleMultiSelect);
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
                PlayKitList = new ArrayList<Integer>();

                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        kitplayer.setText(kitplayer.getText() + selectedItems.get(i) + ", ");
                        PlayKitList.add(Integer.valueOf(selectedId.get(i)));
                        Log.d("run",selectedId.get(i));
                    }
                }
            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void BatchId(String branchId) {

        batch_list = new ArrayList<AdapterListData>();
        batch_list.add(new AdapterListData("","Select Your Batch "));
        spinnerAdapterBatch = new ArrayAdapter<AdapterListData>(EditplayersActivity.this, R.layout.my_selected_item,batch_list);
        spinnerAdapterBatch.setDropDownViewResource(R.layout.my_dropdown_item);
        batch.setAdapter(spinnerAdapterBatch);
        batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                batchID =idpass.id;
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
                            int posbatchid = getDropDownIndex(batch_list,PlayBatch);
                            batch.setSelection(posbatchid);
                            spinnerAdapterBatch.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditplayersActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("branch",branchId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);
    }

    private void viewPlayerDetail(String plyerId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL+"getPlayer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String success = jsonobject.getString("status");
                            if (success.equals("true")) {
                                JSONObject data = jsonobject.getJSONObject("data");
                                playFirstName = data.getString("first_name");
                                playLastName = data.getString("last_name");
                                playMobile = data.getString("mobile");
                                playEmail = data.getString("email");
                                profile = data.getString("profile");
                                playAddress = data.getString("address");

                                playCountry = data.getString("country_id");
                                playState = data.getString("state_id");
                                playCity = data.getString("city_id");
                                PlayBranch = data.getString("branch_id");
                                PlayBatch = data.getString("batch_id");
                                playStatus = data.getString("player_status");
                                playQrCode = data.getString("qr_code");
                                playDate1 = data.getString("join_date");
                                PlayerStatus = data.getString("status");
                                ListKit = data.getString("kit");
                                String source = ListKit;
                                String[] integersAsText = source.split(",");
                                int[] results = new int[ integersAsText.length ];

                                ServerKitList = new ArrayList<>();

                                int i = 0;
                                for ( String textValue : integersAsText ) {
                                    results[i] = Integer.parseInt( textValue );
                                    ServerKitList.add(results[i]);
                                    i++;
                                }

                                firstname.setText(playFirstName);
                                lastname.setText(playLastName);
                                mobile.setText(playMobile);

                                if(playEmail.equals("null")){
                                    email.setText("");
                                }else{
                                    email.setText(playEmail);
                                }
                                address.setText(playAddress);
                                playdate.setText(playDate1);

                                if (profile.equals(objApp.apiImageUrl)) {
                                    playimg.setImageResource(R.drawable.pimg);
                                } else {
                                    Picasso.get().load(profile).into(playimg);
                                }

                                qrgEncoder = new QRGEncoder(playQrCode, null, QRGContents.Type.TEXT, dimen);
                                try {
                                    bitmap = qrgEncoder.encodeAsBitmap();
                                    qrcode.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    Log.e("Tag", e.toString());
                                }
                                Branchdropdownlist(userid);
                                CountryList();

                                int PlayerStatus = getDropDownIndex(StatusList,playStatus);
                                status.setSelection(PlayerStatus);
                                progressDialog.dismiss();

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
                params.put("id",plyerId);
                return params;
            }
        };
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
    private void CountryList() {
        
        countrylist = new ArrayList<AdapterListData>();
        countrylist.add(new AdapterListData("","Select Country "));
        spinnerAdapterCountry = new ArrayAdapter<AdapterListData>(EditplayersActivity.this, R.layout.my_selected_item,countrylist);
        spinnerAdapterCountry.setDropDownViewResource(R.layout.my_dropdown_item);
        countryply.setAdapter(spinnerAdapterCountry);
        countryply.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdapterListData idpass = (AdapterListData)parent.getItemAtPosition(position);
                countryId =idpass.id;
                Statelist(countryId);
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
                            int poscountry = getDropDownIndex(countrylist,playCountry);
                            countryply.setSelection(poscountry);
                            spinnerAdapterCountry.notifyDataSetChanged();
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

    private void Statelist(String countryid) {
        State_list = new ArrayList<AdapterListData>();
        State_list.add(new AdapterListData("","Select Your State "));
        spinnerAdapterstate = new ArrayAdapter<AdapterListData>(EditplayersActivity.this,R.layout.my_selected_item,State_list);
        spinnerAdapterstate.setDropDownViewResource(R.layout.my_dropdown_item);
        stateply.setAdapter(spinnerAdapterstate);
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
                            int psState = getDropDownIndex(State_list,playState);
                            stateply.setSelection(psState);
                            spinnerAdapterstate.notifyDataSetChanged();
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
        City_list = new ArrayList<AdapterListData>();
        City_list.add(new AdapterListData("","Select Your City "));
        ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(EditplayersActivity.this, R.layout.my_selected_item,City_list);
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
                            int poscity = getDropDownIndex(City_list,playCity);
                            cityply.setSelection(poscity);
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
    class SaveAadhar extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditplayersActivity.this);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }
        @Override
        protected String doInBackground(String... strings) {
            Log.d("status","on progress");
            PostAadhar(strings);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            displayMessage(EditplayersActivity.this,msg);
            Intent intent =new Intent(EditplayersActivity.this,Playerslistactivity.class);
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }

    }

    private void PostAadhar(String[] strings) {
        try
        {
            String url = objApp.getApiUrl()+"updatePlayer";
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
            builder.addTextBody("player_id",plyerId);
            builder.addTextBody("first_name", firstname.getText().toString().trim());
            builder.addTextBody("last_name", lastname.getText().toString().trim());
            builder.addTextBody("branch_id", branchId);
            builder.addTextBody("batch", batchID);
            builder.addTextBody("mobile", mobile.getText().toString().trim());
            builder.addTextBody("email", email.getText().toString().trim());
            builder.addTextBody("kit_id", String.valueOf(PlayKitList));
            builder.addTextBody("country_id", countryId);
            builder.addTextBody("state_id", StateId);
            builder.addTextBody("city_id", CityId);
            builder.addTextBody("user_id",userid);
            builder.addTextBody("player_status", StatusId);
            builder.addTextBody("address", address.getText().toString().trim());

            builder.setBoundary(boundary);
            httpPost.setEntity(builder.build());
            HttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == 200) {
                HttpEntity entity1 = response.getEntity();
                String data = EntityUtils.toString(entity1);
                JSONObject jsono = new JSONObject(data);
                Log.d("response22", String.valueOf(jsono));
                resStatus = jsono.getInt("status");
                msg = "Update  Player  Successfully";
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