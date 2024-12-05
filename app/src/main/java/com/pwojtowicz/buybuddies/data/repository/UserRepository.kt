package com.pwojtowicz.buybuddies.data.repository

import android.app.Application
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase

class UserRepository(private val application: Application) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val userDao = buyBuddiesDB.userDao();
}