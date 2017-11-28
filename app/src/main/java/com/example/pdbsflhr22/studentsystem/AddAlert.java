package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

public class AddAlert extends AppCompatActivity {

    EditText ed_title,ed_discp;
    Button Savealert;
    TextView ed_by;
    ProgressDialog progress;
    PrefManager shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("New Announcement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_alert);

        shared = new PrefManager(AddAlert.this);
        ed_title = (EditText)findViewById(R.id.ed_title);
        ed_discp = (EditText)findViewById(R.id.ed_discp);
        ed_by = (TextView)findViewById(R.id.ed_by);
        Savealert = (Button)findViewById(R.id.Btn_add);

        ed_by.setText("By: "+shared.getEmail());

        Savealert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewStudent std= new NewStudent();
                boolean fieldsOK=std.validate(new EditText[]{ed_title, ed_discp});
                if (fieldsOK==false) {
                    Toast.makeText(getBaseContext(),"Please Fill Enter Your details",Toast.LENGTH_SHORT).show();
                }else {

                    //Calling function to save announcement/alert
                    saveNewAlert(ed_title.getText().toString(),ed_discp.getText().toString(),ed_by.getText().toString());
                    // Toast.makeText(getBaseContext(),"Good JOb",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveNewAlert(String title, String disp,String created_by){

        progressss(AddAlert.this);

        HashMap announcement = new HashMap();
        announcement.put("Title", title);
        announcement.put("Announcement",disp);
        announcement.put("created_by",shared.getEmail());


        // save object asynchronously
        Backendless.Persistence.of("Announcement").save(announcement, new AsyncCallback<Map>() {
            public void handleResponse(Map response) {
                progress.dismiss();
                // new Contact instance has been saved
                Toast.makeText(AddAlert.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            public void handleFault(BackendlessFault fault) {
                progress.dismiss();
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(AddAlert.this, "Sorry Bad Request Try Again Later", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void progressss(Context mcontext){
        progress = new ProgressDialog(mcontext);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }


    //Function for arraow in tab
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
