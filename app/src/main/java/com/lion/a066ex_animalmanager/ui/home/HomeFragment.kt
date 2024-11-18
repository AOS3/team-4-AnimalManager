package com.lion.a066ex_animalmanager.ui.home

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.data.dao.AnimalDatabase
import com.lion.a066ex_animalmanager.data.repo.AnimalRepo
import com.lion.a066ex_animalmanager.data.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.databinding.FragmentHomeBinding
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date


class HomeFragment : Fragment(), HomeItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var animalList = mutableListOf<AnimalViewModel>()
    private lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        onClickFab()
        settingRecyclerView()
        refreshRecyclerViewMain()
        onClickToolbar()
        onClickSidebar()
    }

    private fun settingRecyclerView() {
        binding.rvFragmentHome.apply {
            adapter = HomeAnimalListAdapter(animalList, this@HomeFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun refreshRecyclerViewMain() {
        viewLifecycleOwner.lifecycleScope.launch {
            val animals = async(Dispatchers.IO) {
                AnimalRepo.selectAnimalInfoAll(requireContext())
            }.await()

            animalList.clear()
            animalList.addAll(animals)
            binding.rvFragmentHome.adapter?.notifyDataSetChanged()


            binding.apply {
                if (animalList.isEmpty()) {
                    groupFragmentHomeNoData.visibility = View.VISIBLE
                } else {
                    groupFragmentHomeNoData.visibility = View.GONE
                }
            }
        }
    }

    private fun refreshRecyclerViewSort(animalType: AnimalType) {
        viewLifecycleOwner.lifecycleScope.launch {
            val sortAnimals = async(Dispatchers.IO) {
                AnimalRepo.sortAnimalInfo(requireContext(), animalType)
            }.await()

            animalList.clear()
            animalList.addAll(sortAnimals)
            binding.rvFragmentHome.adapter?.notifyDataSetChanged()

            binding.apply {
                if (animalList.isEmpty()) {
                    groupFragmentHomeNoData.visibility = View.VISIBLE
                } else {
                    groupFragmentHomeNoData.visibility = View.GONE
                }
            }
        }
    }

    private fun onClickFab() {
        binding.fabFragmentHome.setOnClickListener {
            mainActivity.replaceFragment(FragmentName.ADD_FRAGMENT, true, null)
        }
    }

    private fun onClickToolbar() {
        binding.toolbarFragmentHome.apply {
            setNavigationOnClickListener {
                binding.drawerLayoutFragmentHome.openDrawer(GravityCompat.START)
            }
            setOnMenuItemClickListener { menuId ->
                when (menuId.itemId) {
                    R.id.menu_home_fragment_toolbar_delete -> settingDialog()
                }
                true
            }
        }
    }

    private fun deleteAnimalInfoAll() {
        if (animalList.isEmpty()) {
            Toast.makeText(requireContext(), "동물 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                async(Dispatchers.IO) {
                    AnimalRepo.deleteAnimalInfoAll(requireContext())
                }.await()

                animalList.clear()
                binding.rvFragmentHome.adapter?.notifyDataSetChanged()

                binding.apply {
                    if (animalList.isEmpty()) {
                        groupFragmentHomeNoData.visibility = View.VISIBLE
                    } else {
                        groupFragmentHomeNoData.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun onClickSidebar() {
        binding.apply {
            sideBarFragmentHome.setNavigationItemSelectedListener { menuId ->
                val animalType = AnimalType.fromMenuId(menuId.itemId)
                when (menuId.itemId) {
                    R.id.menu_home_fragment_sidebar_show_all -> {
                        toolbarFragmentHome.title = "동물 전체 정보"
                        refreshRecyclerViewMain()
                    }

                    R.id.menu_home_fragment_sidebar_show_dog,
                    R.id.menu_home_fragment_sidebar_show_cat,
                    R.id.menu_home_fragment_sidebar_show_parrot -> {
                        animalType?.let {
                            toolbarFragmentHome.title = "${it.typeString} 정보"
                            refreshRecyclerViewSort(it)
                        }
                    }
                }
                drawerLayoutFragmentHome.closeDrawer(GravityCompat.START)
                true
            }
        }
    }

    private fun settingDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlterDialog)
        materialAlertDialogBuilder.setTitle("전체 삭제")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다.")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제") { _: DialogInterface, _: Int ->
            deleteAnimalInfoAll()
        }
        materialAlertDialogBuilder.show()

    }

    override fun onAnimalInfoClick(animalViewModel: AnimalViewModel, pos: Int) {
        val dataBundle = Bundle()
        dataBundle.putInt("animalIdx", animalList[pos].animalIdx)
        mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
    }


}