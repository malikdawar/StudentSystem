package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    EditText Lemail,Lpassword;
    TextView tv_SignUp;
    ProgressDialog progress;
    Button loginbtn;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appVersion = "v1";
        Backendless.initApp( this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion );

        tv_SignUp=(TextView)findViewById(R.id.tv_SignUp);
        Lemail=(EditText) findViewById(R.id.Lemail);
        Lpassword=(EditText) findViewById(R.id.Lpassword);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        tv_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,Registration.class);
                startActivity(i);
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling custom login function
                simplelogin();
                //  rememberme();

            }
        });


        if (!new PrefManager(this).isUserLogedOut()) {
            //user's email and password both are saved in preferences
            startHomeActivity();
        }
    }

    //Custom Functions
    private void rememberme() {

        // Store values at the time of the login attempt.
        String email = Lemail.getText().toString();
        String password = Lpassword.getText().toString();
        // save data in local shared preferences
        if (checkBoxRememberMe.isChecked()) {
            saveLoginDetails(email, password);
            startHomeActivity();
        }
    }


    private void saveLoginDetails(String email, String password) {
        new PrefManager(this).saveLoginDetails(email, password);
    }


    //custom login function
    public void simplelogin(){
        progress = new ProgressDialog(MainActivity.this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        if(!Lemail.getText().toString().equals("") && !Lpassword.getText().toString().equals("")) {

            Backendless.UserService.login(Lemail.getText().toString(), Lpassword.getText().toString(), new AsyncCallback<BackendlessUser>() {
                public void handleResponse(BackendlessUser user) {

                    Toast.makeText(getBaseContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    rememberme();
                    progress.dismiss();
                    startHomeActivity();

                }

                public void handleFault(BackendlessFault fault) {

                    progress.dismiss();
                    // login failed, to get the error code call fault.getCode()
                    Toast.makeText(getBaseContext(),  "Error :" +fault.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        else  if(Lemail.getText().toString().equals("") || Lpassword.getText().toString().equals("")) {
            progress.dismiss();
            Toast.makeText(getApplicationContext(),"Please Fill the email & Password",Toast.LENGTH_SHORT).show();

        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeDrawer.class);
        startActivity(intent);
        finish();
    }

}
