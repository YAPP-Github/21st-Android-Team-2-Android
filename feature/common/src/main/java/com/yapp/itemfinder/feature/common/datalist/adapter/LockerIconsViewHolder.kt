package com.yapp.itemfinder.feature.common.datalist.adapter

import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import com.yapp.itemfinder.domain.model.LockerIcons
import com.yapp.itemfinder.feature.common.databinding.LockerIconsBinding

class LockerIconsViewHolder(
    val binding: LockerIconsBinding
) : DataViewHolder<LockerIcons>(binding) {

    private val radioGroup1: RadioGroup = binding.radioGroup1
    private val radioGroup2: RadioGroup = binding.radioGroup2

    override fun reset() {
        return
    }

    override fun bindViews(data: LockerIcons) {

        radioGroup1.setOnCheckedChangeListener(RadioGroup1Listener())
        radioGroup2.setOnCheckedChangeListener(RadioGroup2Listener())

    }

    inner class RadioGroup1Listener : OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
            if (checkedId != -1) {
                radioGroup2.setOnCheckedChangeListener(null)
                radioGroup2.clearCheck()
                radioGroup2.setOnCheckedChangeListener(RadioGroup2Listener())
            }
        }
    }

    inner class RadioGroup2Listener : OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
            if (checkedId != -1) {
                radioGroup1.setOnCheckedChangeListener(null)
                radioGroup1.clearCheck()
                radioGroup1.setOnCheckedChangeListener(RadioGroup1Listener())
            }
        }
    }
}
