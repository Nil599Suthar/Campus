package com.example.sutharnil.buggy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.database.*;

public class BroadCastService extends BroadcastReceiver {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Intent mServiceIntent;
    private Req_Noti_Service YourService;

    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
            Get_Request(context);
//            context.stopService(mServiceIntent);

    }

    private void Get_Request(final Context context1){


//        send_requestList.clear();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Requests");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                Toast.makeText(context1, "ok", Toast.LENGTH_SHORT).show();
                mServiceIntent = new Intent(context1, Req_Noti_Service.class);
                context1.startService(mServiceIntent);
                context1.stopService(mServiceIntent);
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

}
