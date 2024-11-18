package com.lion.a066ex_animalmanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.a066ex_animalmanager.data.vo.AnimalVO

@Dao
interface AnimalDAO {

    @Insert
    fun insertAnimal(animalVO: AnimalVO)

    @Delete
    fun deleteAnimal(animalVO: AnimalVO)

    @Update
    fun updateAnimal(animalVO: AnimalVO)

    @Delete
    fun deleteAnimalData(animalVO: AnimalVO)

    @Query("DELETE FROM AnimalTable")
    fun deleteAnimalInfoALl()

    @Query("SELECT * FROM AnimalTable Where animalType = :animalType")
    fun getSortAnimal(animalType: Int): List<AnimalVO>

    @Query("SELECT * FROM AnimalTable")
    fun getAllAnimal(): List<AnimalVO>

    @Query("SELECT * FROM AnimalTable WHERE animalIdx=:animalIdx")
    fun selectAnimalDataByAnimalIdx(animalIdx: Int): AnimalVO


}