package com.example.trazaconta.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item (
    @PrimaryKey(autoGenerate = true)
    var itemId : Long = 0,
    var nameitem : String = "",
    var price : Float = 0f,
    var fk_Establishment : Long = 0
)
