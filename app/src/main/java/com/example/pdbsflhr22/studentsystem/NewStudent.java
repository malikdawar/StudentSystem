package com.example.pdbsflhr22.studentsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NewStudent extends AppCompatActivity {

    EditText stdfname,stdemail,stdnumb,stdRollnum,stdSec,parentEmail,parentNum;
    Button registerStudent;
    Spinner gender;
    ProgressDialog progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

         // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_new_student);
        String appVersion = "v1";
        Backendless.initApp( this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion );

        stdfname = (EditText) findViewById(R.id.stdfname);
        stdemail = (EditText) findViewById(R.id.stdemail);
        stdnumb = (EditText) findViewById(R.id.stdnumb);
        stdRollnum = (EditText) findViewById(R.id.stdRollnum);
        stdSec = (EditText) findViewById(R.id.stdSec);
        parentEmail = (EditText) findViewById(R.id.parentEmail);
        parentNum = (EditText) findViewById(R.id.parentNum);

        gender = (Spinner) findViewById(R.id.genderr);

        registerStudent = (Button) findViewById(R.id.registerStudent);
        registerStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean fieldsOK=validate(new EditText[]{stdfname, stdSec, stdRollnum});

                if (fieldsOK==false) {
                    Toast.makeText(getBaseContext(),"Please Fill Enter Your details",Toast.LENGTH_SHORT).show();
                }else {
                     boolean numOK=isValidMobile(new EditText[]{stdnumb, parentNum});
                     boolean mailOK=isValidMail(new EditText[]{stdemail, parentEmail});

                    if (numOK==false) {
                        Toast.makeText(getBaseContext(),"Phone Format is not valid",Toast.LENGTH_SHORT).show();
                    }else if(mailOK==false) {
                        Toast.makeText(getBaseContext(),"Email Format is not valid",Toast.LENGTH_SHORT).show();
                    }else {
                        // adding student
                        saveNewStudentLogin();
                    }
                    //saveNewStudent();
                }
            }
        });

    }

    //saveNewStudent Function to save a student to server

    public void saveNewStudent() {

        HashMap contact = new HashMap();
        contact.put("studentname", stdfname.getText().toString());
        contact.put("email", stdemail.getText().toString());
        contact.put("numb", stdnumb.getText().toString());
        contact.put("rollnum", stdRollnum.getText().toString());
        contact.put("section", stdSec.getText().toString());
        contact.put("parentemail", parentEmail.getText().toString());
        contact.put("parentnumb", parentNum.getText().toString());
        contact.put("gender", gender.getSelectedItem().toString());

        // save object asynchronously
        Backendless.Persistence.of("Contact").save(contact, new AsyncCallback<Map>() {
            public void handleResponse(Map response) {

                // new Contact instance has been saved
                Toast.makeText(NewStudent.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NewStudent.this, NewStudent.class));
                finish();
            }

            public void handleFault(BackendlessFault fault) {
            }
        });
    }


    public void saveNewStudentLogin() {

        progressbar = new ProgressDialog(NewStudent.this);
        progressbar.setTitle("Please Wait!!");
        progressbar.setMessage("Wait!!");
        progressbar.setCancelable(true);
        progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressbar.show();

            BackendlessUser user = new BackendlessUser();
            user.setProperty("name",stdfname.getText().toString());
            user.setProperty("email",stdemail.getText().toString());
            user.setProperty("Role", "Student");
            user.setProperty("userStatus", "ENABLED");
            user.setProperty("dummyPwd",stdfname.getText().toString() );
            user.setPassword(stdfname.getText().toString());

            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                public void handleResponse(BackendlessUser registeredUser) {
                    saveNewStudent();
                    progressbar.dismiss();
                   // Toast.makeText(NewStudent.this, "Registered Successfully Please check your mail and then login ", Toast.LENGTH_SHORT).show();
                }
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(NewStudent.this, "Not registered "+fault, Toast.LENGTH_SHORT).show();
                    progressbar.dismiss();
                }
            });
        }


    //validation function to check either fileds are empty or filled

    public boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }

    //Phone Number Validation
    private boolean isValidMobile(EditText[] fields) {

        for(int i=0; i<fields.length; i++) {
            EditText currentField=fields[i];
            String phone=currentField.getText().toString();

            if (phone.length() != 11 ) {
                return false;
            }
        }
        return true;
    }

    //Email VAlidation
    private boolean isValidMail(EditText[] fields) {
         String email = null;
        for(int i=0; i<fields.length; i++) {
            EditText currentField = fields[i];
           email = currentField.getText().toString();
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    //Function for arraow in tab
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
