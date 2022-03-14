package com.payirchithidal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddKitActivity extends AppCompatActivity {
    EditText kitname;
    Spinner branch_dropdown;
    ImageView kitimg;
    Button kitsavebtn;
    String branchid1;
    String msg;
    File file1;
    public ImageView pick;
    private  String URL ="";
    private AppStorage objApp = null;
    static final int CAPTURE_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 10;
    Bitmap bt = null;
    File photoFile = null;
    String mCurrentPhotoPath;
    String UserId;
    String sname;
    String  branchname;
    int resStatus;
    ProgressDialog progressDialog;
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
        getSupportActionBar().setTitle("Add Kit Form");

        objApp = AppStorage.getInstance();
        URL = objApp.getApiUrl();
        setContentView(R.layout.activity_kit_add);
        kitname =(EditText)findViewById(R.id.kitname);
        branch_dropdown=(Spinner)findViewById(R.id.selectbranch);
        String userid = objApp.getUserId();
        Branchdropdownlist(userid);
        Button cancelbtnkit=(Button) findViewById(R.id.cancelbtnkit);
        cancelbtnkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddKitActivity.this, KitlistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        pick = (ImageView)findViewById(R.id.kitimg);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AddKitActivity.this,"Choose Icon");
            }
        });

        kitsavebtn =(Button)findViewById(R.id.kitsavebtn);
        kitsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserId = objApp.getUserId();
                sname =kitname.getText().toString().trim();
                branchname= String.valueOf(branchid1);
                if(sname.isEmpty()){
                    kitname.setError("This field is required.");
                    kitname.requestFocus();
                } else if(branchid1.isEmpty()){

                    TextView errorText = (TextView)branch_dropdown.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select Branch");
                    branch_dropdown.requestFocus();

                }else{
                    progressDialog = new ProgressDialog(AddKitActivity.this);
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

    private void selectImage(Context context, String title) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (ActivityCompat.checkSelfPermission(AddKitActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.CAMERA,
                                MY_CAMERA_REQUEST_CODE);
                    }else{
                        getCapture();

                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (ActivityCompat.checkSelfPermission(AddKitActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                Log.i("gok",photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(AddKitActivity.this,"Camera is not available."+e.toString());
        }
    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(AddKitActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddKitActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(AddKitActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private File createImageFile4() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(AddKitActivity.this,"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            AddKitActivity.this.getPackageName() + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }
        }

    }

    private File createImageFile() throws IOException  {
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
                        pick.setImageBitmap(bt);
                        file1 = photoFile;
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String rname = ImageFilePath.getPath(AddKitActivity.this,selectedImage);

                        if (selectedImage != null) {
                            try{
                                bt = getBitmapFromUri(selectedImage);
                            }catch (Exception o)
                            {

                            }
                            pick.setImageBitmap(bt);
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

    ArrayList<AdapterListData> branchlist = new ArrayList<AdapterListData>();
    branchlist.add(new AdapterListData("","Select Branch "));
    ArrayAdapter<AdapterListData> spinnerAdapter = new ArrayAdapter<AdapterListData>(AddKitActivity.this,R.layout.my_selected_item,branchlist);
    spinnerAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
    branch_dropdown.setAdapter(spinnerAdapter);
    branch_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AdapterListData id1 = (AdapterListData)parent.getItemAtPosition(position);
            branchid1 =id1.id;
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
                        Toast.makeText(AddKitActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(AddKitActivity.this,error.toString(), Toast.LENGTH_LONG).show();

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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.d("gokul","ss:"+requestCode);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCapture();
            }
            else {
                Toast.makeText(AddKitActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == MY_GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
            else {
                Toast.makeText(AddKitActivity.this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SaveAadhar extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            PostAadhar(strings);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(resStatus == 0){
                displayMessage(AddKitActivity.this,msg);
            }else{
                displayMessage(AddKitActivity.this,msg);
                Intent intent =new Intent(AddKitActivity.this,KitlistActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }

    private void PostAadhar(String[] strings) {
        try
        {
            String url = objApp.getApiUrl()+"addKit";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            String boundary = "-------------" + System.currentTimeMillis();
            httpPost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
            Log.d("response","post data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (file1 != null) {
                builder.addBinaryBody("image", file1);
            }
            builder.addTextBody("user_id",UserId);
            builder.addTextBody("kit_name", sname);
            builder.addTextBody("branch", branchname);
            builder.setBoundary(boundary);
            httpPost.setEntity(builder.build());
            HttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == 200) {
                HttpEntity entity1 = response.getEntity();
                String data = EntityUtils.toString(entity1);
                Log.d("response22",data);
                JSONObject jsono = new JSONObject(data);
                resStatus = jsono.getInt("status");
                String message = jsono.getString("msg");
                msg =message;

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
