package com.example.mystoryapp2.view.story.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp2.MainActivity
import com.example.mystoryapp2.R
import com.example.mystoryapp2.data.viewmodel.ListStoryViewModel
import com.example.mystoryapp2.data.viewmodel.ViewModelFactory
import com.example.mystoryapp2.databinding.ActivityHomeBinding
import com.example.mystoryapp2.view.auth.SettingsActivity
import com.example.mystoryapp2.view.maps.MapsActivity
import com.example.mystoryapp2.view.story.addstory.AddStoryActivity
import com.example.mystoryapp2.view.utils.SessionPref

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var pref: SessionPref
    private val rvAdapter = HomeAdapter()
    private val listStoryViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryViewModel.stories.observe(this){data ->
            if (data != null) {
                rvAdapter.submitData(lifecycle , data)
            }
        }

        rvAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvStory.isVisible = false
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.isVisible = false
                    binding.rvStory.isVisible = true
                }
                is LoadState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.rvStory.isVisible = false
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.btnMaps.setOnClickListener {startActivity(Intent(this, MapsActivity::class.java))}

        binding.btnSetting.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }

        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter =
                rvAdapter.withLoadStateFooter(footer = LoadingStateAdapter { rvAdapter.retry() })
            isNestedScrollingEnabled = false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}