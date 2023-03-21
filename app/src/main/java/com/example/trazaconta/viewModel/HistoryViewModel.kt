package com.example.trazaconta.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trazaconta.data.dao.GuestDAO
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.service.EstablishmentService


class HistoryViewModel(private val dao: GuestDAO) : ViewModel() {

    private val _establishmentList = MutableLiveData<List<Establishment>>()
    val establishment: LiveData<List<Establishment>> = _establishmentList

    private val repository = EstablishmentService(dao)


    fun getEstablishment(filter: Int) = _establishmentList.postValue(repository.getEstablishment(filter))


    fun deleteEstablishment(fk:Long, establishment: Establishment)
    {
    repository.deleteEstablishment(fk,establishment)
    getEstablishment(0)
    }

}
class HistoryViewModelFactory(private val repository: GuestDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T
    }
}