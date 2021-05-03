package com.learning.laundrymanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.EventBus.EnableNextButton;
import com.learning.laundrymanagement.Interface.IRecycleItemSelectedListener;
import com.learning.laundrymanagement.Model.Laundry;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyLaundryAdapter extends RecyclerView.Adapter<MyLaundryAdapter.MyViewHolder> {

    Context context;
    List<Laundry> laundryList;
    List<CardView> cardViewList;


    public MyLaundryAdapter(Context context, List<Laundry> laundryList) {
        this.context = context;
        this.laundryList = laundryList;
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_laundry,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.laundryName.setText(laundryList.get(position).getName());
        holder.laundryAddress.setText(laundryList.get(position).getAddress());
        if (!cardViewList.contains(holder.card_laundry))
            cardViewList.add(holder.card_laundry);

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                holder.card_laundry.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.holo_orange_dark));


                //EventBus
                EventBus.getDefault().postSticky(new EnableNextButton(1,laundryList.get(position)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView laundryName,laundryAddress;
        CardView card_laundry;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            card_laundry = itemView.findViewById(R.id.card_laundry);
            laundryName = itemView.findViewById(R.id.laundry_name);
            laundryAddress= itemView.findViewById(R.id.laundry_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
