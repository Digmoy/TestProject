package com.digmoy.testapllicationdigmoy.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel

@Database(entities = [UserTableModel::class],version = 1,exportSchema = false)
abstract class UserDataBase : RoomDatabase() {

    abstract fun getUserDao(): DAOUser

    companion object{

        private const val DB_NAME = "user_database.db"
        @Volatile private var instance: UserDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDataBase::class.java,
            DB_NAME
        ).build()
    }
//        @Volatile
//        private var INSTANCE: UserDataBase? = null
//
//        fun getDataBaseClient(context: Context) : UserDataBase {
//
//            if (INSTANCE != null) return INSTANCE!!
//
//            synchronized(this) {
//
//                INSTANCE = Room
//                    .databaseBuilder(context, UserDataBase::class.java, "USER_DATABASE")
//                    .fallbackToDestructiveMigration()
//                    .build()
//
//                return INSTANCE!!
//
//            }
//        }

}