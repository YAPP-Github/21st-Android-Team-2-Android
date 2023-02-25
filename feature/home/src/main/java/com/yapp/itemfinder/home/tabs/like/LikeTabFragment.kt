package com.yapp.itemfinder.home.tabs.like

import androidx.fragment.app.viewModels
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.FragmentLikeTabBinding
import com.yapp.itemfinder.home.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yapp.itemfinder.feature.common.R as CR

class LikeTabFragment : BaseFragment<LikeTabViewModel, FragmentLikeTabBinding>() {

    override val vm by viewModels<LikeTabViewModel>()

    override val binding by viewBinding(FragmentLikeTabBinding::inflate)

    override val depth: Depth
        get() = Depth.FIRST

    override fun initViews() = with(binding) {
        initToolBar()
    }

    private fun initToolBar() = with(binding.searchTopNavigationView) {
        leftButtonImageResId = CR.drawable.ic_menu
        searchBarImageResId = CR.drawable.ic_search
        searchBarBackgroundResId = CR.drawable.bg_button_brown_02_radius_8
        searchBarText = getString(R.string.home_search_bar_text)
        searchBarTextColor = CR.color.gray_03
        leftButtonClickListener = {
            startActivity(
                SettingsActivity.newIntent(requireContext())
            )
        }
        searchBarClickListener = {
            requireContext().showShortToast(getString(R.string.prepare_this_feature))
        }
    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        val TAG = LikeTabFragment::class.simpleName.toString()

        fun newInstance() = LikeTabFragment()

    }

}
