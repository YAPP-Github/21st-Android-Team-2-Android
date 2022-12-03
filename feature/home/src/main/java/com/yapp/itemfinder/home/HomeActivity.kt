package com.yapp.itemfinder.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.hideSoftInput
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.ActivityHomeBinding
import com.yapp.itemfinder.home.tabs.home.HomeTabFragment
import com.yapp.itemfinder.home.tabs.like.LikeTabFragment
import com.yapp.itemfinder.home.tabs.reminder.ReminderTabFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>() {

    override val vm by viewModels<HomeViewModel>()

    override val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun initViews() {
        initNavigationBar()
    }

    override fun initState() {
        super.initState()
        vm.changeNavigation(MainNavigation(R.id.menu_home))
    }

    private fun initNavigationBar() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val navigationId = item.itemId
            when (navigationId) {
                R.id.menu_reminder -> {
                    showFragment(ReminderTabFragment.TAG)
                    true
                }
                R.id.menu_home -> {
                    showFragment(HomeTabFragment.TAG)
                    true
                }
                R.id.menu_like -> {
                    showFragment(LikeTabFragment.TAG)
                    true
                }
                else -> false
            }
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        vm.navigationItemStateFlow.collect { navigation ->
            navigation ?: return@collect
            binding.bottomNav.selectedItemId = navigation.navigationMenuId
        }
    }

    private fun showFragment(tag: String) {
        binding.root.hideSoftInput()
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        foundFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            val fragment = getFragmentByTag(tag)
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment, tag)
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun getFragmentByTag(tag: String): Fragment? =
        when (tag) {
            ReminderTabFragment.TAG -> ReminderTabFragment.newInstance()
            HomeTabFragment.TAG -> HomeTabFragment.newInstance()
            LikeTabFragment.TAG -> LikeTabFragment.newInstance()
            else -> null
        }

    companion object {

        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }

}
