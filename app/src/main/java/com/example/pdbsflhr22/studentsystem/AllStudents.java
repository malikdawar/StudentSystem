package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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



public class AllStudents extends AppCompatActivity {

    private static final String TAG_NAME = "studentname";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_NUMB = "numb";
    private static final String TAG_ROLLNUM = "rollnum";
    private static final String TAG_SECTION = "section";
    private static final String TAG_PEMAIL = "parentemail";
    private static final String TAG_PNUM = "parentnumb";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_OBJECTID = "objectId";

    ArrayList<HashMap<String, String>> infoList;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressDialog progress;
   // ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_all_students);
        infoList = new ArrayList<HashMap<String, String>>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        String appVersion = "v1";
        Backendless.initApp( this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion );

        findStudents();


        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_dark, android.R.color.holo_purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {

                        infoList.clear();
                        findStudents();
                        Toast.makeText(getApplicationContext(),"Refreshed",Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }

        });


    }

    public void findStudents(){

        progressss(AllStudents.this);

        long startTime = System.currentTimeMillis();
        final CountDownLatch latch = new CountDownLatch( 1 );
        AsyncCallback<BackendlessCollection<Contact>> callback=new AsyncCallback<BackendlessCollection<Contact>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<Contact> students )
            {
                //  List<String> infoList = new ArrayList<>();
                Toast.makeText(AllStudents.this,"Students Found" + students.getTotalObjects(),Toast.LENGTH_SHORT).show();
                Iterator<Contact> iterator=students.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {

                    Contact student=iterator.next();

                    String Name= student.getName();
                    String email=student.getEmail();
                    String numb=student.getPhone();
                    String rollnumb=student.getRollNum();
                    String sect=student.getSection();
                    String pemail=student.getParentEmail();
                    String pnum=student.getParentNum();
                    String gender= student.getGender();
                    String objectId= student.getObjectId();


                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_NAME, Name);
                    map.put(TAG_EMAIL, email);
                    map.put(TAG_NUMB, numb);
                    map.put(TAG_ROLLNUM, rollnumb);
                    map.put(TAG_SECTION, sect);
                    map.put(TAG_PEMAIL, pemail);
                    map.put(TAG_PNUM, pnum);
                    map.put(TAG_GENDER, gender);
                    map.put(TAG_OBJECTID, objectId);

                    infoList.add(map);
                }

                String[]from= {TAG_NAME,TAG_EMAIL,TAG_NUMB };
                int[] to={R.id.lvname,R.id.lvnumb,R.id.lvgroup };

                ListAdapter adapter = new SimpleAdapter(AllStudents.this, infoList,R.layout.listviewxmlui,from,to);
                ListView lv=(ListView)findViewById(R.id.listView);
                lv.setAdapter(adapter);
                progress.dismiss();
                latch.countDown();

                //function on  press
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            //sending intent to detail activity
                            Intent intent = new Intent();
                            intent.setClass(getBaseContext(), Details.class);
                            intent.putExtra("NamE", infoList.get(position).get(TAG_NAME));
                            intent.putExtra("EmaiL", infoList.get(position).get(TAG_EMAIL));
                            intent.putExtra("NumbeR", infoList.get(position).get(TAG_NUMB));
                            intent.putExtra("RollNuM", infoList.get(position).get(TAG_ROLLNUM));
                            intent.putExtra("SectioN", infoList.get(position).get(TAG_SECTION));
                            intent.putExtra("PEmaiL", infoList.get(position).get(TAG_PEMAIL));
                            intent.putExtra("PNuM", infoList.get(position).get(TAG_PNUM));
                            intent.putExtra("GendeR", infoList.get(position).get(TAG_GENDER));
                            startActivity(intent);
                        }catch (NullPointerException ex){Toast.makeText(getBaseContext(),"please check you Connection",Toast.LENGTH_SHORT).show();}
                    }
                });

                //function on long press
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        HashMap contact = new HashMap();
                        contact.put("objectId", infoList.get(position).get(TAG_OBJECTID));
                        deleteContact(contact);
                        return true;
                    }
                });

            }
            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(AllStudents.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }};
        Backendless.Data.of( Contact.class ).find( callback );
    }

    //function to delete student
    public void deleteContact(final Map stdObjId) {

        Backendless.Persistence.of("Contact").remove(stdObjId, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                // Contact objectdhas been deleted
                //     Log.d("savedContact",savedContact+"   ");
                Toast.makeText(AllStudents.this, "Deleted", Toast.LENGTH_SHORT).show();
                infoList.clear();
                findStudents();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(AllStudents.this, "Bad Request", Toast.LENGTH_SHORT).show();

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