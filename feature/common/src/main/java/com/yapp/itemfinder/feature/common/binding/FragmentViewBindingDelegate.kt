package com.yapp.itemfinder.feature.common.binding

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

/**
 * @author SODA1127
 * ViewBinding 사용 시 Delegate 패턴을 이용하여 Fragment에 inflate할 뷰바인딩 인스턴스를 생성함.
 * private val binding: FragmentMainBinding by viewBinding()
 * reference : https://zhuinden.medium.com/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
 */

@JvmName("viewBindingFragment")
inline fun <F : Fragment, T : ViewBinding> F.viewBinding(
    crossinline vbFactory: (LayoutInflater) -> T,
    crossinline inflaterProvider: (F) -> LayoutInflater = Fragment::getLayoutInflater,
): FragmentViewBindingDelegate<F, T> = viewBinding { fragment: F -> vbFactory(inflaterProvider(fragment)) }

fun <F : Fragment, T : ViewBinding> F.viewBinding(
    viewBinder: (F) -> T,
): FragmentViewBindingDelegate<F, T> = FragmentViewBindingDelegate(viewBinder)

class FragmentViewBindingDelegate<F : Fragment, T : ViewBinding>(
    val viewBinder: (F) -> T,
) {

    /**
     * initiate variable for binding view
     */
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: F, property: KProperty<*>): T {
        binding?.let { return it }

        /**
         * Adding observer to the fragment lifecycle
         *
         * 더 이상 LifeCycleObserver 사용하는 경우 Lifecycle-awareness 컴포넌트 사용 전제하에 destroy 시 구독해제를 해줄 필요가 없어짐.
         * @see [https://github.com/googlecodelabs/android-lifecycles/issues/5]
         */
        thisRef.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                thisRef.viewLifecycleOwnerLiveData.observe(thisRef) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            /**
                             * Clear the binding when Fragment lifecycle called the onDestroy
                             *
                             * Fragment에서 Navigation component 또는 BackStack or detach를 사용하는 경우,
                             * onDestroyView() 이후에 Fragment view는 종료되지만, Fragment는 여전히 살아 있습니다.
                             * 즉 메모리 누수가 발생하게 됩니다. 그래서 binding변수를 onDestroyView 이후에 null로 해제해주어야 합니다.
                             *
                             * https://developer.android.com/topic/libraries/view-binding#fragments
                             */
                            binding = null
                        }
                    })
                }
            }
        })


        /**
         * Checking the fragment lifecycle
         */
        val currentState = thisRef.lifecycle.currentState
        if (!currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Unsupported Fragment Lifecycle for ViewBinding Access. Current State: $currentState")
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
