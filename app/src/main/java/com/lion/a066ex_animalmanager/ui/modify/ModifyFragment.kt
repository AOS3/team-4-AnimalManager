package com.lion.a066ex_animalmanager.ui.modify

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.data.dao.AnimalDatabase
import com.lion.a066ex_animalmanager.data.repo.AnimalRepo
import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.databinding.FragmentModifyBinding
import com.lion.a066ex_animalmanager.ui.extensions.formatToString
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date


class ModifyFragment : Fragment() {
    private var _binding: FragmentModifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private var selectedBirthDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyBinding.inflate(inflater, container, false)
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
        settingToolbar()
        settingTextView()
    }

    private fun settingTextView() {
        binding.apply {
            val animalIdx = arguments?.getInt("animalIdx")!!
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    AnimalRepo.selectAnimalInfoByAnimalIdx(requireContext(), animalIdx)
                }
                val animalViewModel = work1.await()

                when (animalViewModel.animalType) {
                    AnimalType.ANIMAL_TYPE_DOG -> {
                        btnGroupFragmentModifyCategory.check(R.id.toggle_category_dog)
                    }

                    AnimalType.ANIMAL_TYPE_CAT -> {
                        btnGroupFragmentModifyCategory.check(R.id.toggle_category_cat)
                    }

                    AnimalType.ANIMAL_TYPE_PARROT -> {
                        btnGroupFragmentModifyCategory.check(R.id.toggle_category_parrot)
                    }
                }
                when (animalViewModel.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE -> {
                        radioGroupFragmentModify.check(R.id.radio_btn_fragment_modify_male)
                    }

                    AnimalGender.ANIMAL_GENDER_FEMALE -> {
                        radioGroupFragmentModify.check(R.id.radio_btn_fragment_modify_female)
                    }
                }
                tfFragmentModifyName.editText?.setText(animalViewModel.animalName)
                tfFragmentModifyAge.editText?.setText("${animalViewModel.animalAge}")
                tvFragmentModifyAnimalBirth.text = animalViewModel.animalBirth.formatToString()
                selectedBirthDate = animalViewModel.animalBirth
                onClickBirthBtn(animalViewModel)
            }
        }
    }

    private fun settingToolbar() {
        binding.toolbarFragmentModify.apply {
            setOnMenuItemClickListener { menuId ->
                when (menuId.itemId) {
                    R.id.menu_home_modify_fragment_toolbar_save -> updateRoomDB()
                }
                true
            }
            setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    private fun updateRoomDB() {
        val animalType = getAnimalType()
        val animalName = getAnimalName()
        val animalAge = getAnimalAge()
        val animalGender = getAnimalGender()
        val animalBirth = getAnimalBirth()

        checkUpdateData(animalType, animalName, animalAge, animalGender, animalBirth)

    }

    private fun checkUpdateData(animalType: AnimalType, animalName: String, animalAge: String, animalGender: AnimalGender, animalBirth: Date?) {
        binding.apply {
            var isName = false
            var isAge = false
            var isDate = false
            if (animalName.isEmpty()) {
                tfFragmentModifyName.isErrorEnabled = true
                tfFragmentModifyName.error = "이름을 입력하세요"
            } else {
                tfFragmentModifyName.isErrorEnabled = false
                isName = true
            }
            if (animalAge.isEmpty() || animalAge.toInt() == 0 || animalAge.toInt() > 150) {
                tfFragmentModifyAge.isErrorEnabled = true
                tfFragmentModifyAge.error = "나이를 입력하세요 (최소 1살, 최대 150살)"
            } else {
                isAge = true
                tfFragmentModifyAge.isErrorEnabled = false
            }
            if (animalBirth == null) {
                tvFragmentModifyAnimalBirthError.visibility = View.VISIBLE
            } else {
                isDate = true
                tvFragmentModifyAnimalBirthError.visibility = View.GONE
            }


            if (isName && isAge && isDate) {
                settingDialog(animalType, animalName, animalAge, animalGender, animalBirth!!)
                // insert

            }
        }
    }

    private fun updateClearRoomDB(animalType: AnimalType, animalName: String, animalAge: String, animalGender: AnimalGender, animalBirth: Date?) {
        val animalIdx = arguments?.getInt("animalIdx")!!
        val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName, animalAge.toInt(), animalGender, animalBirth!!)
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                AnimalRepo.updateStudentInfo(requireContext(), animalViewModel)
            }
            work1.join()
            removeFragment()
        }
    }

    private fun getAnimalType(): AnimalType {
        binding.apply {
            return when (btnGroupFragmentModifyCategory.checkedButtonId) {
                R.id.btn_category_dog -> AnimalType.ANIMAL_TYPE_DOG
                R.id.btn_category_cat -> AnimalType.ANIMAL_TYPE_CAT
                else -> AnimalType.ANIMAL_TYPE_PARROT
            }
        }
    }

    private fun getAnimalName() = binding.tfFragmentModifyName.editText?.text.toString()

    private fun getAnimalAge() = binding.tfFragmentModifyAge.editText?.text.toString()

    private fun getAnimalGender(): AnimalGender {
        binding.apply {
            return when (radioGroupFragmentModify.checkedRadioButtonId) {
                R.id.radio_btn_fragment_add_male -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }
        }
    }

    private fun getAnimalBirth() = selectedBirthDate

    private fun onClickBirthBtn(animalViewModel: AnimalViewModel) {
        binding.apply {
            btnFragmentModifyBirth.setOnClickListener {
                val calendar = Calendar.getInstance()
                selectedBirthDate = selectedBirthDate ?: animalViewModel.animalBirth.also {
                    calendar.time = it
                }
                val getYear = calendar.get(Calendar.YEAR)
                val getMonth = calendar.get(Calendar.MONTH)
                val getDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedBirthDate = calendar.time

                    tvFragmentModifyAnimalBirth.text = selectedBirthDate?.formatToString()
                    tvFragmentModifyAnimalBirthError.visibility = View.GONE
                }
                val pickerDialog = DatePickerDialog(requireContext(), datePicker, getYear, getMonth, getDay)
                pickerDialog.show()
            }
        }
    }

    private fun settingDialog(animalType: AnimalType, animalName: String, animalAge: String, animalGender: AnimalGender, animalBirth: Date?) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlterDialog)
        materialAlertDialogBuilder.setTitle("수정")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다.")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("수정") { _: DialogInterface, _: Int ->
            AnimalDatabase.getInstance(requireContext())

            updateClearRoomDB(animalType, animalName, animalAge, animalGender, animalBirth)
        }
        materialAlertDialogBuilder.show()

    }

    private fun removeFragment() {
        mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
    }


}