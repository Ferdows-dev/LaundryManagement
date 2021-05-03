package com.learning.laundrymanagement.Interface;

import com.learning.laundrymanagement.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner>banners);
    void onBannerLoadFailed(String message);
}
