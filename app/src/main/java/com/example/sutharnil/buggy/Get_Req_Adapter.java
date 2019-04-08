package com.example.sutharnil.buggy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Get_Req_Adapter extends RecyclerView.Adapter<Get_Req_Adapter.MyViewHolder> {

    private List<Send_request> send_requestList;
    private OnclickAccept mlistener;
    private OnclickDecline mlistener1 ;

    public Get_Req_Adapter(List<Send_request> context) {
            this.send_requestList=context;
    }

    public interface OnclickAccept {
        void onclickAccept(int position);
    }
    public void setOnclickAccept(Get_Req_Adapter.OnclickAccept listener) {
        mlistener = listener;
    }

    public interface OnclickDecline {
        void onclickDecline(int position);
    }
    public void setOnclickDecline(Get_Req_Adapter.OnclickDecline listener1) {
        mlistener1 = listener1;
    }
    @NonNull
    @Override
    public Get_Req_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.alerts_row,parent,false);
        return new MyViewHolder(view,mlistener,mlistener1);
    }

    @Override
    public void onBindViewHolder(@NonNull Get_Req_Adapter.MyViewHolder holder, int position) {

        holder.name.setText(send_requestList.get(position).getUname());
        holder.passenger.setText(send_requestList.get(position).getPessanger());
        holder.pickup.setText(send_requestList.get(position).getPickpoint());
        holder.drop.setText(send_requestList.get(position).getDroppoint());
    }

    @Override
    public int getItemCount() {
        return send_requestList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name,passenger,pickup,drop;
        private Button accept,decline;

        public MyViewHolder(View itemView, OnclickAccept mlistener, OnclickDecline mlistener1)
        {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);
            passenger=(TextView)itemView.findViewById(R.id.passanger);
            pickup=(TextView)itemView.findViewById(R.id.pickuppoint);
            drop=(TextView)itemView.findViewById(R.id.droppoint);
            accept=(Button)itemView.findViewById(R.id.accept);
//            decline=(Button)itemView.findViewById(R.id.decline);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Get_Req_Adapter.this.mlistener.onclickAccept(position);
                }
            });
//            decline.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Get_Req_Adapter.this.mlistener1.onclickDecline(position);
//                }
//            });

        }
    }



}
