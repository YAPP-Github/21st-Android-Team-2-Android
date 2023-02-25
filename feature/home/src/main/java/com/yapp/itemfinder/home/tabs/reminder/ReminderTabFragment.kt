package com.yapp.itemfinder.home.tabs.reminder

import androidx.fragment.app.viewModels
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.feature.home.databinding.FragmentReminderTabBinding
import com.yapp.itemfinder.home.settings.SettingsActivity
import kotlinx.coroutines.Job

class ReminderTabFragment: BaseFragment<ReminderTabViewModel, FragmentReminderTabBinding>() {

    override val vm by viewModels<ReminderTabViewModel>()

    override val binding by viewBinding(FragmentReminderTabBinding::inflate)

    override val depth: Depth
        get() = Depth.FIRST

    override fun initViews() = with(binding) {
        initToolBar()
    }

    private fun initToolBar() = with(binding.searchTopNavigationView) {
        leftButtonImageResId = R.drawable.ic_menu
        searchBarImageResId = R.drawable.ic_search
        searchBarBackgroundResId = R.drawable.bg_button_brown_02_radius_8
        searchBarText = getString(com.yapp.itemfinder.feature.home.R.string.home_search_bar_text)
        searchBarTextColor = R.color.gray_03
        leftButtonClickListener = {
            startActivity(
                SettingsActivity.newIntent(requireContext())
            )
        }
        searchBarClickListener = {
            requireContext().showShortToast(getString(com.yapp.itemfinder.feature.home.R.string.prepare_this_feature))
        }
    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        val TAG = ReminderTabFragment::class.simpleName.toString()

        fun newInstance() = ReminderTabFragment()

    }

}
