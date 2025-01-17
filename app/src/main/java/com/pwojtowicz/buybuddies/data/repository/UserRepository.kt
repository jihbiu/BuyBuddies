package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
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