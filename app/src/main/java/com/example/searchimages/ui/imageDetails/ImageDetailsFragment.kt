package com.example.searchimages.ui.imageDetails

import com.example.searchimages.databinding.FragmentImageDetailsBinding
import com.example.searchimages.ui.base.BaseFragment
import com.example.searchimages.ui.searchImages.SearchImagesViewModel
import com.example.searchimages.utils.Layouts

class ImageDetailsFragment :
    BaseFragment<FragmentImageDetailsBinding, SearchImagesViewModel>() {
    override var layoutId: Int = Layouts.fragment_image_details

    override fun getViewModel(): Nothing? = null

    override fun initView() {
        binding.imageDetail = ImageDetailsFragmentArgs.fromBundle(requireArguments()).imageDetails
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
    }

    override fun initObserver() {}
}