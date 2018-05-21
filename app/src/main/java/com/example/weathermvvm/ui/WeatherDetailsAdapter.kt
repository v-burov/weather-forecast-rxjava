package com.example.weathermvvm.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.weathermvvm.R
import kotlinx.android.synthetic.main.image_list_item.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.ArrayList

/**
 *  Adapter to fill list with weather details
 */

class WeatherDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<ListItem>()

    fun setData(list: List<ListItem>) = items.apply {
        clear()
        addAll(list)
    }.let {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                ListItemType.IMAGE_ITEM -> VHImageItem(inflateView(R.layout.image_list_item, parent))
                else -> VHItem(inflateView(R.layout.list_item, parent))
            }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.type) {
            ListItemType.IMAGE_ITEM -> setImageItem(item as ImageListItem, holder.itemView)
            else -> setCommonItem(item, holder.itemView)
        }
    }

    override fun getItemViewType(position: Int): Int = items[position].type

    private fun inflateView(layoutId: Int, parent: ViewGroup): View = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

    private fun setImageItem(item: ImageListItem, itemView: View) = with(itemView) {
        Glide.with(context)
                .load(item.imagePath)
                .asBitmap()
                .fitCenter()
                .into(image)

        textImageTitle.text = item.title
    }

    private fun setCommonItem(item: ListItem, itemView: View) {
        itemView.apply {
            textTitle.text = item.title
            textValue.text = item.value
        }
    }

    class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView)
    class VHImageItem(itemView: View) : RecyclerView.ViewHolder(itemView)
}

open class ListItem (val type: Int, val title: String, val value: String)
class ImageListItem(type: Int, title: String, val imagePath: String) : ListItem(type, title, "")
class ListItemType {
    companion object {
        const val ITEM = 0
        const val IMAGE_ITEM = 1
    }
}