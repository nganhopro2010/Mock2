package com.hovanngan.mock2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hovanngan.mock2.databinding.ImageItemBinding
import com.hovanngan.mock2.databinding.ImageItemHorizontalBinding
import com.hovanngan.mock2.model.Image

class ImagesAdapter(
    private var isHorizontal: Boolean,
    var itemSelectedListener: (position: Int, data: ArrayList<Image>) -> Unit
) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private val data = ArrayList<Image>()
    private var selectedPosition = 0
    fun setPosition(i: Int){
        selectedPosition = i
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mBinding: ImageItemBinding? = null
        var mBindingHorizontal: ImageItemHorizontalBinding? = null

        init {
            if (isHorizontal) {
                mBindingHorizontal = DataBindingUtil.bind(itemView)
                mBindingHorizontal!!.imageViewHorizontal.setOnClickListener {
                    itemSelectedListener(layoutPosition, data)
                    val previousPosition = selectedPosition
                    selectedPosition = layoutPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
            } else {
                mBinding = DataBindingUtil.bind(itemView)
                mBinding!!.imageView.setOnClickListener {
                    itemSelectedListener(layoutPosition, data)
                }
            }

        }
    }

    fun setData(data: ArrayList<Image>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (isHorizontal) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.image_item_horizontal, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        if (isHorizontal) {
            Glide.with(holder.itemView.context).load(item.uri)
                .into(holder.mBindingHorizontal!!.imageViewHorizontal)

            holder.mBindingHorizontal!!.container.isSelected = selectedPosition == position

        } else {
            Glide.with(holder.itemView.context).load(item.uri).into(holder.mBinding!!.imageView)
        }
    }

    override fun getItemCount(): Int = data.size
}
