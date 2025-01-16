package com.pwojtowicz.buybuddies.data.repository

import android.app.Application
import android.util.Log
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.data.db.BuyBuddiesDatabase
import com.pwojtowicz.buybuddies.data.dto.UserDTO
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.User

class UserRepository(private val application: BuyBuddiesApplication) {
    private val buyBuddiesDB: BuyBuddiesDatabase = BuyBuddiesDatabase.getInstance(application)
    private val userDao = buyBuddiesDB.userDao();

    suspend fun insertUser(user: User): Long {
        try {
            return userDao.insert(user)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting user", e)
            throw e
        }
    }

    suspend fun deleteAll() {
        try {
            userDao.deleteAll()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting all users", e)
            throw e
        }
    }

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }
}