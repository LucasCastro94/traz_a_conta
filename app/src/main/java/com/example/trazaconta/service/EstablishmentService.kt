package com.example.trazaconta.service

import com.example.trazaconta.data.dao.GuestDAO
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.utils.SortedByDate

class EstablishmentService (private val repository:GuestDAO) {

    fun getEstablishment(filter: Int) : MutableList<Establishment>
    {

       return when(filter)
        {
            1 -> SortedByDate().descending(repository.getListEstablishment())
            2 ->  SortedByDate().ascending(repository.getListEstablishment())
            3 ->  repository.getListEstablishmentASC()
            else -> repository.getListEstablishment()

        }
    }

    fun deleteEstablishment(fk:Long, establishment: Establishment)
    {
        repository.deleteChildEstablishment(fk)
        repository.deleteEstablishment(establishment)
    }

}