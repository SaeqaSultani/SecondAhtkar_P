package com.example.women.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.women.BuildConfig;
import com.example.women.R;
import com.example.women.networking_senddata.ApiClient;
import com.example.women.networking_senddata.ApiInterfaceSendReport;
import com.example.women.util.MySharedPreferences;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportActivityPlaces extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //api
    private ApiInterfaceSendReport apiInterface;

    //fields for capture image
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_Picker = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private final int REQUEST_PREMISSION_CODE = 2342;
    private String currentPhotoPath = null;
    private String mImageFileLocation = "";

    //SharedPreferences
    private MySharedPreferences preferences;
    private int id;


    //field for upload image
    private ImageView btn_chooseImage, setImage;
    private Button btn_upload;
    private EditText et_address,et_position;
    private TextView tv_load;
    private Context context;
    private ProgressBar progressBar;
    private Spinner sp_province,sp_county;
    private TextView tv_county,tv_province;
    Handler handler;
    Runnable runnable;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_places);

        //permission method
        RequestStoragePermission();

        //find views
        findView();

        //sharedPrefereces
        preferences = MySharedPreferences.getInstance(this);
        id = preferences.getId();
//        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();


        ArrayAdapter<CharSequence> arrayAdapter_1 = ArrayAdapter.createFromResource(this, R.array.array_province, android.R.layout.simple_spinner_item);
        arrayAdapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_province.setAdapter(arrayAdapter_1);
        sp_province.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter_2 = ArrayAdapter.createFromResource(this, R.array.array_county, android.R.layout.simple_spinner_item);
        arrayAdapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_county.setAdapter(arrayAdapter_2);
        sp_county.setOnItemSelectedListener(this);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }

    //method for choose image
    public void onClick(View view){

        new MaterialDialog.Builder(this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                selectImage();
                                break;
                            case 1:
                                camera();
                                break;
                        }
                    }
                })
                .show();
    }

    public void findView(){

        et_address = findViewById(R.id.activity_report_places_EditText_address);
        et_position = findViewById(R.id.activity_report_places_EditText_position);
        btn_chooseImage = findViewById(R.id.chooseImage);
        setImage = findViewById(R.id.ImageView);
        btn_upload = findViewById(R.id.btn_upload);
        progressBar = findViewById(R.id.spin_kit_report);
        tv_load = findViewById(R.id.text_view_load_report);
        sp_province = findViewById(R.id.activity_report_places_spinner_province);
        sp_county = findViewById(R.id.activity_report_places_spinner_county);
        tv_county = findViewById(R.id.activity_report_places_TextView_county);
        tv_province = findViewById(R.id.activity_report_places_TextView_province);
    }


    //Request method for capture image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {


            setImage.setImageURI(Uri.parse(currentPhotoPath));
            galleryAddPic(currentPhotoPath);

        } else if (requestCode == REQUEST_IMAGE_Picker && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            currentPhotoPath = getPath(uri);
            setImage.setImageURI(Uri.parse(currentPhotoPath));
            galleryAddPic(currentPhotoPath);


        }else if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK){

            if (Build.VERSION.SDK_INT > 21) {

                Glide.with(this).load(mImageFileLocation).into(setImage);
                currentPhotoPath = mImageFileLocation;

            }
            else{
                Uri uri = data.getData();
                currentPhotoPath = getPath(uri);
                setImage.setImageURI(Uri.parse(currentPhotoPath));
                galleryAddPic(currentPhotoPath);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                setImage.setImageBitmap(bitmap);
            }

        }

    }

//methods for upload image
    private void galleryAddPic(String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_Picker);
        }
    }

    public void camera(){
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        }else {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_PIC_REQUEST);
        }

    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //api methods
    private void uploadImage() {

        if (currentPhotoPath == null || currentPhotoPath.equals("")) {
            Toasty.info(ReportActivityPlaces.this, R.string.info_toast, Toasty.LENGTH_SHORT, true).show();
            return;
        }

        if (sp_province.getSelectedItemPosition() == 0){
            Toasty.warning(ReportActivityPlaces.this, getString(R.string.choose_province), Toasty.LENGTH_SHORT).show();
            return;
        }

        if (sp_county.getSelectedItemPosition() == 0){
            Toasty.warning(ReportActivityPlaces.this, getString(R.string.choose_county), Toasty.LENGTH_SHORT).show();
            return;
        }

        String address = et_address.getText().toString();
        String position = et_position.getText().toString();


        if (address.isEmpty()) {
            et_address.setError(getString(R.string.writ_village));
            et_address.findFocus();
            return;
        }
        if (position.isEmpty()) {
            et_position.setError(getString(R.string.write_place_of_owner));
            et_position.findFocus();
            return;
        }

        //Initialize progress handler.
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        tv_load.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timer.cancel();
                progressBar.setVisibility(View.GONE);
                tv_load.setVisibility(View.GONE);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(runnable);
            }
        }, 5000, 3000);


        //call apiInterfaice
        apiInterface = ApiClient.getApiClient().create(ApiInterfaceSendReport.class);
        Call<ResponseBody> call = apiInterface.uploadDataPlaces(
                createPartFromString(tv_province.getText().toString()),
                createPartFromString(tv_county.getText().toString()),
                createPartFromString(et_position.getText().toString()),
                createPartFromString(et_address.getText().toString()),
                prepareFilePart(currentPhotoPath, "image", "c_photo"),
                id
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (!response.isSuccessful()) {

                    Toasty.warning(ReportActivityPlaces.this, R.string.warning_toast , Toasty.LENGTH_SHORT, true).show();
//                    Toast.makeText(ReportActivityPlaces.this, "erore" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toasty.success(getApplicationContext(), getResources().getString(R.string.success_toast), Toasty.LENGTH_SHORT).show();
//                Intent intent = new Intent(ReportActivityHoard.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toasty.error(ReportActivityPlaces.this, R.string.error_toast, Toasty.LENGTH_SHORT, true).show();
//                Log.i("MyTAG", t.getMessage());
            }
        });

    }

    //methods that are dependent on APIs
    @NonNull
    private RequestBody createPartFromString(String descriptionPart) {
        return RequestBody.create(MultipartBody.FORM, descriptionPart);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String pathSave, String type, String field) {
        File originalFile = new File(String.valueOf(pathSave));
        RequestBody filePart = RequestBody.create(
                MediaType.parse(type + "/*"),
                originalFile);

        return MultipartBody.Part.createFormData(field, originalFile.getName(), filePart);
    }

    //methods that are dependent on permission
    public void RequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PREMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PREMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(this, R.string.permission, Toast.LENGTH_SHORT).show();
            } else {
                Toasty.warning(this, R.string.permission_not, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //methods that are dependent on spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.activity_report_places_spinner_province){

            String text = adapterView.getItemAtPosition(i).toString();
            tv_province.setText(text);

        }else if (adapterView.getId() == R.id.activity_report_places_spinner_county){

            String text = adapterView.getItemAtPosition(i).toString();
            tv_county.setText(text);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
