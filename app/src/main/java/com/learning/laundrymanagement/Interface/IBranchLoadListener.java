package com.learning.laundrymanagement.Interface;

import com.learning.laundrymanagement.Model.Laundry;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Laundry> laundryList);
    void onBranchLoadFailed(String message);
}
