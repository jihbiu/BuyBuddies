package com.pwojtowicz.buybuddies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.pwojtowicz.buybuddies.data.repository.UserRepository

class UserViewModel(application: Application): ViewModel() {
    private val userRepository = UserRepository(application)

}