package com.portmone.sampleapp_v3;

import android.app.Application;

import com.portmone.ecomsdk.PortmoneSDK;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        PortmoneSDK.init(this);
    }
}
