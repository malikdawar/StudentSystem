package com.example.pdbsflhr22.studentsystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class HomeDrawer extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {


    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_drawer);

        String gcmSenderID = "936947286396";
        String appVersion = "v1";
        Backendless.initApp( this, "F25B84FF-D689-4AB6-FF8D-A6B451E50C00", "3A96A208-9EE2-2623-FFF8-CCF6F8E6A100", appVersion );
        Backendless.Messaging.registerDevice(gcmSenderID);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
         if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please Click BACK Again to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // NAVIGATION MENU FUNCTION

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.students) {
            startActivity(new Intent(HomeDrawer.this,AllStudents.class));
            //finish();

        } else if (id == R.id.addstudent) {
            startActivity(new Intent(HomeDrawer.this,NewStudent.class));
           // finish();
        } else if (id == R.id.alerts) {
            startActivity(new Intent(HomeDrawer.this,MyAlert.class));
            // finish();
        } else if (id == R.id.exams) {

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(HomeDrawer.this);
            LayoutInflater inflaterr = (LayoutInflater)HomeDrawer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewtemplelayout= inflaterr.inflate(R.layout.customalertterm, null);
            ListView termlist = (ListView)viewtemplelayout.findViewById(R.id.mylist);
            alertdialog.setView(viewtemplelayout);

            final AlertDialog adb=alertdialog.create();

            termlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String val =(String) parent.getItemAtPosition(position);
                    Intent mIntent= new Intent(HomeDrawer.this,Quiz.class);
                    mIntent.putExtra("mTerm", val);
                    startActivity(mIntent);

                    adb.dismiss();

                }
            });
            adb.setView(viewtemplelayout);
            adb.show();

        }else if (id == R.id.addexams) {

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(HomeDrawer.this);
            LayoutInflater inflaterr = (LayoutInflater)HomeDrawer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewtemplelayout= inflaterr.inflate(R.layout.customalertterm, null);
            ListView termlist = (ListView)viewtemplelayout.findViewById(R.id.mylist);
            alertdialog.setView(viewtemplelayout);

            final AlertDialog adb=alertdialog.create();

            termlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String val =(String) parent.getItemAtPosition(position);
                    Intent mIntent= new Intent(HomeDrawer.this,AddExam.class);
                    mIntent.putExtra("mTerm", val);
                    startActivity(mIntent);
                    adb.dismiss();
                    finish();
                }
            });
            adb.setView(viewtemplelayout);
            adb.show();

        } else if (id == R.id.help) {
            alertdiaaboutApp();

        }else if (id == R.id.addalert) {

            startActivity(new Intent(HomeDrawer.this,AddAlert.class));

        } else if (id == R.id.nav_share) {

            try {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "E-Diary");
                String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                String text = "Checkout E-Diary App an Teacher Management Application that helps Teacher, managing their class.\nInstall it Now";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_send) {

            String text = "Checkout E-Diary App an Teacher Management Application that helps Teacher, managing their class.\n" +"Install it Now";
            sendtospec(text,HomeDrawer.this);

        }else if (id == R.id.reset_pwd) {

            Toast.makeText(HomeDrawer.this, "Please Ask Your Admin", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.logout) {
            new PrefManager(HomeDrawer.this).logoutsession();
            logoutserver();
            startActivity(new Intent(HomeDrawer.this,MainActivity.class));
            finish();
        }
        return true;
    }

    public void alertdiaaboutApp(){

        AlertDialog.Builder adb = new AlertDialog.Builder(HomeDrawer.this);
        adb.setIcon(R.drawable.micon);
        adb.setTitle("Help");
        adb.setMessage(R.string.aboutApp);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        adb.show();

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

    public void logoutserver(){

        Backendless.UserService.logout(new AsyncCallback<Void>()
        {
            public void handleResponse( Void response )
            {
                Toast.makeText(HomeDrawer.this,"loged out",Toast.LENGTH_SHORT).show();
                // user has been logged out.
            }

            public void handleFault( BackendlessFault fault )
            {
                // something went wrong and logout failed, to get the error code call fault.getCode()
            }
        });
    }


}
