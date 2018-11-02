package jeedom.gauthier.android.jeedomappmobile.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class JeedomAppGauthier extends Application {

    private static JeedomAppGauthier jeedomAppGauthier;

    private static FirebaseApp firebaseApp;


    @Override
    public void onCreate() {
        super.onCreate();

        jeedomAppGauthier = this;
        firebaseApp = FirebaseApp.getInstance();


    }

    public static JeedomAppGauthier getJeedomAppGauthier () {
        return jeedomAppGauthier;
    }
}
