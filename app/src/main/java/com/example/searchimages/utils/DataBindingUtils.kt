package com.example.searchimages.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("app:loadImage", "app:thumbnail", requireAll = false)
fun loadImage(imageView: ImageView, url: String, thumbnail: String?) {
    Glide.with(imageView)
        .load(url)
        .thumbnail(Glide.with(imageView).load(thumbnail))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

@BindingAdapter("app:loadTags")
fun loadTags(chipGroup: ChipGroup, tags: List<String>) {
    chipGroup.removeAllViews()
    val context = chipGroup.context
    tags.forEach {
        chipGroup.addView(Chip(context).apply {
            text = it
            isClickable = false
            isFocusable = false
        })
    }
}

@BindingAdapter("app:visibleOrGone")
fun visibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
