package com.example.sutharnil.buggy;

import android.Manifest;
import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    EditText uid ,upass;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;
    private  CheckBox driver;
    private SharedPreferences sharedPreferences;
    private CheckBox checkBox;
    private  String s1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(loginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "You have already granted this permission!",
//                    Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Button ulogin = (Button) findViewById(R.id.userlogin);
        uid=(EditText)findViewById(R.id.did);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        upass=(EditText)findViewById(R.id.upass);

        firebaseAuth =FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()!=null){

                    if (firebaseAuth.getCurrentUser().getEmail().charAt(0)=='u'){
                        Intent intent2 = new Intent(loginActivity.this, RequestBuggy.class);
                        startActivity(intent2);
                        finish();
                    }else if (firebaseAuth.getCurrentUser().getEmail().charAt(0)=='d'){
                        Intent intent2 = new Intent(loginActivity.this, DriverScreen.class);
                        startActivity(intent2);
                        finish();
                    }
                }
            }
        };

        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id=uid.getText().toString();
                String pass=upass.getText().toString();

                if(id.isEmpty() && pass.isEmpty()){
                    Toast.makeText(loginActivity.this, "Id & Pass Empty", Toast.LENGTH_SHORT).show();
                }else if(id.isEmpty()){
                    Toast.makeText(loginActivity.this, "Id Empty", Toast.LENGTH_SHORT).show();
                }else if(pass.isEmpty()){
                    Toast.makeText(loginActivity.this, "Pass Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (id.charAt(0) == 'd' && checkBox.isChecked()) {
                                          singin(id,pass);

                    }else if(id.charAt(0)=='u' && !(checkBox.isChecked())){
                        singin1(id,pass);
                    }else{
                        Toast.makeText(loginActivity.this, "Please Enter correct Id", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void singin(final String id, String pass) {

        progressDialog.setMessage("Trying to Login...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(id,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(loginActivity.this, "Login", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(loginActivity.this, DriverScreen.class);
//                    startActivity(intent);
//                    finish();
                }else {
                    Toast.makeText(loginActivity.this, "Login Error...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void singin1(final String id, String pass) {

        progressDialog.setMessage("Trying to Login...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(id,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(loginActivity.this, "Login", Toast.LENGTH_SHORT).show();
//                    Intent intent1 = new Intent(loginActivity.this, RequestBuggy.class);
//                    startActivity(intent1);
//                    finish();

                }else {
                    Toast.makeText(loginActivity.this, "Login Error...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

