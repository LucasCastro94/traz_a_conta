package com.example.trazaconta.utils

import com.example.trazaconta.entity.Establishment
import java.text.SimpleDateFormat

class SortedByDate {
   private val format = SimpleDateFormat("dd/MM/yyyy")

    fun descending(list: MutableList<Establishment>) : MutableList<Establishment>{

        val filtered = list.filter {
            format.parse(it.date)?.let { true } ?: false
        }.sortedByDescending { format.parse(it.date) }

    return filtered.toMutableList()
    }

    fun ascending(list: MutableList<Establishment>) : MutableList<Establishment>{

        val filtered = list.filter {
            format.parse(it.date)?.let { true } ?: false
        }.sortedBy { format.parse(it.date) }

        return filtered.toMutableList()
    }


}