package com.example.searchimages.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.searchimages.utils.Strings

abstract class BaseFragment<L : ViewBinding, T : BaseViewModel> : Fragment(), ViewRule {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
        getViewModel()?.getEvents()?.observe(viewLifecycleOwner) {
            it?.let {
                if (it is Result.Success) {
                    Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                } else
                    if (it is Result.Error) {
                        Toast.makeText(
                            context,
                            getString(Strings.error_placeholder, it.error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getViewModel(): T?
    abstract var layoutId: Int
    private var _binding: L? = null
    val binding: L get() = _binding!!
}