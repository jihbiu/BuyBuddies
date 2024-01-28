package com.pwojtowicz.buybuddies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pwojtowicz.buybuddies.data.model.GroceryItem
import com.pwojtowicz.buybuddies.data.model.GroceryList

@Database(
    entities = [GroceryList::class, GroceryItem::class],
    version = 1
)
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryListDao(): GroceryListDao
    abstract fun groceryItemDao(): GroceryItemDao

    companion object {
        @Volatile
        private var Instance: GroceryDatabase? = null

        fun getInstance(context: Context): GroceryDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, GroceryDatabase::class.java, "grocery_database")
                    .build()
                    .also { Instance = it }
            }

        }
    }
}