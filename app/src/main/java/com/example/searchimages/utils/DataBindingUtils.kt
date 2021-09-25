package com.example.searchimages.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView).load(url).into(imageView)
}

@BindingAdapter("app:loadTags")
fun loadTags(chipGroup: ChipGroup, tags: List<String>) {
    chipGroup.removeAllViews()
    val context = chipGroup.context
    tags.forEach {
        chipGroup.addView(Chip(context).apply { text = it })
    }
}

@BindingAdapter("app:visibleOrGone")
fun visibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
