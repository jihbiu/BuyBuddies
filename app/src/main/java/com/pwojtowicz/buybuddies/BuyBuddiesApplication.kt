package com.pwojtowicz.buybuddies

import android.app.Application
import com.google.android.gms.auth.api.identity.Identity
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import com.pwojtowicz.buybuddies.navigation.NavRoute
import com.pwojtowicz.buybuddies.ui.MainViewModel
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel

class BuyBuddiesApplication : Application() {
    lateinit var authorizationClient: AuthorizationClient

    lateinit var userRepository: UserRepository
    lateinit var homeRepository: HomeRepository
    lateinit var groceryListRepository: GroceryListRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDependencies()
    }

    private fun initializeDependencies() {
        authorizationClient = AuthorizationClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )

        userRepository = UserRepository(application = this)
        homeRepository = HomeRepository(application= this)
        groceryListRepository = GroceryListRepository(application = this)
    }

    companion object {
        lateinit var instance: BuyBuddiesApplication
            private set
    }
}