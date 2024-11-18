package com.lion.a066ex_animalmanager.ui.add

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.data.repo.AnimalRepo
import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.databinding.FragmentAddBinding
import com.lion.a066ex_animalmanager.ui.extensions.formatToString
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    private var selectedBirthDate: Date? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startApp()
    }

    private fun startApp() {
        onClickToolbar()
        onClickBirthBtn()
    }

    private fun onClickToolbar() {
        binding.toolbarFragmentAdd.apply {
            setOnMenuItemClickListener { menuId ->
                when (menuId.itemId) {
                    R.id.menu_home_modify_fragment_toolbar_save -> {
                        insertRoomDB()
                    }
                }
                true
            }
            setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    private fun insertRoomDB() {
        val animalType = getAnimalType()
        val animalName = getAnimalName()
        val animalAge = getAnimalAge()
        val animalGender = getAnimalGender()
        val animalBirth = getAnimalBirth()

        checkInsertData(animalType, animalName, animalAge, animalGender, animalBirth)

    }

    private fun checkInsertData(animalType: AnimalType, animalName: String, animalAge: String, animalGender: AnimalGender, animalBirth: Date?) {
        binding.apply {
            var isName = false
            var isAge = false
            var isDate = false
            if (animalName.isEmpty()) {
                tfFragmentAddName.isErrorEnabled = true
                tfFragmentAddName.error = "이름을 입력하세요"
            } else {
                tfFragmentAddName.isErrorEnabled = false
                isName = true
            }
            if (animalAge.isEmpty() || animalAge.toInt() == 0 || animalAge.toInt() > 150) {
                tfFragmentAddAge.isErrorEnabled = true
                tfFragmentAddAge.error = "나이를 입력하세요 (최소 1살, 최대 150살)"
            } else {
                isAge = true
                tfFragmentAddAge.isErrorEnabled = false
            }
            if (animalBirth == null) {
                tvFragmentAddAnimalBirthError.visibility = View.VISIBLE
            } else {
                isDate = true
                tvFragmentAddAnimalBirthError.visibility = View.GONE
            }

            if (isName && isAge && isDate) {
                // insert
                val animalViewModel = AnimalViewModel(0, animalType, animalName, animalAge.toInt(), animalGender, animalBirth!!)
                viewLifecycleOwner.lifecycleScope.launch {
                    val animals = async(Dispatchers.IO) {
                        AnimalRepo.insertAnimalInfo(requireContext(),animalViewModel)
                    }
                    animals.join()
                    removeFragment()
                }
//                CoroutineScope(Dispatchers.Main).launch {
//                    val work1 = async(Dispatchers.IO) {
//                        AnimalRepo.insertAnimalInfo(mainActivity, animalViewModel)
//                    }
//                    work1.join()
//                    removeFragment()
//                }
            }
        }
    }

    private fun getAnimalType(): AnimalType {
        binding.apply {
            return when (btnGroupFragmentAddCategory.checkedButtonId) {
                R.id.btn_category_dog -> AnimalType.ANIMAL_TYPE_DOG
                R.id.btn_category_cat -> AnimalType.ANIMAL_TYPE_CAT
                else -> AnimalType.ANIMAL_TYPE_PARROT
            }
        }
    }

    private fun getAnimalName() = binding.tfFragmentAddName.editText?.text.toString()

    private fun getAnimalAge() = binding.tfFragmentAddAge.editText?.text.toString()

    private fun getAnimalGender(): AnimalGender {
        binding.apply {
            return when (radioGroupFragmentAdd.checkedRadioButtonId) {
                R.id.radio_btn_fragment_add_male -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }
        }
    }

    private fun getAnimalBirth() = selectedBirthDate


    private fun removeFragment() {
        mainActivity.removeFragment(FragmentName.ADD_FRAGMENT)
    }


    private fun onClickBirthBtn() {
        binding.apply {
            btnFragmentAddBirth.setOnClickListener {
                val calendar = Calendar.getInstance()
                val getYear = calendar.get(Calendar.YEAR)
                val getMonth = calendar.get(Calendar.MONTH)
                val getDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedBirthDate = calendar.time

                    tvFragmentAddAnimalBirth.text = selectedBirthDate?.formatToString()
                    tvFragmentAddAnimalBirthError.visibility = View.GONE
                }
                val pickerDialog = DatePickerDialog(requireContext(), datePicker, getYear, getMonth, getDay)
                pickerDialog.show()
            }
        }
    }
}