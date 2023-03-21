package com.example.trazaconta.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Establishment (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var name: String = "",

    var date: String = "",

    var hour: String = "",

)