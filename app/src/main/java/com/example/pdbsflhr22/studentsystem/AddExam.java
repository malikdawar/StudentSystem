package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AddExam extends AppCompatActivity {

    ProgressDialog progress;
    ArrayList<HashMap<String, String>> infoList;
    private static final String TAG_NAME = "studentname";
    EditText sub1,sub2,sub3,sub4,sub5,sub6,remarks,sudname;
    Button bt1,bt2;
    String mterm=null;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    Spinner spinnerVAr;
    String spinnerVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mterm = getIntent().getExtras().getString("mTerm");
        getSupportActionBar().setTitle(mterm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_exam);

        infoList = new ArrayList<HashMap<String, String>>();

        sudname=(EditText)findViewById(R.id.sudname);
        sub1=(EditText)findViewById(R.id.sub1);
        sub2=(EditText)findViewById(R.id.sub2);
        sub3=(EditText)findViewById(R.id.sub3);
        sub4=(EditText)findViewById(R.id.sub4);
        sub5=(EditText)findViewById(R.id.sub5);
        sub6=(EditText)findViewById(R.id.sub6);
        remarks=(EditText)findViewById(R.id.remarks);

        spinnerVAr=(Spinner)findViewById(R.id.studentIdentity);

        radioGroup = (RadioGroup) findViewById(R.id.radiogp);

        bt1=(Button) findViewById(R.id.status);
        bt2=(Button)findViewById(R.id.save_record);
        findStudents();

        spinnerVAr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(AddExam.this, ""+infoList.get(position).get(TAG_NAME).toString(), Toast.LENGTH_SHORT).show();
                spinnerVal= infoList.get(position).get(TAG_NAME).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean chkEmpty=validate(new EditText[]{sub1, sub2,sub3,sub4,sub5,sub6});
                if(chkEmpty==true) {
                    boolean ValidMarks=isValidMarks(new EditText[]{sub1,sub2,sub3,sub4,sub5,sub6});
                    if(ValidMarks==true){

                        AlertDialog.Builder adb=new AlertDialog.Builder(AddExam.this);
                        adb.setTitle(mterm+"T.Marks : 600");
                        adb.setIcon(R.drawable.micon);
                        adb.setMessage(ObtainedMarks()+"/600 \n"+calculateStatus());
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        adb.setPositiveButton("Save & Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                // upload the result
                                saveResult();
                            }
                        });
                        adb.show();
                    }else{ Toast.makeText(AddExam.this, "Enter Valid Marks", Toast.LENGTH_SHORT).show();}
                }else {
                    Toast.makeText(AddExam.this, "Field can't be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Email VAlidation
    private boolean isValidMarks(EditText[] fields) {
        String marks = null;
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            marks = currentField.getText().toString();
            if(Integer.parseInt(marks)>100) {
                return false;
            }
        }
        return true;
    }

    public boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }

    public String calculateStatus(){
        String result="Pass";
        float Total_MArks=600;
        float AvG;

        AvG=(ObtainedMarks()/Total_MArks*100);
                if(AvG<33.33){
                    return "Fail";
                }

        return result;
    }

    public float ObtainedMarks(){
        float ObtainedMarks,s1,s2,s3,s4,s5,s6;
        s1=Float.parseFloat(sub1.getText().toString());
        s2=Float.parseFloat(sub2.getText().toString());
        s3=Float.parseFloat(sub3.getText().toString());
        s4=Float.parseFloat(sub4.getText().toString());
        s5=Float.parseFloat(sub5.getText().toString());
        s6=Float.parseFloat(sub6.getText().toString());
        ObtainedMarks=s1+s2+s3+s4+s5+s6;

        return ObtainedMarks;
    }

//populating student name
    public void findStudents(){

        progressss(AddExam.this);
        final CountDownLatch latch = new CountDownLatch( 1 );
        AsyncCallback<BackendlessCollection<Contact>> callback=new AsyncCallback<BackendlessCollection<Contact>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<Contact> students )
            {
                Iterator<Contact> iterator=students.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    Contact student=iterator.next();
                    String Name= student.getName();
                    String rollnumb=student.getRollNum();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME, Name);
                    infoList.add(map);
                }
                String[]from= {TAG_NAME };
                int[] to={R.id.studentui, };
                SimpleAdapter adapter = new SimpleAdapter(AddExam.this, infoList,R.layout.mspinnerui,from,to);
                spinnerVAr.setAdapter(adapter);
                progress.dismiss();
                latch.countDown();

            }
            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(AddExam.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }};
        Backendless.Data.of( Contact.class ).find( callback );
    }


    public void progressss(Context mcontext){
        progress = new ProgressDialog(mcontext);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }


    public void saveResult(){

        progressss(AddExam.this);
        PrefManager shared = new PrefManager(AddExam.this);


        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);

        final HashMap results = new HashMap();
        results.put("studentIdentity",spinnerVal);
        results.put("english_A",sub1.getText().toString());
        results.put("pakstudies",sub2.getText().toString());
        results.put("urdu",sub3.getText().toString());
        results.put("maths",sub4.getText().toString());
        results.put("islamiyat",sub5.getText().toString());
        results.put("elective",sub6.getText().toString());
        results.put("tmarks",ObtainedMarks()+"");
        results.put("status",calculateStatus());
        results.put("attendence",radioButton.getText().toString());
        results.put("remarks",remarks.getText().toString());
        results.put("created_by",shared.getEmail());


        // save object asynchronously
        Backendless.Persistence.of(mterm).save(results, new AsyncCallback<Map>() {
            public void handleResponse(Map response) {
                progress.dismiss();
                // new Contact instance has been saved
                Toast.makeText(AddExam.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                Intent in=new Intent(AddExam.this,AddExam.class);
                in.putExtra("mTerm", mterm);
                startActivity(in);
                finish();

            }
            public void handleFault(BackendlessFault fault) {
                progress.dismiss();
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(AddExam.this, "Sorry Bad Request Try Again Later"+fault, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed(){
       startActivity(new Intent(AddExam.this,HomeDrawer.class));
        finish();
    }

    //Function for arraow in tab
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
