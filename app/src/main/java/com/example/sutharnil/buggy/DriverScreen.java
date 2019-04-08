package com.example.sutharnil.buggy;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverScreen extends AppCompatActivity {

    private FragmentManager  fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    Fragment fragment;
    Intent mServiceIntent;
    private Req_Noti_Service YourService;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            ActionBar actionBar;
            switch (item.getItemId()) {
                case R.id.nav_alert:


                    String title2 ="Alerts";
                    fragment= new Alert();
                    fragmentManager=getFragmentManager();
                     fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame1,fragment);
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle(title2);

                    return true;

                case R.id.nav_profile:

                    String title ="Profile";
                    fragment= new Profile();
                     fragmentManager=getFragmentManager();
                     fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame1,fragment);
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle(title);


                    return true;
                case R.id.nav_buggy:

                    String title1 ="Buggy Service";

                    fragment= new BuggyService();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame1,fragment);
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle(title1);

                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_screen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.nav_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth=FirebaseAuth.getInstance();



    }


    @Override
    protected void onDestroy() {
//         mServiceIntent = new Intent(this, Req_Noti_Service.class);

//        stopService(mServiceIntent);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_for_driver,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.logout1){

            final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(this);
            alert.setMessage("Do You Want To LogOut ?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Singout();
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

            android.support.v7.app.AlertDialog alertDialog=alert.create();
            alertDialog.show();        }
        return super.onOptionsItemSelected(item);
    }

    private void Singout() {

        firebaseAuth.signOut();

        firebaseUser= firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(DriverScreen.this,loginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

//        }
    }
}
