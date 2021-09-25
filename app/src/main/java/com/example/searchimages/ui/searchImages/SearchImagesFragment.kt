package com.example.searchimages.ui.searchImages

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchimages.databinding.FragmentSearchImagesBinding
import com.example.searchimages.ui.base.BaseFragment
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.Layouts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchImagesFragment :
    BaseFragment<FragmentSearchImagesBinding, SearchImagesViewModel>(),
    ImagesAdapter.OnItemClickListener {
    override var layoutId: Int = Layouts.fragment_search_images

    private val searchImagesViewModel: SearchImagesViewModel by viewModels()

    override fun getViewModel() = searchImagesViewModel
    private var imagesAdapter: ImagesAdapter? = null

    override fun onDestroyView() {
        super.onDestroyView()
        imagesAdapter = null
    }

    override fun initView() {
        binding.viewModel = searchImagesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        imagesAdapter = ImagesAdapter()
        imagesAdapter?.listener = this
        with(binding) {
            rvImages.layoutManager = LinearLayoutManager(context)
            rvImages.adapter = imagesAdapter
        }
    }

    override fun initObserver() {
        with(binding) {
            searchImagesViewModel.searchList.observe(viewLifecycleOwner) {
                it?.let {
                    rvImages.tag = false
                    imagesAdapter?.setData(it)
                }
            }

            rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        (recyclerView.layoutManager as? LinearLayoutManager)?.let { lm ->
                            if (recyclerView.tag == false) {
                                if ((lm.childCount + lm.findFirstVisibleItemPosition()) >= lm.itemCount) {
                                    searchImagesViewModel.searchImages()
                                    recyclerView.tag = true
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onItemClick(item: ImageItemUIModel) {
        val direction =
            SearchImagesFragmentDirections.actionSearchImagesFragmentToImageDetailsFragment(item)
        findNavController().navigate(direction)
    }
}