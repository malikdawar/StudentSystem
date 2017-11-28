package com.example.pdbsflhr22.studentsystem;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {

    ImageButton stdcall,stdsms,stdemail,par_email,par_sms,par_call;
    String NamE,EmaiL,NumbeR,RollNuM,SectioN,PEmaiL,PNuM,GendeR;
    ImageView img;
    EditText editTextmassage,editTextmassage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_details);

        NamE = getIntent().getExtras().getString("NamE");
        EmaiL = getIntent().getExtras().getString("EmaiL");
        NumbeR = getIntent().getExtras().getString("NumbeR");
        RollNuM = getIntent().getExtras().getString("RollNuM");
        SectioN = getIntent().getExtras().getString("SectioN");
        PEmaiL = getIntent().getExtras().getString("PEmaiL");
        PNuM = getIntent().getExtras().getString("PNuM");
        GendeR = getIntent().getExtras().getString("GendeR");
        getSupportActionBar().setTitle(NamE+" ("+EmaiL+")");

        img=(ImageView) findViewById(R.id.imageView3);

        stdcall=(ImageButton)findViewById(R.id.callstd);
        stdsms=(ImageButton)findViewById(R.id.msgstd);
        stdemail=(ImageButton)findViewById(R.id.mailstd);

        par_call=(ImageButton)findViewById(R.id.callparent);
        par_sms=(ImageButton)findViewById(R.id.msgparent);
        par_email=(ImageButton)findViewById(R.id.mailparent);

        editTextmassage=(EditText)findViewById(R.id.editTextmassage);
        editTextmassage2=(EditText)findViewById(R.id.editTextmassage2);

        if(GendeR.equals("Male")) {
            img.setImageResource(R.drawable.stdnt);
        }else{
            img.setImageResource(R.drawable.studentgirl);
        }
        stdcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling out the Call function to make an call
                make_call(NumbeR);
            }
        });
        stdsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling out the SMS function to make an SMSon clicking sms ImageView
                sendSMS(editTextmassage.getText().toString(),NumbeR);
                editTextmassage.setText("");
            }
        });

        stdemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling Out Email Function for Email Intent
                sendEmail(EmaiL);
            }
        });

        par_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling out the Call function to make an call on clicking call ImageView
                make_call(PNuM);

            }
        });

        par_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling out the SMS function to make an SMSon clicking sms ImageView
                sendSMS(editTextmassage2.getText().toString(),PNuM);
                editTextmassage2.setText("");

            }
        });

        par_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling Out Email Function for Email Intent
                sendEmail(PEmaiL);
            }
        });





    }

    public void make_call(String phone){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone));

        if (ActivityCompat.checkSelfPermission(Details.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    protected void sendSMS(String massage,String phone) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, massage, null, null);  //  Toast.makeText(getBaseContext(),"Send SMS",Toast.LENGTH_SHORT).show();
            Toast.makeText(Details.this, "Sending Sms.", Toast.LENGTH_SHORT).show();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Details.this, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmail(String emailAddress){
        String[] TO = {""+emailAddress};
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL  ,TO);
        i.putExtra(Intent.EXTRA_SUBJECT, "E-Diary Mail");
       /*  i.putExtra(Intent.EXTRA_TEXT   , "body of email");*/
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Details.this, "Unknown Error.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_info_action:
                ShowDetails();
               // Toast.makeText(Details.this, "hello", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void ShowDetails(){


        final Dialog dialog = new Dialog(Details.this);
        dialog.setContentView(R.layout.detailscustomalert);
        dialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Details</font>"));

        // set the custom dialog components - text, image and button
        TextView customname = (TextView) dialog.findViewById(R.id.customname);
        customname.setText(NamE);

        TextView customemail = (TextView) dialog.findViewById(R.id.customemail);
        customemail.setText(EmaiL);

        TextView customNumb = (TextView) dialog.findViewById(R.id.customNumb);
        customNumb.setText(NumbeR);

        TextView customRollNum = (TextView) dialog.findViewById(R.id.customRollNum);
        customRollNum.setText(RollNuM);

        TextView customSection = (TextView) dialog.findViewById(R.id.customSection);
        customSection.setText(SectioN);

        TextView customGender = (TextView) dialog.findViewById(R.id.customGender);
        customGender.setText(GendeR);

        TextView customPnum = (TextView) dialog.findViewById(R.id.customPnum);
        customPnum.setText(PNuM);

        TextView customPmail = (TextView) dialog.findViewById(R.id.customPmail);
        customPmail.setText(PEmaiL);

        dialog.show();
    }


    //Function for arraow in tab
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
