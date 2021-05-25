package com.hovanngan.mock2.fullscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.hovanngan.mock2.ImagesAdapter
import com.hovanngan.mock2.MainActivity
import com.hovanngan.mock2.R
import com.hovanngan.mock2.databinding.ActivityFullScreenBinding
import com.hovanngan.mock2.model.Image


class FullScreenActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFullScreenBinding
    private var data = ArrayList<Image>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var adapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen)
        initComponent()
        listener()

    }

    private fun initComponent() {
        val bundle = intent.extras
        val images: ArrayList<Image>? = bundle!!.getParcelableArrayList(MainActivity.EXTRA_IMAGES)
        data.addAll(images!!)
        viewPagerAdapter = ViewPagerAdapter(data) { isSelected ->
            if (isSelected) {
                mBinding.rvImagesHorizontal.visibility = View.GONE
            } else {
                mBinding.rvImagesHorizontal.visibility = View.VISIBLE
            }

        }
        mBinding.pager.adapter = viewPagerAdapter

        adapter = ImagesAdapter(true) { position, _ ->
            mBinding.pager.setCurrentItem(position, false)
        }
        mBinding.rvImagesHorizontal.adapter = adapter
        adapter.setData(images)
        if (intent.hasExtra(MainActivity.EXTRA_POSITION)) {
            val position = intent.getIntExtra(MainActivity.EXTRA_POSITION, 0)
            mBinding.pager.currentItem = position
            mBinding.rvImagesHorizontal.postDelayed({
                mBinding.rvImagesHorizontal.scrollToPosition(position)
            }, 100)
            adapter.setPosition(position)
        }
    }

    private fun listener() {
        mBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.rvImagesHorizontal.smoothScrollToPosition(position)
                adapter.setPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }
}
