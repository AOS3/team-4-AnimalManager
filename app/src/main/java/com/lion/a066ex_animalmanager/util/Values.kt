package com.lion.a066ex_animalmanager.util

import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.data.vo.AnimalVO

enum class FragmentName(var number: Int, var str: String) {
    HOME_FRAGMENT(1, "HomeFragment"),
    ADD_FRAGMENT(2, "AddFragment"),
    SHOW_FRAGMENT(3, "ShowFragment"),
    MODIFY_FRAGMENT(4, "ModifyFragment")
}

enum class AnimalType(var typeNumber: Int, var typeString: String) {
    ANIMAL_TYPE_DOG(1, "강아지"),
    ANIMAL_TYPE_CAT(2, "고양이"),
    ANIMAL_TYPE_PARROT(3, "앵무새");

    companion object {
        fun fromMenuId(menuId: Int): AnimalType? {
            return when (menuId) {
                R.id.menu_home_fragment_sidebar_show_dog -> ANIMAL_TYPE_DOG
                R.id.menu_home_fragment_sidebar_show_cat -> ANIMAL_TYPE_CAT
                R.id.menu_home_fragment_sidebar_show_parrot -> ANIMAL_TYPE_PARROT
                else -> null
            }
        }
    }
}

enum class AnimalGender(var genderNumber: Int, var genderType: String) {
    ANIMAL_GENDER_MALE(0, "수컷"),
    ANIMAL_GENDER_FEMALE(1, "암컷"),
}