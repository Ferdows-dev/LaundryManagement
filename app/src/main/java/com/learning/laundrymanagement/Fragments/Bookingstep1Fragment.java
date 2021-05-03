package com.learning.laundrymanagement.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.learning.laundrymanagement.Adapter.MyLaundryAdapter;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Common.SpaceItemDecoration;
import com.learning.laundrymanagement.Interface.IAllLaundyLoadListener;
import com.learning.laundrymanagement.Interface.IBranchLoadListener;
import com.learning.laundrymanagement.Model.Laundry;
import com.learning.laundrymanagement.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Bookingstep1Fragment extends Fragment implements IAllLaundyLoadListener, IBranchLoadListener {

    CollectionReference allLaundryRef;
    CollectionReference branchRef;

    IAllLaundyLoadListener iAllLaundyLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.laundru_recycleview)
    RecyclerView laundry_recyclerview;

    Unbinder unbinder;
    AlertDialog dialog;

    static Bookingstep1Fragment instance;
    public static Bookingstep1Fragment getInstance(){
        if (instance==null)
            instance = new Bookingstep1Fragment();

            return instance;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allLaundryRef = FirebaseFirestore.getInstance().collection("AllLaundry");
        iAllLaundyLoadListener= this;
        iBranchLoadListener = this;
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View view= inflater.inflate(R.layout.booking_step_one_fragment,container,false);
         unbinder= ButterKnife.bind(this,view);

         initView();
         loadAllLaundry();
         
         return view;
    }

    private void initView() {
        laundry_recyclerview.setHasFixedSize(true);
        laundry_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        laundry_recyclerview.addItemDecoration(new SpaceItemDecoration(4));
    }

    private void loadAllLaundry() {
        allLaundryRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please Choose city");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllLaundyLoadListener.onAllLaundryLoadSuccess(list);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllLaundyLoadListener.onAllLaundryLoadFailed(e.getMessage());

            }
        });
    }

    @Override
    public void onAllLaundryLoadSuccess(List<String> areaNameList) {

        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position>0)
                {
                    loadBranchofCity(item.toString());
                }else{
                    laundry_recyclerview.setVisibility(View.GONE);
                }
            }
        });
        

    }

    private void loadBranchofCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        branchRef = FirebaseFirestore.getInstance().collection("AllLaundry")
                .document(cityName)
                .collection("Branch");
        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Laundry> list = new ArrayList<>();
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        Laundry laundry = documentSnapshot.toObject(Laundry.class);
                        laundry.setLaundryId(documentSnapshot.getId());
                        list.add(laundry);
                    }
                    iBranchLoadListener.onBranchLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllLaundryLoadFailed(String message) {
        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSuccess(List<Laundry> laundryList) {

        MyLaundryAdapter adapter = new MyLaundryAdapter(getActivity(),laundryList);
        laundry_recyclerview.setAdapter(adapter);
        laundry_recyclerview.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
