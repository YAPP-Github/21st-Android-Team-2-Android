package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import com.yapp.itemfinder.domain.model.LockerIconId
import com.yapp.itemfinder.domain.model.LockerIcons
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LockerIconsBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class LockerIconsViewHolder(
    val binding: LockerIconsBinding
) : DataViewHolder<LockerIcons>(binding) {

    private val radioGroup1: RadioGroup = binding.radioGroup1
    private val radioGroup2: RadioGroup = binding.radioGroup2

    override fun reset() {
        return
    }

    override fun bindViews(data: LockerIcons) {

        radioGroup1.setOnCheckedChangeListener(RadioGroup1Listener(data))
        radioGroup2.setOnCheckedChangeListener(RadioGroup2Listener(data))

        if (data.mode == ScreenMode.EDIT_MODE) {
            binding.apply {
                radioGroup1.clearCheck()
                radioGroup2.clearCheck()
                when (data.icon) {
                    LockerIconId.LOCKER_ICON1.iconId -> radioButton1.isChecked = true
                    LockerIconId.LOCKER_ICON2.iconId -> radioButton2.isChecked = true
                    LockerIconId.LOCKER_ICON3.iconId -> radioButton3.isChecked = true
                    LockerIconId.LOCKER_ICON4.iconId -> radioButton4.isChecked = true
                    LockerIconId.LOCKER_ICON5.iconId -> radioButton5.isChecked = true
                    LockerIconId.LOCKER_ICON6.iconId -> radioButton6.isChecked = true
                    LockerIconId.LOCKER_ICON7.iconId -> radioButton7.isChecked = true
                    LockerIconId.LOCKER_ICON8.iconId -> radioButton8.isChecked = true
                }
            }
        }

    }

    inner class RadioGroup1Listener(val data: LockerIcons) : OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
            if (checkedId != -1) {
                radioGroup2.setOnCheckedChangeListener(null)
                radioGroup2.clearCheck()
                radioGroup2.setOnCheckedChangeListener(RadioGroup2Listener(data))
                val icon = when (checkedId) {
                    R.id.radioButton1 -> LockerIconId.LOCKER_ICON1.iconId
                    R.id.radioButton2 -> LockerIconId.LOCKER_ICON2.iconId
                    R.id.radioButton3 -> LockerIconId.LOCKER_ICON3.iconId
                    else -> LockerIconId.LOCKER_ICON4.iconId
                }
                data.changeIcon(icon)
            }
        }
    }

    inner class RadioGroup2Listener(val data: LockerIcons) : OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
            if (checkedId != -1) {
                radioGroup1.setOnCheckedChangeListener(null)
                radioGroup1.clearCheck()
                radioGroup1.setOnCheckedChangeListener(RadioGroup1Listener(data))
                val icon = when (checkedId) {
                    R.id.radioButton5 -> LockerIconId.LOCKER_ICON5.iconId
                    R.id.radioButton6 -> LockerIconId.LOCKER_ICON6.iconId
                    R.id.radioButton7 -> LockerIconId.LOCKER_ICON7.iconId
                    else -> LockerIconId.LOCKER_ICON8.iconId
                }
                data.changeIcon(icon)
            }
        }
    }
}
