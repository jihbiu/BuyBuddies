package com.pwojtowicz.buybuddies.di

import android.content.Context
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.api.GroceryListApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListItemDao
import com.pwojtowicz.buybuddies.data.dao.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dao.HomeDao
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.prefernces.PreferencesManager
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.GroceryRepository
import com.pwojtowicz.buybuddies.data.repository.HomeRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository = UserRepository(userDao)

    @Provides
    @Singleton
    fun provideHomeRepository(
        homeDao: HomeDao
    ): HomeRepository = HomeRepository(homeDao)

    @Provides
    @Singleton
    fun provideGroceryListRepository(
        authClient: AuthorizationClient,
        groceryListApiService: GroceryListApiService,
        groceryListDao: GroceryListDao
    ): GroceryListRepository = GroceryListRepository(authClient, groceryListApiService, groceryListDao)

    @Provides
    @Singleton
    fun provideGroceryRepository(
        groceryListDao: GroceryListDao,
        groceryItemDao: GroceryListItemDao,
        groceryListLabelDao: GroceryListLabelDao
    ): GroceryRepository = GroceryRepository(groceryListDao, groceryItemDao, groceryListLabelDao)
}