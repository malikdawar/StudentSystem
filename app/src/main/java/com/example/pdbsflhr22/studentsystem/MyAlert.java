package com.example.pdbsflhr22.studentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class MyAlert extends AppCompatActivity {
    AlertDialog alertmenu;

    EditText ed_title, ed_discp;
    ProgressDialog progress;
    ListView lv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> alertList;
    private static final String TAG_TITLE = "title";
    private static final String TAG_ALERT = "announcement";
    private static final String TAG_CREATED = "created";
    private static final String TAG_OBJECTID = "objectid";
    private static final String TAG_CREATEDBY = "createdby";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //toolbar
        getSupportActionBar().setTitle("Announcement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //calling view
        setContentView(R.layout.activity_my_alert);
        lv = (ListView) findViewById(R.id.listView2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        String appVersion = "v1";
        Backendless.initApp(this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion);


        findAlerts();


        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_dark, android.R.color.holo_purple);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {

                        alertList.clear();
                        findAlerts();
                        Toast.makeText(getApplicationContext(),"Refreshed",Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }

        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {

                    AlertDialog.Builder adb=new AlertDialog.Builder(MyAlert.this);
                    adb.setIcon(R.drawable.announcementicon);
                    adb.setTitle(alertList.get(position).get(TAG_TITLE));
                    adb.setMessage("Created "+alertList.get(position).get(TAG_CREATED)+"\n"+alertList.get(position).get(TAG_ALERT)+"\n BY:"+alertList.get(position).get(TAG_CREATEDBY));

                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    adb.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String text=alertList.get(position).get(TAG_CREATEDBY)+" Said that "+alertList.get(position).get(TAG_TITLE)+" "+alertList.get(position).get(TAG_ALERT);
                            sendtospec(text,MyAlert.this);
                        }
                    });
                    adb.show();
                } catch (NullPointerException ex) {
                    Toast.makeText(getBaseContext(), "please check you Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getBaseContext(),"Deleted",Toast.LENGTH_SHORT).show();
                HashMap announcement = new HashMap();
                announcement.put("objectId", alertList.get(position).get(TAG_OBJECTID));
                deleteContact(announcement);
                return true;
            }
        });

    }



    public void findAlerts(){

       progressss(MyAlert.this);
/*       long startTime = System.currentTimeMillis();*/
       final CountDownLatch latch = new CountDownLatch( 1 );
       AsyncCallback<BackendlessCollection<Announcement>> callback=new AsyncCallback<BackendlessCollection<Announcement>>()
       {
           @Override
           public void handleResponse( BackendlessCollection<Announcement> students )
           {
               alertList = new ArrayList<HashMap<String, String>>();

              // Toast.makeText(MyAlert.this, students.getTotalObjects()+"Alerts Found",Toast.LENGTH_SHORT).show();
               Iterator<Announcement> iterator=students.getCurrentPage().iterator();
               while( iterator.hasNext() )
               {
                   Announcement student=iterator.next();

                   String title= student.getTitle();
                   String announcemnt=student.getAnnouncement();
                   String created=student.getCreatedAt();
                   String objectid=student.getObjectId();
                   String created_by=student.getAnnouncementBY();

                   HashMap<String, String> map = new HashMap<String, String>();

                   // adding each child node to HashMap key => value
                   map.put(TAG_TITLE, title);
                   map.put(TAG_ALERT, announcemnt);
                   map.put(TAG_CREATED," @"+created);
                   map.put(TAG_OBJECTID, objectid);
                   map.put(TAG_CREATEDBY, created_by);
                   // String info = Name + "\n" + numb+"\n"+bloodgroup ;
                   alertList.add(map);
               }

               String[]from= {TAG_TITLE,TAG_ALERT,TAG_CREATED ,TAG_CREATEDBY};
               int[] to={R.id.lvtitle,R.id.lvalert,R.id.lvcreated ,R.id.lvcreatedby};

               ListAdapter adapter = new SimpleAdapter(MyAlert.this, alertList,R.layout.listviewxmlui2,from,to);

               lv.setAdapter(adapter);
               progress.dismiss();
               latch.countDown();
           }

           @Override
           public void handleFault( BackendlessFault backendlessFault )
           {
               Toast.makeText(MyAlert.this,"Error " + backendlessFault.getMessage(),Toast.LENGTH_LONG).show();
              progress.dismiss();
           }
       };
       Backendless.Data.of( Announcement.class ).find( callback );

   }


    //function to delete student
    public void deleteContact(final Map announcement) {

        Backendless.Persistence.of("Announcement").remove(announcement, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                Toast.makeText(MyAlert.this, "Deleted", Toast.LENGTH_SHORT).show();
                alertList.clear();
                findAlerts();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(MyAlert.this, "Bad Request", Toast.LENGTH_SHORT).show();
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


    public  void sendtospec(String text, Context mContext){
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            waIntent.setPackage("com.whatsapp");
            if (waIntent != null) {
                waIntent.putExtra(Intent.EXTRA_TEXT, text);//
                startActivity(Intent.createChooser(waIntent, "Share with"));
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(mContext, "WhatsApp not Installed",Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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
                startActivity(new Intent(MyAlert.this,AddAlert.class));
               // finish();
                // Toast.makeText(MyAlert.this, "hello", Toast.LENGTH_SHORT).show();
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
