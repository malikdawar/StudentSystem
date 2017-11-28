package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Quiz extends AppCompatActivity {

    String mterm;
    ProgressDialog progress;
    ListView lv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> examList;
    private static final String TAG_STUDENTID = "studentid";
    private static final String TAG_TMARKS = "totalmarks";
    private static final String TAG_STATUS = "status";
    private static final String TAG_OBJECTID = "objectid";
    private static final String TAG_CREATEDBY = "createdby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mterm = getIntent().getExtras().getString("mTerm");
        getSupportActionBar().setTitle(mterm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.exams_list);

        examList = new ArrayList<HashMap<String, String>>();

        lv = (ListView) findViewById(R.id.listView3);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        String appVersion = "v1";
        Backendless.initApp(this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion);

        switch(mterm) {
            case "First_Term":
                findMArkSheetFirst();
                break;

            case "Second_Term":
                findMArkSheetSecond();
                break;

            case "Final_Term":
                findMArkSheetThird();
                break;
        }

        //findMArkSheet();


        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_dark, android.R.color.holo_purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {

                        examList.clear();
                        switch(mterm) {
                            case "First_Term":
                                findMArkSheetFirst();
                                break;

                            case "Second_Term":
                                findMArkSheetSecond();
                                break;

                            case "Final_Term":
                                findMArkSheetThird();
                                break;
                        }

                        Toast.makeText(getApplicationContext(),"Online Data Refreshed",Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Quiz.this,examList.get(position).get(TAG_STATUS) +"\n"+examList.get(position).get(TAG_CREATEDBY), Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap contact = new HashMap();
                contact.put("objectId", examList.get(position).get(TAG_OBJECTID));
                deleteReport(contact);
                return true;
            }
        });


    }


    //delete data

    public void deleteReport(final Map stdObjId) {

        Backendless.Persistence.of(mterm).remove(stdObjId, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                // Contact objectdhas been deleted
                //     Log.d("savedContact",savedContact+"   ");
                Toast.makeText(Quiz.this, "Deleted", Toast.LENGTH_SHORT).show();
                examList.clear();
                switch(mterm) {
                    case "First_Term":
                        findMArkSheetFirst();
                        break;

                    case "Second_Term":
                        findMArkSheetSecond();
                        break;

                    case "Final_Term":
                        findMArkSheetThird();
                        break;
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(Quiz.this, "Bad Request", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //first term data

    public void findMArkSheetFirst(){

        progressss(Quiz.this);

        final CountDownLatch latch = new CountDownLatch( 1 );
        AsyncCallback<BackendlessCollection<First_Term>> callback=new AsyncCallback<BackendlessCollection<First_Term>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<First_Term> reports )
            {

                Iterator<First_Term> iterator=reports.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    First_Term marks=iterator.next();

                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_STUDENTID, marks.getStudentIdentity());
                    map.put(TAG_TMARKS, marks.getTmarks());
                    map.put(TAG_STATUS,"("+marks.getTmarks()+") marks \t"+marks.getStatus());
                    map.put(TAG_OBJECTID, marks.getObjectId());
                    map.put(TAG_CREATEDBY,"Checker "+ marks.getCreated_by());
                    examList.add(map);
                }

                String[]from= {TAG_STUDENTID,TAG_STATUS,TAG_CREATEDBY };
                int[] to={R.id.lvtitle,R.id.lvalert,R.id.lvcreated };

                ListAdapter adapter = new SimpleAdapter(Quiz.this, examList,R.layout.listviewxmlui3,from,to);

                lv.setAdapter(adapter);

                progress.dismiss();
                if(examList.size()==0){

                    Toast.makeText(Quiz.this, "No DATA FOUND", Toast.LENGTH_SHORT).show();
                }
                latch.countDown();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(Quiz.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        };
        Backendless.Data.of( First_Term.class ).find( callback );

    }
    //2nd term data

    public void findMArkSheetSecond(){

        progressss(Quiz.this);
/*       long startTime = System.currentTimeMillis();*/
        final CountDownLatch latch = new CountDownLatch( 1 );
        AsyncCallback<BackendlessCollection<Second_Term>> callback=new AsyncCallback<BackendlessCollection<Second_Term>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<Second_Term> reports )
            {
                Iterator<Second_Term> iterator=reports.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    Second_Term marks=iterator.next();

                    HashMap<String, String> map = new HashMap<String, String>();


                    // adding each child node to HashMap key => value
                    map.put(TAG_STUDENTID, marks.getStudentIdentity());
                    map.put(TAG_TMARKS, marks.getTmarks());
                    map.put(TAG_STATUS,"("+marks.getTmarks()+") marks \t"+marks.getStatus());
                    map.put(TAG_OBJECTID, marks.getObjectId());
                    map.put(TAG_CREATEDBY,"Checker "+ marks.getCreated_by());
                    examList.add(map);
                }

                String[]from= {TAG_STUDENTID,TAG_STATUS,TAG_CREATEDBY };
                int[] to={R.id.lvtitle,R.id.lvalert,R.id.lvcreated };

                ListAdapter adapter = new SimpleAdapter(Quiz.this, examList,R.layout.listviewxmlui3,from,to);

                lv.setAdapter(adapter);
                progress.dismiss();
                if(examList.size()==0){

                    Toast.makeText(Quiz.this, "No DATA FOUND", Toast.LENGTH_SHORT).show();
                }
                latch.countDown();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(Quiz.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        };
        Backendless.Data.of( Second_Term.class ).find( callback );

    }

    //3rd term data

    public void findMArkSheetThird(){

        progressss(Quiz.this);
/*       long startTime = System.currentTimeMillis();*/
        final CountDownLatch latch = new CountDownLatch( 1 );
        AsyncCallback<BackendlessCollection<Final_Term>> callback=new AsyncCallback<BackendlessCollection<Final_Term>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<Final_Term> reports )
            {
                Iterator<Final_Term> iterator=reports.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    Final_Term marks=iterator.next();


                    HashMap<String, String> map = new HashMap<String, String>();


                    // adding each child node to HashMap key => value
                    map.put(TAG_STUDENTID, marks.getStudentIdentity());
                    map.put(TAG_TMARKS, marks.getTmarks());
                    map.put(TAG_STATUS,"("+marks.getTmarks()+") marks \t"+marks.getStatus());
                    map.put(TAG_OBJECTID, marks.getObjectId());
                    map.put(TAG_CREATEDBY,"Checker "+ marks.getCreated_by());
                    examList.add(map);
                }

                String[]from= {TAG_STUDENTID,TAG_STATUS,TAG_CREATEDBY };
                int[] to={R.id.lvtitle,R.id.lvalert,R.id.lvcreated };

                ListAdapter adapter = new SimpleAdapter(Quiz.this, examList,R.layout.listviewxmlui3,from,to);

                lv.setAdapter(adapter);
                progress.dismiss();
                if(examList.size()==0){

                    Toast.makeText(Quiz.this, "No DATA FOUND", Toast.LENGTH_SHORT).show();
                }
                latch.countDown();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Toast.makeText(Quiz.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        };
        Backendless.Data.of( Final_Term.class ).find( callback );

    }


    public void progressss(Context mcontext){
        progress = new ProgressDialog(mcontext);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_info_action:

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(Quiz.this);
                LayoutInflater inflaterr = (LayoutInflater)Quiz.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewtemplelayout= inflaterr.inflate(R.layout.customalertterm, null);
                ListView termlist = (ListView)viewtemplelayout.findViewById(R.id.mylist);
                alertdialog.setView(viewtemplelayout);

                final AlertDialog adb=alertdialog.create();

                termlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String val =(String) parent.getItemAtPosition(position);
                        Intent mIntent= new Intent(Quiz.this,AddExam.class);
                        mIntent.putExtra("mTerm", val);
                        startActivity(mIntent);
                        adb.dismiss();
                    }
                });
                adb.setView(viewtemplelayout);
                adb.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Function for arraow in tab
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
