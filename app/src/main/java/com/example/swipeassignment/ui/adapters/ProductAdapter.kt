package com.example.swipeassignment.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.FragmentProductItemBinding
import com.example.swipeassignment.model.ProductDataResponse

class ProductAdapter(private val context: Context):ListAdapter<ProductDataResponse,ProductAdapter.MainViewHolder>(ComparatorDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.MainViewHolder {
        val binding = FragmentProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MainViewHolder, position: Int) {
        val products = getItem(position)
        products?.let {
            holder.bindItems(it)
        }
    }
    inner class MainViewHolder(private val binding: FragmentProductItemBinding):RecyclerView.ViewHolder(binding.root){
            fun bindItems(products:ProductDataResponse){
                binding.apply {
                    productTitleTextView.text=products.product_name
                    productCategoryTextView.text = products.product_type
                    productPriceTextView.text= products.price.toString()
//                    Loading Image
                    productImageViewLoadingProgressBar.isVisible = true
                    Glide.with(context)
                        .load(products.image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_not_found_2)
                        .listener(object :RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                productImageViewLoadingProgressBar.isVisible= false
                                return false
                            }
                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                productImageViewLoadingProgressBar.isVisible=false
                                return false
                            }
                        })
                        .into(productImageView)
                }

            }

    }

    class ComparatorDiffUtil():DiffUtil.ItemCallback<ProductDataResponse>(){
        override fun areItemsTheSame(
            oldItem: ProductDataResponse,
            newItem: ProductDataResponse
        ): Boolean {
         return oldItem.product_name == newItem.product_name
        }

        override fun areContentsTheSame(
            oldItem: ProductDataResponse,
            newItem: ProductDataResponse
        ): Boolean {
         return oldItem == newItem
        }
    }
}