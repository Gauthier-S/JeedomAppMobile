package jeedom.gauthier.android.jeedomappmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SmartHouseMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 123;
    private final String TAG = this.getClass().getName();
    private final List<AuthUI.IdpConfig> providers = new ArrayList<>();


    private MenuItem login;
    private MenuItem logout;

    private ImageView photo;
    private TextView name;
    private TextView email;

    private FirebaseUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());

        login = navigationView.getMenu().findItem(R.id.nav_login);
        logout = navigationView.getMenu().findItem(R.id.nav_logout);

        logout.setEnabled(false);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        email = (TextView) headerView.findViewById(R.id.email_user);
        name = (TextView) headerView.findViewById(R.id.name_user);
        photo = (ImageView) headerView.findViewById(R.id.photo_user);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);


        } else if (id == R.id.nav_logout) {

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            login.setEnabled(true);
                            logout.setEnabled(false);
                        }
                    });

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {

                //Login with success
                myUser = FirebaseAuth.getInstance().getCurrentUser();

                //We set what we want
                login.setTitle("Connecté");
                logout.setEnabled(true);
                login.setEnabled(false);

                email.setText(myUser.getEmail());
                name.setText(myUser.getDisplayName());
                Glide.with(this).load(myUser.getPhotoUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(photo);

            } else {
                //Failed to login
                Log.e(TAG, "Failed to login : " + resultCode);
            }
        }

    }
}
