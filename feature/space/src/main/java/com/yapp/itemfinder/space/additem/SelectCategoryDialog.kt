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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (arguments?.getString(SELECTED_CATEGORY_KEY)) {
            ItemCategorySelection.LIFE.label -> binding.lifeRadioButton.isChecked = true
            ItemCategorySelection.FOOD.label -> binding.foodRadioButton.isChecked = true
            ItemCategorySelection.FASHION.label -> binding.fashionRadioButton.isChecked = true
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val category = when (binding.categoryRadioGroup.checkedRadioButtonId) {
            R.id.lifeRadioButton -> ItemCategorySelection.LIFE
            R.id.foodRadioButton -> ItemCategorySelection.FOOD
            R.id.fashionRadioButton -> ItemCategorySelection.FASHION
            else -> {
                ItemCategorySelection.DEFAULT
            }
        }
        setCheckedCategory(category)
    }

    private fun setCheckedCategory(category: ItemCategorySelection) {
        setFragmentResult(
            AddItemActivity.CHECKED_CATEGORY_REQUEST_KEY,
            bundleOf(AddItemActivity.CHECKED_CATEGORY_KEY to category.label)
        )
    }

    companion object {
        const val SELECTED_CATEGORY_KEY = "SELECTED_CATEGORY_KEY"
        fun getInstance() = SelectCategoryDialog()
    }
}
