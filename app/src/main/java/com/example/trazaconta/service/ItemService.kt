package com.example.trazaconta.service

import com.example.trazaconta.data.dao.GuestDAO
import com.example.trazaconta.entity.Item

class ItemService(val repository : GuestDAO)
{



    fun getList(id: Long, filter: Int) : MutableList<Item>
    {
       return when(filter)
        {
            1 -> repository.getListItemDESC(id)
            2 -> repository.getListItemASC(id)
            3 -> repository.getListItemAlfa(id)
            else -> repository.getListItem(id)

        }
    }


    fun deleteItem(item: Item) = repository.deleteItem(item)

    fun updateItem(item: Item) : Int = repository.updateItem(item)


    fun updateSumResult(itemList: List<Item>) : String {
        val sum = itemList.sumOf { it.price.toDouble() }
        return String.format("R$ %.2f", sum)
    }
}