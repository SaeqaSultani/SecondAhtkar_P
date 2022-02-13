package com.example.women.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.women.R;
import com.example.women.networking_senddata.ApiClient;
import com.example.women.networking_senddata.ApiInterfaceSendReport;
import com.example.women.networking_senddata.ClassSignup;
import com.example.women.util.MySharedPreferences;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //views
    private Button button;
    private EditText etName, etLastName,etPhone,etplace,et_password;
    private TextView tv_load;
    private ProgressBar progressBar;
    private Spinner sp_province,sp_county;
    private TextView tv_county,tv_province;
    Handler handler;
    Runnable runnable;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_singup);

        initToolbar();
        findView();


        //change the language of App white isRtl method
        String languageToLoad = "fa";
        Locale locale = new Locale(languageToLoad);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        //spinnerAdapters
        ArrayAdapter<CharSequence> arrayAdapter_1 = ArrayAdapter.createFromResource(this, R.array.array_province, android.R.layout.simple_spinner_item);
        arrayAdapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_province.setAdapter(arrayAdapter_1);
        sp_province.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> arrayAdapter_2 = ArrayAdapter.createFromResource(this, R.array.array_county, android.R.layout.simple_spinner_item);
        arrayAdapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_county.setAdapter(arrayAdapter_2);
        sp_county.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = etName.getText().toString();
                String last_name = etLastName.getText().toString();
                String province = tv_province.getText().toString();
                String phone = etPhone.getText().toString();
                String password = et_password.getText().toString();
                String county = tv_county.getText().toString();
                String place = etplace.getText().toString();


                if (name.isEmpty()) {
                    etName.setError(getString(R.string.write_name));
                    etName.findFocus();
                    return;
                }
                if (last_name.isEmpty()) {
                    etLastName.setError(getString(R.string.write_last_name));
                    etLastName.findFocus();
                    return;
                }
                if (phone.length() == 9){
                    etPhone.setError(getString(R.string.write_phone_number));
                    etPhone.findFocus();
                    return;
                }
                if (password.length() < 6){
                    et_password.setError(getString(R.string.write_password));
                    et_password.findFocus();
                    return;
                }


                if (sp_province.getSelectedItemPosition() == 0) {
                    Toasty.warning(SingupActivity.this, getString(R.string.choose_province), Toasty.LENGTH_SHORT).show();
                    return;
                }

                if (sp_county.getSelectedItemPosition() == 0){
                    Toasty.warning(SingupActivity.this, getString(R.string.choose_county), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (place.isEmpty()) {
                    etplace.setError(getString(R.string.write_place));
                    etplace.findFocus();
                    return;
                }

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
                },3000,1000);



                ApiInterfaceSendReport apiInterface = ApiClient.getApiClient().create(ApiInterfaceSendReport.class);
                Call<ResponseBody> call = apiInterface.signupMethod(
                        createPartFromString(etName.getText().toString()),
                        createPartFromString(etLastName.getText().toString()),
                        createPartFromString(tv_province.getText().toString()),
                        createPartFromString(tv_county.getText().toString()),
                        createPartFromString(etplace.getText().toString()),
                        createPartFromString(etPhone.getText().toString()),
                        createPartFromString(et_password.getText().toString()));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (!response.isSuccessful()) {

                            Toasty.warning(SingupActivity.this, "Sever problem : please check your phoneNumber", Toast.LENGTH_SHORT, true).show();

//                            Toast.makeText(SingupActivity.this, "the error code is : "+response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        Toast.makeText(SignupPageActivity.this, "server response is success" + modelClass.getResponse(), Toast.LENGTH_SHORT).show();
                        Toasty.success(SingupActivity.this, R.string.success_toast, Toast.LENGTH_SHORT, true).show();


                        Intent intent = new Intent(SingupActivity.this,LoginPageActivity.class);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Toasty.error(SingupActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                        Toast.makeText(SingupActivity.this, "server field "+ t, Toast.LENGTH_SHORT).show();
//                        Log.i("TAGS", "onFailure: " + t);

                    }
                });

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void findView(){
        etName = findViewById(R.id.activity_Signup_page_EditText_name);
        etLastName = findViewById(R.id.activity_Signup_page_EditText_lastname);
        etPhone = findViewById(R.id.activity_Signup_page_EditText_phone_number);
        etplace = findViewById(R.id.activity_Signup_page_EditText_place);
        et_password = findViewById(R.id.activity_Signup_page_EditText_password);
        sp_province = findViewById(R.id.spinner_province);
        sp_county = findViewById(R.id.spinner_county);
        tv_county = findViewById(R.id.activity_Signup_page_TextView_county);
        tv_province = findViewById(R.id.activity_Signup_page_TextView_province);
        progressBar = findViewById(R.id.spin_kit);
        tv_load = findViewById(R.id.text_view_load);
        button = findViewById(R.id.btn_sign_up);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.spinner_province){

            String text = adapterView.getItemAtPosition(i).toString();
            tv_province.setText(text);

        }else if (adapterView.getId() == R.id.spinner_county){

            String text = adapterView.getItemAtPosition(i).toString();
            tv_county.setText(text);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //method that is dependent on APIs
    @NonNull
    private RequestBody createPartFromString(String descriptionPart) {
        return RequestBody.create(MultipartBody.FORM, descriptionPart);
    }

}
