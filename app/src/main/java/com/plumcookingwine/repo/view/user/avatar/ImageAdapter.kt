package com.plumcookingwine.repo.view.user.avatar

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.plumcookingwine.repo.R
import com.plumcookingwine.repo.data.UploadDao
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author kangf
 * @data 2019/9/10
 * @description class ImageAdapter
 */
class ImageAdapter(private val list: List<UploadDao>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var mOnItemClick: OnItemClick? = null

    fun setOnItemClick(add: () -> Unit, click: (Int) -> Unit) {
        mOnItemClick = object : OnItemClick {
            override fun add() {
                add()
            }

            override fun click(pos: Int) {
                click(pos)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageViewHolder {

        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_image, null, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, pos: Int) {

        val iv = holder.iv
        Glide.with(iv)
            .load(list[pos].path)
            .into(iv)



        holder.itemView.setOnClickListener {
            if (pos == list.size - 1) {
                mOnItemClick?.add()
            } else {
                mOnItemClick?.click(pos)
            }
        }

        val showProgress = list[pos].showProgress
        if (showProgress) {
            val uploadSize = list[pos].uploadSize
            val totalSize = list[pos].totalSize
            val percent =
                BigDecimal(uploadSize.toString()).divide(
                    BigDecimal(totalSize.toString()),
                    2,
                    RoundingMode.HALF_UP
                )
                    .multiply(BigDecimal("100"))
            holder.progress.text = "$percent%"
            if (uploadSize >= totalSize) {
                holder.progress.visibility = View.GONE
            } else {
                holder.progress.visibility = View.VISIBLE
            }
        } else {
            holder.progress.visibility = View.GONE
        }

        if(list[pos].isError) {
            holder.mError.visibility = View.VISIBLE
        } else {
            holder.mError.visibility = View.GONE
        }
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iv: ImageView = itemView.findViewById(R.id.mIv)

        val progress = itemView.findViewById<TextView>(R.id.mProgress)

        val mError = itemView.findViewById<TextView>(R.id.mError)
    }

    interface OnItemClick {

        fun add()

        fun click(pos: Int)
    }
}
