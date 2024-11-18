package com.lion.a066ex_animalmanager.data.repo

import android.content.Context
import com.lion.a066ex_animalmanager.data.dao.AnimalDatabase
import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.data.vo.AnimalVO
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType

class AnimalRepo {
    companion object {
        // Save animal info
        fun insertAnimalInfo(context: Context, animalViewModel: AnimalViewModel) {
            val animalDatabase = AnimalDatabase.getInstance(context)
            val animalType = animalViewModel.animalType.typeNumber
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.genderNumber
            val animalBirth = animalViewModel.animalBirth

            val animalVO = AnimalVO(
                animalType = animalType,
                animalName = animalName,
                animalAge = animalAge,
                animalGender = animalGender,
                animalBirth = animalBirth
            )

            animalDatabase?.animalDAO()?.insertAnimal(animalVO)
        }

        // Delete All AnimalInfo
        fun deleteAnimalInfoAll(context: Context) {
            val animalDatabase = AnimalDatabase.getInstance(context)
            animalDatabase?.animalDAO()?.deleteAnimalInfoALl()
        }

        // Get All AnimalInfo
        fun selectAnimalInfoAll(context: Context): MutableList<AnimalViewModel> {
            val animalDatabase = AnimalDatabase.getInstance(context)
            val animalVoList = animalDatabase?.animalDAO()?.getAllAnimal()
            val animalViewModelList = mutableListOf<AnimalViewModel>()
            animalVoList?.forEach {
                val animalType = when (it.animalType) {
                    AnimalType.ANIMAL_TYPE_DOG.typeNumber -> AnimalType.ANIMAL_TYPE_DOG
                    AnimalType.ANIMAL_TYPE_CAT.typeNumber -> AnimalType.ANIMAL_TYPE_CAT
                    else -> AnimalType.ANIMAL_TYPE_PARROT
                }
                val animalGender = when (it.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE.genderNumber -> AnimalGender.ANIMAL_GENDER_MALE
                    else -> AnimalGender.ANIMAL_GENDER_FEMALE
                }
                val animalIdx = it.animalIdx
                val animalName = it.animalName
                val animalAge = it.animalAge
                val animalBirth = it.animalBirth

                val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName, animalAge, animalGender, animalBirth!!)
                animalViewModelList.add(animalViewModel)
            }
            return animalViewModelList
        }

        // Get Sort AnimalInfo
        fun sortAnimalInfo(context: Context, animalType: AnimalType): MutableList<AnimalViewModel> {
            val animalDatabase = AnimalDatabase.getInstance(context)
            val animalVoList = animalDatabase?.animalDAO()?.getSortAnimal(animalType.typeNumber)
            val animalViewModelList = mutableListOf<AnimalViewModel>()
            animalVoList?.forEach {
                val animalTypeStr = when (it.animalType) {
                    AnimalType.ANIMAL_TYPE_DOG.typeNumber -> AnimalType.ANIMAL_TYPE_DOG
                    AnimalType.ANIMAL_TYPE_CAT.typeNumber -> AnimalType.ANIMAL_TYPE_CAT
                    else -> AnimalType.ANIMAL_TYPE_PARROT
                }
                val animalGender = when (it.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE.genderNumber -> AnimalGender.ANIMAL_GENDER_MALE
                    else -> AnimalGender.ANIMAL_GENDER_FEMALE
                }
                val animalIdx = it.animalIdx
                val animalName = it.animalName
                val animalAge = it.animalAge
                val animalBirth = it.animalBirth

                val animalViewModel = AnimalViewModel(animalIdx, animalTypeStr, animalName, animalAge, animalGender, animalBirth!!)
                animalViewModelList.add(animalViewModel)
            }
            return animalViewModelList
        }


        // Get a AnimalInfo by Index
        fun selectAnimalInfoByAnimalIdx(context: Context, animalIdx: Int): AnimalViewModel {
            val animalDatabase = AnimalDatabase.getInstance(context)
            val animalVoList = animalDatabase?.animalDAO()?.selectAnimalDataByAnimalIdx(animalIdx)

            val animalType = when (animalVoList?.animalType) {
                AnimalType.ANIMAL_TYPE_DOG.typeNumber -> AnimalType.ANIMAL_TYPE_DOG
                AnimalType.ANIMAL_TYPE_CAT.typeNumber -> AnimalType.ANIMAL_TYPE_CAT
                else -> AnimalType.ANIMAL_TYPE_PARROT
            }
            val animalGender = when (animalVoList?.animalGender) {
                AnimalGender.ANIMAL_GENDER_MALE.genderNumber -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }
            val animalName = animalVoList?.animalName
            val animalAge = animalVoList?.animalAge
            val animalBirth = animalVoList?.animalBirth
            val animalViewModel = AnimalViewModel(
                animalIdx, animalType, animalName!!,
                animalAge!!, animalGender, animalBirth!!
            )
            return animalViewModel
        }

        // Delete a AnimalInfo by Index
        fun deleteAnimalData(context: Context, animalIdx: Int) {
            val animalDatabase = AnimalDatabase.getInstance(context)
            val animalVO = AnimalVO(animalIdx = animalIdx)

            animalDatabase?.animalDAO()?.deleteAnimalData(animalVO)
        }

        // Modify AnimalInfo
        fun updateStudentInfo(context: Context, animalViewModel: AnimalViewModel) {
            val studentDatabase = AnimalDatabase.getInstance(context)

            val studentIdx = animalViewModel.animalIdx
            val animalType = animalViewModel.animalType.typeNumber
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.genderNumber
            val animalBirth = animalViewModel.animalBirth
            val animalVO = AnimalVO(studentIdx, animalType, animalName, animalAge, animalGender, animalBirth)

            studentDatabase?.animalDAO()?.updateAnimal(animalVO)
        }

    }
}