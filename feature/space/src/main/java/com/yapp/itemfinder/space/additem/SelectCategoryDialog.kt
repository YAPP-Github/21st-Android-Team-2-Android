package com.yapp.itemfinder.space.additem

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.domain.model.ItemCategorySelection
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.SelectCategoryDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryDialog : DialogFragment() {
    private var _binding: SelectCategoryDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectCategoryDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val categoryLabel = when (binding.categoryRadioGroup.checkedRadioButtonId) {
            R.id.lifeRadioButton -> ItemCategorySelection.LIFE.label
            R.id.foodRadioButton -> ItemCategorySelection.FOOD.label
            R.id.fashionRadioButton -> ItemCategorySelection.FASHION.label
            else -> {
                ItemCategorySelection.DEFAULT.label
            }
        }
        setCheckedCategory(categoryLabel)
    }

    private fun setCheckedCategory(categoryName: String) {
        setFragmentResult(
            AddItemActivity.CHECKED_CATEGORY_REQUEST_KEY,
            bundleOf(AddItemActivity.CHECKED_CATEGORY_KEY to categoryName)
        )
    }

    companion object {
        fun getInstance() = SelectCategoryDialog()
    }
}
