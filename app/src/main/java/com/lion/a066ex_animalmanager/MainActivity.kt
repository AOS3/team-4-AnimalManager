package com.lion.a066ex_animalmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.a066ex_animalmanager.databinding.ActivityMainBinding
import com.lion.a066ex_animalmanager.ui.add.AddFragment
import com.lion.a066ex_animalmanager.ui.home.HomeFragment
import com.lion.a066ex_animalmanager.ui.modify.ModifyFragment
import com.lion.a066ex_animalmanager.ui.show.ShowFragment
import com.lion.a066ex_animalmanager.util.FragmentName

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(FragmentName.HOME_FRAGMENT, false, null)
    }


    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack: Boolean, dataBundle: Bundle?) {
        val newFragment = when (fragmentName) {
            FragmentName.HOME_FRAGMENT -> HomeFragment()
            FragmentName.ADD_FRAGMENT -> AddFragment()
            FragmentName.SHOW_FRAGMENT -> ShowFragment()
            FragmentName.MODIFY_FRAGMENT -> ModifyFragment()
        }

        if (dataBundle != null) {
            newFragment.arguments = dataBundle
        }

        supportFragmentManager.commit {
            newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)

            replace(R.id.fragment_container_main, newFragment)
            if (isAddToBackStack) {
                addToBackStack(fragmentName.str)
            }
        }
    }

    fun removeFragment(fragmentName: FragmentName) {
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}