package com.yapp.itemfinder.home.tabs

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.*
import com.yapp.itemfinder.home.tabs.like.LikeTabFragment
import kotlinx.coroutines.Job

class LockerListFragment: Fragment(){

    companion object {

        val TAG = LockerListFragment::class.simpleName.toString()

        fun newInstance() = LockerListFragment()

    }
}