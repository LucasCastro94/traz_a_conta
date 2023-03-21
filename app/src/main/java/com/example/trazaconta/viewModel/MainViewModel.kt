package com.example.trazaconta.viewModel

import android.app.Application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.trazaconta.data.GuestDataBase
import com.example.trazaconta.data.SecurityPreferences
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.utils.Constants


class MainViewModel(application: Application) : ViewModel() {


 private val mDatabase = GuestDataBase.getDatabase(application).guestDao()
 private val mShared = SecurityPreferences(application)


 fun insert(entity: Establishment) {
  mShared.saveFK(Constants.KEYS.toListItem,mDatabase.insertEstablishment(entity))
 }
}

 class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
   return MainViewModel(application) as T
  }
 }





