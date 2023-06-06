package com.example.mystoryapp2.view.story.Home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp2.R
import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.view.story.detail.DetailActivity
import com.example.mystoryapp2.view.utils.Constant
import com.example.mystoryapp2.view.utils.setImageUrl

class HomeAdapter : PagingDataAdapter<ListStoryItem, HomeAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImg: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val created: TextView = itemView.findViewById(R.id.tvStoryDate)
        val name: TextView = itemView.findViewById(R.id.tv_item_name)
        val desc: TextView = itemView.findViewById(R.id.tvStoryDesc)
        fun bind(story: ListStoryItem) {
            name.text = story.name
            desc.text = story.description
            created.text = story.createdAt

            Glide.with(photoImg.context)
                .load(story.photoUrl)
                .into(photoImg)
            with(itemView) {
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(Constant.name, story.name)
                    intent.putExtra(Constant.photo_url, story.photoUrl)
                    intent.putExtra(Constant.desc, story.description)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            /* transition between recyclerview & acitvity detail */
                            androidx.core.util.Pair(desc, "description"),
                            androidx.core.util.Pair(name, "name"),
                            androidx.core.util.Pair(photoImg, "profile"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())

                }
            }
        }
    }
}