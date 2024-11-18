package com.lion.a066ex_animalmanager.data.viewmodel

import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import java.util.Date

data class AnimalViewModel(
    var animalIdx: Int,
    var animalType: AnimalType,
    var animalName: String,
    var animalAge: Int,
    var animalGender: AnimalGender,
    var animalBirth: Date
)