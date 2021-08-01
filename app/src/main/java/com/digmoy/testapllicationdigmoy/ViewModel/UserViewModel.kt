package com.digmoy.testapllicationdigmoy.ViewModel

import androidx.lifecycle.ViewModel
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel
import com.digmoy.testapllicationdigmoy.Repository.UserRepository

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun insertUser (user : UserTableModel) = repository.insertUser(user)

    suspend fun updateUser(user: UserTableModel) = repository.updateUser(user)

    suspend fun deleteUser(user: UserTableModel) = repository.deleteUser(user)

    fun getSelectUser(id : Int) = repository.getSelectUser(id)

    fun getAllUser() = repository.getAllUser()

}