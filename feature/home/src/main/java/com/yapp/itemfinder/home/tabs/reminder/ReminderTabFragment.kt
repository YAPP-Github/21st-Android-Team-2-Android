package com.yapp.itemfinder.home.tabs.reminder

import androidx.fragment.app.viewModels
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentReminderTabBinding
import kotlinx.coroutines.Job

class ReminderTabFragment: BaseFragment<ReminderTabViewModel, FragmentReminderTabBinding>() {

    override val vm by viewModels<ReminderTabViewModel>()

    override val binding by viewBinding(FragmentReminderTabBinding::inflate)

    override fun initViews() = with(binding) {
    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        val TAG = ReminderTabFragment::class.simpleName.toString()

        fun newInstance() = ReminderTabFragment()

    }

}
