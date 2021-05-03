package com.learning.laundrymanagement.Interface;

import java.util.List;

public interface IAllLaundyLoadListener {
    void onAllLaundryLoadSuccess(List<String>areaNameList);
    void onAllLaundryLoadFailed(String message);
}
