package com.example.searchimages.ui.searchImages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.searchimages.databinding.ItemImageDetailsBinding
import com.example.searchimages.ui.base.BaseViewHolder
import com.example.searchimages.ui.models.ImageItemUIModel
import java.lang.ref.WeakReference


class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ItemImageViewHolder>() {
    private val dataList = ArrayList<ImageItemUIModel>()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ItemImageViewHolder {
        val binding =
            ItemImageDetailsBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ItemImageViewHolder(binding, WeakReference(listener))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.onBind(position, dataList[position])
    }

    fun setData(newDataList: List<ImageItemUIModel>) {
        val diffCallback = DiffCallback(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataList.clear()
        dataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(item: ImageItemUIModel)
    }


    class ItemImageViewHolder(
        private val binding: ItemImageDetailsBinding,
        private val onItemClickListener: WeakReference<OnItemClickListener?>
    ) : BaseViewHolder<ImageItemUIModel>(binding.root) {
        override fun onBind(position: Int, data: ImageItemUIModel) {
            binding.itemModel = data
            binding.listener = onItemClickListener.get()
            binding.executePendingBindings()
        }
    }


    private class DiffCallback(
        private val oldList: List<ImageItemUIModel>,
        private val newList: List<ImageItemUIModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
