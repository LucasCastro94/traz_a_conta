package com.example.trazaconta.data.dao

import androidx.room.*
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.entity.Item



@Dao
interface GuestDAO{


     @Insert
     fun insertEstablishment(establishment: Establishment) : Long

     @Query("SELECT * FROM Establishment WHERE id = :id")
     fun getEstablishmentById(id: Long) : Establishment

     @Update
     fun updateEstablishment(establishment: Establishment) : Int

     @Delete
     fun deleteEstablishment(establishment: Establishment)

     @Query ("DELETE FROM Item WHERE fk_Establishment = :id")
     fun deleteChildEstablishment(id : Long)



     @Query("SELECT * FROM Establishment")
     fun getListEstablishment() : MutableList<Establishment>

     @Query("SELECT * FROM Establishment ORDER BY name ASC")
     fun getListEstablishmentASC() : MutableList<Establishment>



     //LISTITEM

     @Insert
     fun insertItem(item:Item):Long

     //Get All
     @Query("SELECT * FROM Item WHERE fk_Establishment = :id")
     fun getListItem(id : Long) : MutableList<Item>

     @Query("SELECT * FROM Item WHERE fk_Establishment = :id ORDER BY price DESC")
     fun getListItemDESC(id : Long) : MutableList<Item>

     @Query("SELECT * FROM Item WHERE fk_Establishment = :id ORDER BY price ASC")
     fun getListItemASC(id : Long) : MutableList<Item>

     @Query("SELECT * FROM Item WHERE fk_Establishment = :id ORDER BY nameitem ASC")
     fun getListItemAlfa(id : Long) : MutableList<Item>


     @Delete
     fun deleteItem(item:Item)

     @Update
     fun updateItem(item:Item) : Int


}

