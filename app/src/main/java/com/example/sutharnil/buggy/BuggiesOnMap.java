package com.example.sutharnil.buggy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static java.sql.Types.NULL;

public class BuggiesOnMap extends AppCompatActivity implements OnMapReadyCallback  {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private static final int ERROR_DAILOG = 9001;
    private GoogleMap mMap;
    private double userLat,userLong;
    private LatLng latLng1,latLng2;
    private static final int LOCATION_REQUEST = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buggies_on_map);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.check, null);

        userLat=getIntent().getDoubleExtra("userLat",NULL);
        userLong=getIntent().getDoubleExtra("userLong",NULL);
        Toast.makeText(this, userLat +" "+userLong, Toast.LENGTH_SHORT).show();

        latLng1=new LatLng(userLat,userLong);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        AlertDialog.Builder builder=new AlertDialog.Builder(BuggiesOnMap.this);
//        builder.setTitle("Do you want to Exit ?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog  alertDialog =builder.create();
//        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Booking Successful");
        alertDialogBuilder.setMessage("Your request for a \n buggy is Successfully \n assign to buggy in Short Time... ");
//
        alertDialogBuilder.setIcon(R.drawable.ic_check_black_24dp);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alertDialog.cancel();
            }
        }).start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
//        mMap.setMyLocationEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .title("You Are Here!!")
                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_person_pin_circle_black_24dp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,17));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
