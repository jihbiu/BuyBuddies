package com.pwojtowicz.buybuddies

import android.app.Application
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.prefernces.PreferencesManager
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository

class BuyBuddiesApplication : Application() {
    lateinit var authorizationClient: AuthorizationClient
    lateinit var preferencesManager: PreferencesManager

    lateinit var userRepository: UserRepository
    lateinit var homeRepository: HomeRepository
    lateinit var groceryListRepository: GroceryListRepository

    override fun onCreate() {
        super.onCreate()

        instance = this
        initializeDependencies()
    }

    private fun initializeDependencies() {
        Firebase.initialize(this)

        preferencesManager = PreferencesManager(this)

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