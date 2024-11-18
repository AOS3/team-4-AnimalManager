package com.lion.a066ex_animalmanager.ui.show

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.data.repo.AnimalRepo
import com.lion.a066ex_animalmanager.databinding.FragmentShowBinding
import com.lion.a066ex_animalmanager.ui.extensions.formatToString
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowFragment : Fragment() {
    private var _binding: FragmentShowBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowBinding.inflate(inflater, container, false)
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
        settingTextView()
    }

    private fun onClickToolbar() {
        binding.toolbarFragmentShow.apply {
            setNavigationOnClickListener { removeFragment() }
            setOnMenuItemClickListener { menuId ->
                when (menuId.itemId) {
                    R.id.menu_show_fragment_toolbar_remove -> settingDialog()
                    R.id.menu_show_fragment_toolbar_modify -> replaceFragment()
                }
                true
            }
        }
    }

    private fun settingDialog() {
        // 다이얼 로그를 띄워주다
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(),R.style.CustomAlterDialog)
        materialAlertDialogBuilder.setTitle("삭제")
        materialAlertDialogBuilder.setMessage("삭제를 할 경우 복원이 불가능합니다.")
        materialAlertDialogBuilder.setNegativeButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제") { _: DialogInterface, _: Int ->
            onClickDeleteBtn()
        }
        materialAlertDialogBuilder.show()
    }

    private fun onClickDeleteBtn() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                val animalIdx = arguments?.getInt("animalIdx")
                AnimalRepo.deleteAnimalData(requireContext(), animalIdx!!)
            }
            work1.join()
            removeFragment()
        }
    }

    private fun settingTextView() {
        val animalIdx = arguments?.getInt("animalIdx")
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                AnimalRepo.selectAnimalInfoByAnimalIdx(requireContext(), animalIdx!!)
            }
            val animalViewModel = work1.await()
            binding.apply {
                tvFragmentShowAnimalName.text = animalViewModel.animalName
                tvFragmentShowAnimalAge.text = animalViewModel.animalAge.toString()
                tvFragmentShowAnimalGender.text = animalViewModel.animalGender.genderType
                tvFragmentShowAnimalCategory.text = animalViewModel.animalType.typeString
                tvFragmentShowAnimalBirth.text = animalViewModel.animalBirth.formatToString("yyyy년 MM월 dd일")

            }
        }
    }

    private fun removeFragment() {
        mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
    }

    private fun replaceFragment() {
        val dataBundle = Bundle()
        val animalIdx = arguments?.getInt("animalIdx")
        dataBundle.putInt("animalIdx", animalIdx!!)
        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, dataBundle)
    }
}