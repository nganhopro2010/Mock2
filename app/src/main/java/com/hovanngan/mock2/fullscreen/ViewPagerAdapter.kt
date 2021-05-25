package com.hovanngan.mock2.fullscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.hovanngan.mock2.R
import com.hovanngan.mock2.model.Image

class ViewPagerAdapter(private val images: ArrayList<Image>, val itemListener: (isSelected: Boolean) -> Unit) :
    PagerAdapter() {
    private var isSelected: Boolean = false

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.image_full_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(container.context).load(images[position].uri!!.toUri()).into(imageView);
        imageView.setOnClickListener {
            isSelected = !isSelected
            itemListener(isSelected)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
