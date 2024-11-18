package com.lion.a066ex_animalmanager.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "AnimalTable")
class AnimalVO(
    @PrimaryKey(autoGenerate = true)
    var animalIdx: Int = 0,
    var animalType: Int = 0,
    var animalName: String = "",
    var animalAge: Int = 0,
    var animalGender: Int = 0,
    var animalBirth: Date?=null
)