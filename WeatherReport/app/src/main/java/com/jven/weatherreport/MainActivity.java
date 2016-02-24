package com.jven.weatherreport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Jven on 24/2/2016.
 */
public class MainActivity extends Activity {
    private static final String TAG = "WeatherReport.Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        WeatherFromWeb.Test();
    }
}
