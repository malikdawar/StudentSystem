package com.example.pdbsflhr22.studentsystem;

/**
 * Created by pdbsflhr22 on 7/15/2017.
 */
import com.backendless.push.BackendlessBroadcastReceiver;

public class MyPushReceiver extends BackendlessBroadcastReceiver
{
    @Override
    public Class getServiceClass()
{
    return MyPushService.class;
}


}