package com.example.searchimages.ui.searchImages

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchimages.databinding.FragmentSearchImagesBinding
import com.example.searchimages.ui.base.BaseFragment
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.Layouts
import com.example.searchimages.utils.Strings
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

        searchImagesViewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            item?.let { showDetailConfirmationDialog(it) }
        }
    }

    override fun onItemClick(item: ImageItemUIModel) {
        searchImagesViewModel.onImageItemClick(item)
    }

    private fun showDetailConfirmationDialog(item: ImageItemUIModel) {
        AlertDialog.Builder(context).setTitle(Strings.please_confirm)
            .setMessage(Strings.do_you_want_to_view_details)
            .setPositiveButton(Strings.yes) { dialog, _ ->
                openDetailsPage(item)
                dialog.dismiss()
            }
            .setNegativeButton(Strings.cancel) { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener {
                searchImagesViewModel.onDismissClick()
            }.create().show()
    }

    private fun openDetailsPage(item: ImageItemUIModel) {
        val direction =
            SearchImagesFragmentDirections.actionSearchImagesFragmentToImageDetailsFragment(
                item
            )
        findNavController().navigate(direction)
    }
}