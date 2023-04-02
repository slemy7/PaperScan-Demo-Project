package com.example.myapplication.extensions

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.viewBinding(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        init {
            // observe the View Lifecycle of the Fragment
            this@viewBinding
                .viewLifecycleOwnerLiveData
                .observe(this@viewBinding) { owner: LifecycleOwner? ->
                    owner?.lifecycle?.addObserver(this)
                }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>
        ): T {
            return binding ?: error("Called before onCreateView or during / after onDestroyView.")
        }

        override fun setValue(
            thisRef: Fragment,
            property: KProperty<*>,
            value: T
        ) {
            binding = value
            (binding as? ViewDataBinding)?.lifecycleOwner = thisRef.viewLifecycleOwner
        }
    }
