package com.learning.laundrymanagement.Interface;

import com.learning.laundrymanagement.Model.BookinInformation;

public interface IBookinInfoLoadListener {
void onBookinInfoLoadEmpty();
void onBookinginfoLoadSuccess(BookinInformation bookinInformation);
void onBookinginfoLoadFailed(String message);
}
