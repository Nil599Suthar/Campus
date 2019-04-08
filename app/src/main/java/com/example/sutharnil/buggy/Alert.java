package com.example.sutharnil.buggy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;


public class Alert extends Fragment {

    private RecyclerView recyclerView;
    private Get_Req_Adapter getReqAdapter;
    private List<Send_request> send_requestList=new ArrayList<>();
    private List<Send_request1> send_request1List=new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences1;
    private String a, b, user_name = "";
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
//        databaseReference1 = FirebaseDatabase.getInstance().getReference("UserInfo");
        a = firebaseUser.getEmail().toString();

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);


        getReqAdapter=new Get_Req_Adapter(send_requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getReqAdapter.setOnclickAccept(new Get_Req_Adapter.OnclickAccept() {


            @Override
            public void onclickAccept(int position) {
                String id=send_requestList.get(position).getUId();
                String key = id.substring(0,id.length()-4);

//                if (a.equals(id)){
//                    Toast.makeText(getActivity(), send_requestList.get(position).getUserLat()+" "+send_requestList.get(position).getUserLong(), Toast.LENGTH_SHORT).show();
                double userlat =send_requestList.get(position).getUserLat();
                double userlong =send_requestList.get(position).getUserLong();
//                }

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference= firebaseDatabase.getReference("Requests");
                databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("request_Status").setValue("Accepted");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

              //  getLocation(position);          //location fetch

                Bundle b =new Bundle();
                b.putDouble("lat",userlat);
                b.putDouble("long",userlong);
                fragment= new BuggyService();
                fragment.setArguments(b);
                fragmentManager=getFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame1,fragment);
                fragmentTransaction.commit();
//                getSupportActionBar().setTitle(title2);

            }
        });

//        getReqAdapter.setOnclickDecline(new Get_Req_Adapter.OnclickDecline() {
//
//
//            @Override
//            public void onclickDecline(int position) {
//
////                Intent broadcastIntent = new Intent();
////                broadcastIntent.setAction("restartservice1");
////                broadcastIntent.setClass(getActivity(), BroadCastService1.class);
////                getActivity().sendBroadcast(broadcastIntent);
//            }
//        });
        SnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(recyclerView);



        return view;
    }

    private void getLocation(int position) {

        String id=send_requestList.get(position).getUId();
        String key = id.substring(0,id.length()-4);





        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Requests");
        databaseReference.child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Send_request1 sendRequest=new Send_request1();
                for(DataSnapshot s1 : dataSnapshot.getChildren()) {
                    sendRequest = dataSnapshot.getValue(Send_request1.class);
                }
                send_request1List.add(sendRequest);
//                getReqAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(getReqAdapter);
                //notification

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Get_Request() {

        send_requestList.clear();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Requests");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Send_request sendRequest=new Send_request();
                    for(DataSnapshot s1 : dataSnapshot.getChildren()) {
                        sendRequest = dataSnapshot.getValue(Send_request.class);
                    }
                    send_requestList.add(sendRequest);
                    getReqAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(getReqAdapter);
                    //notification

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences1 = getActivity().getSharedPreferences("switch",Context.MODE_PRIVATE);
        String On=sharedPreferences1.getString("on","n").toString();

        if (On.equals("on")){
            Get_Request();

        }else if (On.equals("off")){
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_priority_high_black_24dp);
            builder.setCancelable(true);
            builder.setMessage("Please Active Your Availability..");
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }
}


