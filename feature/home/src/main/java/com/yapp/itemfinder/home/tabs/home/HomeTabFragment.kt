package com.yapp.itemfinder.home.tabs.home

import androidx.fragment.app.viewModels
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import kotlinx.coroutines.Job

class HomeTabFragment: BaseFragment<HomeTabViewModel, FragmentHomeTabBinding>() {

    override val vm by viewModels<HomeTabViewModel>()

    override val binding by viewBinding(FragmentHomeTabBinding::inflate)

    override fun initViews() = with(binding) {
    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()

    }

}
