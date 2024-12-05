package com.pwojtowicz.buybuddies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pwojtowicz.buybuddies.data.dao.depot.DepotDao
import com.pwojtowicz.buybuddies.data.dao.groceryitem.GroceryItemDao
import com.pwojtowicz.buybuddies.data.dao.grocerylist.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.grocerylist.GroceryListLabelDao
import com.pwojtowicz.buybuddies.data.dao.user.UserDao
import com.pwojtowicz.buybuddies.data.model.depot.Depot
import com.pwojtowicz.buybuddies.data.model.groceryitem.GroceryItem
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryList
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListImage
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListLabel
import com.pwojtowicz.buybuddies.data.model.grocerylist.GroceryListLabelCross
import com.pwojtowicz.buybuddies.data.model.user.User
import com.pwojtowicz.buybuddies.data.model.user.UserAvatar

@Database(
    entities = [
        Depot::class,
        GroceryItem::class,
        GroceryList::class,
        GroceryListImage::class,
        GroceryListLabel::class,
        GroceryListLabelCross::class,
        User::class,
        UserAvatar::class],
    version = 1,
    exportSchema = false
)
abstract class BuyBuddiesDatabase : RoomDatabase() {
    abstract fun groceryListDao(): GroceryListDao
    abstract fun groceryListLabelDao(): GroceryListLabelDao
    abstract fun groceryItemDao(): GroceryItemDao
    abstract fun depotDao(): DepotDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: BuyBuddiesDatabase? = null

        fun getInstance(context: Context): BuyBuddiesDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    BuyBuddiesDatabase::class.java,
                    "buybuddies_databases"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}