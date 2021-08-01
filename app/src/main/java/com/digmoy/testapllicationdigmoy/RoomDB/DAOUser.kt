package com.digmoy.testapllicationdigmoy.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel

@Dao
interface DAOUser {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userTableModel: UserTableModel)

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<UserTableModel>>

    @Query("SELECT * FROM user where id =:id")
    fun getSelectUser(id : Int) : LiveData<UserTableModel>

    @Update
    suspend fun updateUser(userTableModel: UserTableModel)

    @Delete
    suspend fun deleteUser(userTableModel: UserTableModel)

}