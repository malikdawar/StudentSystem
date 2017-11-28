package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Registration extends AppCompatActivity {


    private EditText ed1, ed2, ed3;
    private Button btnSignUp;
    ProgressDialog progress;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String appVersion = "v1";
        Backendless.initApp( this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion );

        ed1 = (EditText) findViewById(R.id.name);
        ed2 = (EditText) findViewById(R.id.email);
        ed3 = (EditText) findViewById(R.id.password);
        tv_login = (TextView) findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginscr();
            }
        });
        btnSignUp = (Button) findViewById(R.id.button_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customsignup();
            }
        });
    }

    public void loginscr(){

        Intent logginn= new Intent(Registration.this,MainActivity.class);
        startActivity(logginn);
        finish();
    }

    public void customsignup(){
        progress = new ProgressDialog(Registration.this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        if(!ed1.getText().toString().equals("") && !ed2.getText().toString().equals("")  && !ed3.getText().toString().equals("")) {
            BackendlessUser user = new BackendlessUser();
            user.setProperty("name", ed1.getText().toString());
            user.setProperty("email", ed2.getText().toString());
            user.setProperty("Role", "Teacher");
            user.setProperty("dummyPwd", ed3.getText().toString());
            user.setPassword(ed3.getText().toString());

            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                public void handleResponse(BackendlessUser registeredUser) {
                    Toast.makeText(Registration.this, "Registered Successfully Please check your mail and then login ", Toast.LENGTH_SHORT).show();
                    loginscr();
                    progress.dismiss();
                }

                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(Registration.this, "Error Occoured Please check Your Network or Try Again Later", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(MainActivity.this,ed1.getText().toString()+ed2.getText().toString()+ed3.getText().toString()+bloodgroup.getSelectedItem().toString()+city.getSelectedItem().toString()+gender+ numb.getText().toString() , Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            });
        }
        else if(ed1.getText().toString().equals("") || ed2.getText().toString().equals("")  || ed3.getText().toString().equals("")){
            Toast.makeText(getBaseContext(),"Please Fill All the Fields",Toast.LENGTH_SHORT).show();
        }
    }
}
