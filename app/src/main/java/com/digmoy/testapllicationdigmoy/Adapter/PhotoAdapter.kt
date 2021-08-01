package com.digmoy.testapllicationdigmoy.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.digmoy.testapllicationdigmoy.ApiModel.Photos
import com.digmoy.testapllicationdigmoy.databinding.HomeLayoutBinding
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper

class PhotoAdapter(private val context: Context, private val modelList: List<Photos>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HomeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToViews(position: Int) {

            Log.e("Image",""+modelList[position].url+".jpg")

            UrlImageViewHelper.setUrlDrawable(binding.image,modelList[position].url+".jpg")
            binding.name.text = modelList[position].title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.ViewHolder {
        return ViewHolder(HomeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PhotoAdapter.ViewHolder, position: Int) {
        holder.setDataToViews(position)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}