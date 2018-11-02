package jeedom.gauthier.android.jeedomappmobile.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class SmartHouse extends Application {

    private static SmartHouse smartHouse;

    private static FirebaseApp firebaseApp;


    @Override
    public void onCreate() {
        super.onCreate();

        smartHouse = this;
        firebaseApp = FirebaseApp.getInstance();
        
    }

    public static SmartHouse getSmartHouse () {
        return smartHouse;
    }
}
