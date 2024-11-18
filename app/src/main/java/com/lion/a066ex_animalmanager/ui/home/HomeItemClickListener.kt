package com.lion.a066ex_animalmanager.ui.home

import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel

interface HomeItemClickListener {
    fun onAnimalInfoClick(animalViewModel: AnimalViewModel, pos: Int)
}