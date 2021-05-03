package com.learning.laundrymanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.EventBus.EnableNextButton;
import com.learning.laundrymanagement.Interface.IRecycleItemSelectedListener;
import com.learning.laundrymanagement.Model.Shop;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyShopAdapter extends RecyclerView.Adapter<MyShopAdapter.MyViewHolder> {
    Context context;
    List<Shop> shopList;
    List<CardView> cardViewList;


    public MyShopAdapter(Context context, List<Shop> shopList) {
        this.context = context;
        this.shopList = shopList;
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layput_shop,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.shop_name.setText(shopList.get(position).getName());
        holder.ratingBar.setRating((float)shopList.get(position).getRating());
        if (!cardViewList.contains(holder.card_shop))
            cardViewList.add(holder.card_shop);

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                for (CardView cardView: cardViewList){
                    cardView.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white));
                }
                holder.card_shop.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.holo_orange_dark));

//

                //Eventbus
                EventBus.getDefault().postSticky(new EnableNextButton(2,shopList.get(position)));
            }
        });


    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView shop_name;
        RatingBar ratingBar;
        CardView card_shop;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_shop = itemView.findViewById(R.id.card_shop);
            shop_name = itemView.findViewById(R.id.shop_name);
            ratingBar = itemView.findViewById(R.id.rtb_ratingBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
