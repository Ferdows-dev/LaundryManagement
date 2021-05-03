package com.learning.laundrymanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Model.BookinInformation;
import com.learning.laundrymanagement.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

    Context context;
    List<BookinInformation> bookinInformationList;

    public MyHistoryAdapter(Context context, List<BookinInformation> bookinInformationList) {
        this.context = context;
        this.bookinInformationList = bookinInformationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.booking_time_textview.setText(bookinInformationList.get(position).getTime());
        holder.laundry_location_text.setText(bookinInformationList.get(position).getLaundryAddress());
        holder.laundry_name_textview.setText(bookinInformationList.get(position).getLaundryName());
        holder.booking_user_adresss_textview.setText(Common.currentUser.getAddress());

    }

    @Override
    public int getItemCount() {
        return bookinInformationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Unbinder unbinder;

        @BindView(R.id.laundry_name_textview)
        TextView laundry_name_textview;
        @BindView(R.id.booking_date_text)
        TextView booking_date_text;
        @BindView(R.id.laundry_location_text)
        TextView laundry_location_text;
        @BindView(R.id.booking_time_textview)
        TextView booking_time_textview;
        @BindView(R.id.booking_user_text)
        TextView booking_user_text;
        @BindView(R.id.booking_user_adresss_textview)
        TextView booking_user_adresss_textview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
