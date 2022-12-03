package com.yapp.itemfinder.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.ActivityHomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>() {

    override val vm by viewModels<HomeViewModel>()

    override val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val navigationId = item.itemId
            when (navigationId) {
                R.id.menu_home -> {
                    true
                }
                R.id.menu_space -> {
                    true
                }
                R.id.menu_product -> {
                    true
                }
                R.id.menu_like -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {

    }

    companion object {

        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }

}
