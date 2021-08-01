package com.digmoy.testapllicationdigmoy.Repository

import androidx.lifecycle.LiveData
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel
import com.digmoy.testapllicationdigmoy.RoomDB.UserDataBase


class UserRepository(private val userDataBase: UserDataBase) {

    suspend fun insertUser(user : UserTableModel) = userDataBase.getUserDao().insertUser(user)

    suspend fun updateUser(user : UserTableModel) = userDataBase.getUserDao().updateUser(user)

    suspend fun deleteUser (user: UserTableModel) = userDataBase.getUserDao().deleteUser(user)

    fun getSelectUser(id : Int) = userDataBase.getUserDao().getSelectUser(id)

    fun getAllUser () : LiveData<List<UserTableModel>> = userDataBase.getUserDao().getAll()

}