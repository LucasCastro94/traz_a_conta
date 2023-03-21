package com.example.trazaconta.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room.databaseBuilder
import com.example.trazaconta.data.dao.GuestDAO
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.entity.Item

@Database(entities = [Item::class,Establishment::class], version = 1)
abstract class GuestDataBase: RoomDatabase() {
    abstract fun guestDao(): GuestDAO

    companion object {

        private lateinit var INSTANCE: GuestDataBase

        fun getDatabase(context: Context): GuestDataBase {

            if (!::INSTANCE.isInitialized) {
                synchronized(GuestDataBase::class.java)
                {
                    INSTANCE = databaseBuilder(context, GuestDataBase::class.java, "TrazAContaDB")
                        .allowMainThreadQueries()
                        .build()

                }
            }
            return INSTANCE
        }
    }
}