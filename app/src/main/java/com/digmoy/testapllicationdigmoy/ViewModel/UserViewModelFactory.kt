package com.digmoy.testapllicationdigmoy.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digmoy.testapllicationdigmoy.Repository.UserRepository
import com.digmoy.testapllicationdigmoy.Utils.UtilFunctions.loge

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(UserRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            loge(e.message.toString())
        }
        return super.create(modelClass)
    }
}