package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pwojtowicz.buybuddies.BuyBuddiesApplication

class UserViewModelFactory(private val application: BuyBuddiesApplication)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(application) as T
    }
}