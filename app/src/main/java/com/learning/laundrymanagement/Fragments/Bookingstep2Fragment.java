package com.learning.laundrymanagement.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.laundrymanagement.Adapter.MyShopAdapter;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Common.SpaceItemDecoration;
import com.learning.laundrymanagement.EventBus.ShopDoneEvent;
import com.learning.laundrymanagement.Model.Shop;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Bookingstep2Fragment extends Fragment {
Unbinder unbinder;


@BindView(R.id.recycler_shop)
    RecyclerView recycler_shop;

    //Eventbus start


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void setshopAdapter(ShopDoneEvent event)
    {
        MyShopAdapter adapter = new MyShopAdapter(getContext(),event.getShopList());
        recycler_shop.setAdapter(adapter);
    }

    static Bookingstep2Fragment instance;
    public static Bookingstep2Fragment getInstance(){
        if (instance==null)
            instance = new Bookingstep2Fragment();

        return instance;


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
//


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.booking_step_two_fragment,container,false);

         unbinder= ButterKnife.bind(this,itemView);
         
         initView();
         
        return itemView;
    }

    private void initView() {
        recycler_shop.setHasFixedSize(true);
        recycler_shop.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_shop.addItemDecoration(new SpaceItemDecoration(4));
    }
}
