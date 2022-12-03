package com.yapp.itemfinder.home.tabs.like

import androidx.fragment.app.viewModels
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentLikeTabBinding
import kotlinx.coroutines.Job

class LikeTabFragment: BaseFragment<LikeTabViewModel, FragmentLikeTabBinding>() {

    override val vm by viewModels<LikeTabViewModel>()

    override val binding by viewBinding(FragmentLikeTabBinding::inflate)

    override fun initViews() = with(binding) {
    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        val TAG = LikeTabFragment::class.simpleName.toString()

        fun newInstance() = LikeTabFragment()

    }

}
