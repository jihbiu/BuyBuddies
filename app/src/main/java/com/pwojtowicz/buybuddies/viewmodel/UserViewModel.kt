package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.repository.UserRepository

class UserViewModel(application: BuyBuddiesApplication): ViewModel() {
    private val userRepository = UserRepository(application)

}