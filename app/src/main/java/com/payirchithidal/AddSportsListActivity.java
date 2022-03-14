package com.payirchithidal;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.payirchithidal.session.AppStorage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSportsListActivity extends AppCompatActivity {
    Button cancelbtnsports;
    ImageView sportsimg;
    int SELECT_PICTURE = 200;
    Button sportssavebtn;
    EditText sportsname;
    String  sportname;
    String userid;
    int resStatus;
    String msg;
    private  String URL ="";
    private AppStorage objApp = null;
    private  String url ="";
    File photoFile = null;
    Bitmap bt = null;
    File file1;
    String mCurrentPhotoPath;
    ProgressDialog progressDialog;
    static final int CAPTURE_IMAGE_REQUEST = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 10;
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
        getSupportActionBar().setTitle("Sports Form");
        objApp = AppStorage.getInstance();
        url = objApp.getApiUrl();
        setContentView(R.layout.activity_add_sports_list);
        sportsname = findViewById(R.id.sportsname);
        sportsimg =(ImageView) findViewById(R.id.sportsimg);
        sportssavebtn =(Button)findViewById(R.id.sportssavebtn);
        sportssavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportname = sportsname.getText().toString().trim();
                userid = objApp.getUserId();
                if(sportname.isEmpty()){
                    sportsname.setError("This field is required.");
                    sportsname.requestFocus();
                }else{
                    progressDialog = new ProgressDialog(AddSportsListActivity.this);
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
        cancelbtnsports =(Button) findViewById(R.id.cancelbtnsports);
        cancelbtnsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sportsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AddSportsListActivity.this,"Choose Icon");
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
                        sportsimg.setImageBitmap(bt);
                        file1 = photoFile;
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String rname = ImageFilePath.getPath(AddSportsListActivity.this,selectedImage);

                        if (selectedImage != null) {
                            try{
                                bt = getBitmapFromUri(selectedImage);
                            }catch (Exception o)
                            {

                            }
                            sportsimg.setImageBitmap(bt);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (ActivityCompat.checkSelfPermission(AddSportsListActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.CAMERA,
                                MY_CAMERA_REQUEST_CODE);
                    }else{
                        getCapture();

                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (ActivityCompat.checkSelfPermission(AddSportsListActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                MY_GALLERY_REQUEST_CODE);
                    }else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
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
                            AddSportsListActivity.this.getPackageName() + ".provider",
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
                Log.i("Murugan",photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(AddSportsListActivity.this,"Camera is not available."+e.toString());
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
                displayMessage(AddSportsListActivity.this,"Unable to create directory.");
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
        if (ContextCompat.checkSelfPermission(AddSportsListActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddSportsListActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(AddSportsListActivity.this,
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
                Toast.makeText(AddSportsListActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == MY_GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
            else {
                Toast.makeText(AddSportsListActivity.this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();
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
            Log.d("status","on progress");
            PostAadhar(strings);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if(resStatus==0){
                displayMessage(AddSportsListActivity.this,msg);
            }else{
                displayMessage(AddSportsListActivity.this,msg);
                Intent intent =new Intent(AddSportsListActivity.this,SportsListActivity.class);
                startActivity(intent);
                finish();
            }
                //displayMessage(AddSportsListActivity.this,msg);
               // Intent intent =new Intent(AddSportsListActivity.this,SportsListActivity.class);
                /* startActivity(intent);
                  finish();*/
        }

    }
    public void PostAadhar(String[] valuse) {
        try
        {
            String url = objApp.getApiUrl()+"addSports";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(url);
            String boundary = "-------------" + System.currentTimeMillis();
            httpPost.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
            Log.d("response","post data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (file1 != null) {
                builder.addBinaryBody("icon", file1);
            }
            builder.addTextBody("user_id",userid);
            builder.addTextBody("sports_name", sportname);
            builder.setBoundary(boundary);
            httpPost.setEntity(builder.build());
            HttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            Log.d("status", String.valueOf(status));
            if(status == 200) {
                HttpEntity entity1 = response.getEntity();
                String data = EntityUtils.toString(entity1);
                Log.d("response22",data);
                JSONObject jsono = new JSONObject(data);
                resStatus = jsono.getInt("status");
                String message = jsono.getString("msg");

                msg = message;



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