package com.example.sutharnil.buggy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Map;


public class Profile extends Fragment {

    private SharedPreferences sharedPreferences1,sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference1;
    private TextView bgy_no,bgy_name,bgy_sheet,mobile,address,did,dfname,dlname;
    private Switch Switch;
    private String a,b;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=  inflater.inflate(R.layout.fragment_profile, container, false);
          bgy_no=(TextView) view.findViewById(R.id.bgyno);
          bgy_name=(TextView) view.findViewById(R.id.bgyname);
          bgy_sheet=(TextView) view.findViewById(R.id.bgysheet);
          mobile=(TextView) view.findViewById(R.id.mobileno);
        address=(TextView) view.findViewById(R.id.address);
        dfname=(TextView) view.findViewById(R.id.dfnme);
        dlname=(TextView) view.findViewById(R.id.dlnme);
        did=(TextView) view.findViewById(R.id.did);
          Switch=(Switch)view.findViewById(R.id.switch1);
 //for info

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference1= FirebaseDatabase.getInstance().getReference("DriverInfo");
        a=firebaseUser.getEmail().toString();

//
        Switch.setChecked(false);
        sharedPreferences1 = getContext().getSharedPreferences("switch",Context.MODE_PRIVATE);
        String On=sharedPreferences1.getString("on","n").toString();


        if (On.equals("on")){
            Switch.setChecked(true);
        }else if (On.equals("off")){
            Switch.setChecked(false);
        }else{
            Switch.setChecked(false);
        }

        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getContext(), "Online", Toast.LENGTH_SHORT).show();
                    sharedPreferences = getContext().getSharedPreferences("switch",Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("on","on");
                    editor.commit();

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("restartservice");
                    broadcastIntent.setClass(getActivity(), BroadCastService.class);
                    getActivity().sendBroadcast(broadcastIntent);

                }else{
                    Toast.makeText(getContext(), "Offline", Toast.LENGTH_SHORT).show();
                    sharedPreferences = getContext().getSharedPreferences("switch",Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("on","off");
                        editor.commit();
                }
            }
        });



//bugy information update

//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
//                    View view1=inflater.inflate(R.layout.buggyinfo_update,null);
//                alert.setTitle("Buggy Info Update");
//                alert.setView(view1);
//                final EditText No=(EditText)view1.findViewById(R.id.bgyno);
//               final EditText Name=(EditText)view1.findViewById(R.id.bgyname);
//                final EditText Sheet=(EditText)view1.findViewById(R.id.bgysheet);
//                alert.setIcon(R.drawable.ic_logopit_1534081965403);
//
//                alert.setCancelable(false);
//                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (!No.getText().toString().isEmpty()|| !Name.getText().toString().isEmpty() || !Sheet.getText().toString().isEmpty())
//                        {
//                            if (!No.getText().toString().isEmpty()){
//                                bgy_no.setText(No.getText().toString());
//                            }
//                            if (!Name.getText().toString().isEmpty()){
//                                bgy_name.setText(Name.getText().toString());
//                            }
//                            if (!Sheet.getText().toString().isEmpty()){
//                                bgy_sheet.setText(Sheet.getText().toString());
//                            }
//
//                        }
//                        else {
//                            Toast.makeText(getContext(),"Feel Up Field",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//                AlertDialog alertDialog=alert.create();
//                alertDialog.show();
//
//
//
//            }
//        });

//driver info update

//        update1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
//                View view1=inflater.inflate(R.layout.personalinfo_update,null);
//                alert.setTitle("Personal Info Upate");
//
//                alert.setView(view1);
//
//                final EditText Mobile=(EditText)view1.findViewById(R.id.mobileno);
//                final EditText Address=(EditText)view1.findViewById(R.id.address);
//                alert.setIcon(R.drawable.ic_person_black_24dp);
//                alert.setCancelable(false);
//
//                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (!Mobile.getText().toString().isEmpty()|| !Address.getText().toString().isEmpty())
//                        {
//                            if (!Address.getText().toString().isEmpty()){
//                                address.setText(Address.getText().toString());
//
//                            } if (!Mobile.getText().toString().isEmpty()){
//                            mobile.setText(Mobile.getText().toString());
//
//                        }
//
//                        } else {
//                            Toast.makeText(getContext(),"Feel Up Field",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//
//                AlertDialog alertDialog=alert.create();
//                alertDialog.show();
//
//
//            }
//        });


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();


         b=a.substring(0,a.length()-4);
        databaseReference1.child(b).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> data =(Map<String, Object>) dataSnapshot.getValue();
                bgy_no.setText(""+data.get("car_no").toString());
                bgy_name.setText(""+data.get("car_model").toString());
                bgy_sheet.setText(""+data.get("sheet_no").toString());
                did.setText(""+a);
                mobile.setText(""+data.get("m_no").toString());
                address.setText(""+data.get("address").toString());
                dfname.setText(""+data.get("d_f_name").toString());
                dlname.setText(""+data.get("d_l_name").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
