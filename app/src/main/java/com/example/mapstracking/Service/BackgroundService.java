package com.example.mapstracking.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.mapstracking.Method;

public class BackgroundService extends JobService {

    int count = 0;

    @Override
    public boolean onStartJob(JobParameters params) {
        backgroundService(params);
        return true;
    }

    private void backgroundService(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(count == 0){
                    Method.getCurrentLocation();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jobFinished(params, false);
            }
        }).start();
        /*
        final Handler handler = new Handler();
        final int count = 0;
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                MainActivity.getCurrentLocation();
                if(count != 1) {
                    handler.postDelayed(this, 3000);
                }
            }
        };
        */
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
