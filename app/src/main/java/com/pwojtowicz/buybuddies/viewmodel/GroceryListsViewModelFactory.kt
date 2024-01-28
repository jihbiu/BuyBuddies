package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroceryListsViewModelFactory (private val application: Application)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroceryViewModel(application) as T
    }
}