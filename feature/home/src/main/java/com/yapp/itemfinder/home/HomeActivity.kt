package com.yapp.itemfinder.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.FragmentNavigator
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.hideSoftInput
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.ActivityHomeBinding
import com.yapp.itemfinder.home.tabs.home.HomeTabFragment
import com.yapp.itemfinder.home.tabs.like.LikeTabFragment
import com.yapp.itemfinder.home.tabs.reminder.ReminderTabFragment
import com.yapp.itemfinder.space.LockerListFragment
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import com.yapp.itemfinder.space.managespace.ManageSpaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>(), FragmentNavigator {

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
            when (item.itemId) {
                R.id.menu_tags -> {
                    showFragment(LikeTabFragment.TAG)
                    true
                }
                R.id.menu_home -> {
                    showFragment(HomeTabFragment.TAG)
                    true
                }
                R.id.menu_reminder -> {
                    showFragment(ReminderTabFragment.TAG)
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

    override fun showFragment(tag: String) {
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

    override fun addFragmentBackStack(tag: String, bundle: Bundle?) {
        binding.root.hideSoftInput()
        with(supportFragmentManager) {
            val foundFragment = findFragmentByTag(tag) ?: getFragmentByTag(tag)
            foundFragment?.let {
                it.arguments = Bundle().apply {

                }
                if (bundle != null) {
                    it.arguments = bundle
                }
                beginTransaction()
                    .apply {
                        fragments.forEach { fragment ->
                            if (fragment.isHidden.not())
                                hide(fragment)
                        }
                    }.add(R.id.fragmentContainer, foundFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun getFragmentByTag(tag: String): Fragment? =
        when (tag) {
            ReminderTabFragment.TAG -> ReminderTabFragment.newInstance()
            HomeTabFragment.TAG -> HomeTabFragment.newInstance()
            LikeTabFragment.TAG -> LikeTabFragment.newInstance()
            LockerListFragment.TAG -> LockerListFragment.newInstance()
            ManageSpaceFragment.TAG -> ManageSpaceFragment.newInstance()
            LockerDetailFragment.TAG -> LockerDetailFragment.newInstance()
            else -> null
        }

    companion object {

        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

    }

}
