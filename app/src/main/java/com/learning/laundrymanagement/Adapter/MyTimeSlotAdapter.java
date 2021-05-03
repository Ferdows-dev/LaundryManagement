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
import com.learning.laundrymanagement.Model.Shop;
import com.learning.laundrymanagement.Model.TimeSlot;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder>{
    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();

        this.cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;

        this.cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time_slot.setText(new StringBuilder(Common.ConverTimeSlottoString(position)).toString());
        if (timeSlotList.size() == 0){

            holder.card_time_slot.setEnabled(true);

            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

            holder.time_slot_description.setText("Available");
            holder.time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }else
            {
            for (TimeSlot slotValue: timeSlotList){

                int slot = Integer.parseInt(String.valueOf(slotValue.getSlot()));
                if (slot == position)
                {
                    holder.card_time_slot.setEnabled(false);
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

                    holder.time_slot_description.setText("Full");
                    holder.time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                    holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                }
               


            }
        }
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                for (CardView cardView:cardViewList){
                    if (cardView.getTag() == null)
                        cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                }
                holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
//
//

                EventBus.getDefault().postSticky(new EnableNextButton(3,position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time_slot,time_slot_description;
        CardView card_time_slot;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            time_slot = itemView.findViewById(R.id.time_slot);
            time_slot_description = itemView.findViewById(R.id.time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
