package com.yapp.itemfinder.feature.common.binding

import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

/**
 * @author SODA1127
 * ViewBinding 사용 시 Delegate 패턴을 이용하여 DialogFragment에 inflate할 뷰바인딩 인스턴스를 생성함.
 * private val binding: DialogMainBinding by viewBinding()
 * reference : https://zhuinden.medium.com/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
 */
inline fun <D : DialogFragment, T : ViewBinding> D.viewBinding(
    crossinline vbFactory: (LayoutInflater) -> T,
    crossinline inflaterProvider: (D) -> LayoutInflater = DialogFragment::getLayoutInflater,
): DialogFragmentViewBindingDelegate<D, T> = viewBinding { dialogFragment: D -> vbFactory(inflaterProvider(dialogFragment)) }

fun <D : DialogFragment, T : ViewBinding> D.viewBinding(
    viewBinder: (D) -> T,
): DialogFragmentViewBindingDelegate<D, T> = DialogFragmentViewBindingDelegate(viewBinder)


class DialogFragmentViewBindingDelegate<D : DialogFragment, T : ViewBinding>(
    val viewBinder: (D) -> T,
) {
    /**
     * initiate variable for binding view
     */
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: D, property: KProperty<*>): T {
        binding?.let { return it }

        /**
         * Checking the BottomSheetDialogFragment lifecycle
         */
        val currentState = thisRef.lifecycle.currentState
        if (!currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Unsupported DialogFragment Lifecycle for ViewBinding Access. Current State: $currentState")
        }

        /**
         * Bind layout
         */
        binding = viewBinder(thisRef)

        /**
         * Return binding layout
         */
        return binding as T
    }
}
