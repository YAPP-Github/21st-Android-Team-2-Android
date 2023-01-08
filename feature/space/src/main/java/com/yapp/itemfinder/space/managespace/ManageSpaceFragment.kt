package com.yapp.itemfinder.space.managespace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yapp.itemfinder.space.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageSpaceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_space, container, false)
    }

    companion object {

        val TAG = ManageSpaceFragment::class.simpleName.toString()

        fun newInstance() = ManageSpaceFragment()

    }
}
