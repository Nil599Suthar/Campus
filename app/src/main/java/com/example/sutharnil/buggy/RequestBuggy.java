package com.example.sutharnil.buggy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static java.sql.Types.NULL;

public class RequestBuggy extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, databaseReference1;
    private Spinner pickup, drop;
    private TextView seek;
    private String s1, s2, s3, s4, s5,s6;
    private String a, b, user_name = "";
    private ProgressDialog progressDialog;
    private Button req_buggy;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double userLat,userLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_buggy);



        req_buggy = (Button) findViewById(R.id.req_buggy);
        final SeekBar seekbar = findViewById(R.id.seekBar);
        Button plus = (Button) findViewById(R.id.btnplus);
        Button minus = (Button) findViewById(R.id.btnminus);
        seek = (TextView) findViewById(R.id.seek);
        pickup = (Spinner) findViewById(R.id.pickup);
        drop = (Spinner) findViewById(R.id.drop);
        progressDialog = new ProgressDialog(RequestBuggy.this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("UserInfo");
        a = firebaseUser.getEmail().toString();

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();



        seekbar.setProgress(1);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seek.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekbar.setProgress(seekbar.getProgress() + 1);

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekbar.setProgress(seekbar.getProgress() - 1);

            }
        });

        req_buggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s1 = pickup.getSelectedItem().toString();
                s2 = drop.getSelectedItem().toString();
                s3 = seek.getText().toString();
                s4 = firebaseUser.getEmail().toString();
                s5 = user_name.toString();
                  s6="pending";
                  if (s1.equals(s2)){
                      Toast.makeText(RequestBuggy.this, "Please Chech Your Drop Point", Toast.LENGTH_SHORT).show();
                  }else {
                      addData(s4, s5, s1, s2, s3, userLat, userLong, s6);
                      Toast.makeText(RequestBuggy.this, userLong + " " + userLat, Toast.LENGTH_SHORT).show();
                  }
            }
        });
    }


    private void addData(String s4, String s5, String s1, String s2, String s3, final double userLat, final double userLong, String s6) {


        if (s5.trim().equals("") && userLat != NULL) {
            Toast.makeText(this, "Please Check Number Of Passangers", Toast.LENGTH_SHORT).show();

        } else {
            String key = a.substring(0,a.length()-4);

            Send_request sendRequest = new Send_request(key, s4, s5, s1, s2, s3,userLat,userLong,s6);
            databaseReference.child(key).setValue(sendRequest);
            final Intent intent = new Intent(RequestBuggy.this, BuggiesOnMap.class);

            progressDialog.setMessage("Your request for a buggy is being processed. \n" +
                    "Please wait for some time....");
            progressDialog.setIcon(R.drawable.ic_logopit_1534081965403);
            progressDialog.setCancelable(false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(3000);
                        progressDialog.dismiss();
                        intent.putExtra("userLat",userLat);
                        intent.putExtra("userLong",userLong);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            progressDialog.show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_for_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            final AlertDialog.Builder alert=new AlertDialog.Builder(this);
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

            AlertDialog alertDialog=alert.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.account) {
            Intent i = new Intent(this, Account_user.class);
            startActivity(i);
        }else if(item.getItemId()==R.id.user_alert){
            Intent intent=new Intent(this,user_alerts.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void Singout() {

        firebaseAuth.signOut();

        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser == null) {
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RequestBuggy.this, loginActivity.class);
            startActivity(i);
            finish();

        }
    }


    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance

        }

        @Override
        protected String doInBackground(String... strings) {

            LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.GPS_PROVIDER;
            if (ActivityCompat.checkSelfPermission(RequestBuggy.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RequestBuggy.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return String.valueOf(true);
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
             userLat = lastKnownLocation.getLatitude();
             userLong = lastKnownLocation.getLongitude();

            b = a.substring(0, a.length() - 4);
            databaseReference1.child(b).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();

                    String n1 = data.get("u_f_name").toString();
                    String n2 = data.get("u_l_name").toString();
                    user_name = n1 + " " + n2;

                    s5 = user_name;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return user_name;
        }

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder builder=new AlertDialog.Builder(RequestBuggy.this);
        builder.setTitle("Do you want to Exit ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog  alertDialog =builder.create();
        alertDialog.show();
    }
}
