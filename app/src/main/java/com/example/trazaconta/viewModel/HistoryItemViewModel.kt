package com.example.trazaconta.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trazaconta.data.dao.GuestDAO
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.entity.Item
import com.example.trazaconta.service.ItemService

class HistoryItemViewModel(private val dao: GuestDAO) : ViewModel() {


    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList

    private val _sumResult = MutableLiveData<String>()
    val sumResult = _sumResult

    private val repository : ItemService = ItemService(dao)




    fun insertItem(name: String, price: Float, fkEstablishment: Long) {
        dao.insertItem(
            Item(
                nameitem = name,
                price = price,
                fk_Establishment = fkEstablishment
            )
        )
        getList(fkEstablishment,2)

    }

    fun getList(id: Long, filter: Int) = _itemList.postValue(repository.getList(id,filter))

    fun deleteItem(item: Item) = repository.deleteItem(item)

    fun updateItem(item: Item) : Int = repository.updateItem(item)

    fun updateSumResult(itemList: List<Item>) = _sumResult.postValue(repository.updateSumResult(itemList))

    fun updateEstablishment(establishment: Establishment) = dao.updateEstablishment(establishment)

    fun getEstablishmentByID(id: Long) : Establishment = dao.getEstablishmentById(id)





    init {
        itemList.observeForever { itemList ->
            updateSumResult(itemList)
        }
    }





}

class HistoryItemViewModelFactory(private val repository: GuestDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryItemViewModel(repository) as T
    }
}