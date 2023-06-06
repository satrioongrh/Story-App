package com.example.mystoryapp2.view.story.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.databinding.ActivityDetailBinding
import com.example.mystoryapp2.view.utils.Constant

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var story: ListStoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDetailName.text = intent.getData(Constant.name, "Name")
        Glide.with(binding.root)
            .load(intent.getData(Constant.photo_url, ""))
            .into(binding.ivDetailPhoto)
        binding.tvDetailDescription.text =
            intent.getData(Constant.desc, "Caption")
    }


    private fun Intent.getData(key: String, defaultValue: String = "None"): String {
        return getStringExtra(key) ?: defaultValue
    }
}