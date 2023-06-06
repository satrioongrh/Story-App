package com.example.mystoryapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mystoryapp2.data.paging.StoryPagingSource
import com.example.mystoryapp2.data.viewmodel.ListStoryViewModel
import com.example.mystoryapp2.data.viewmodel.ViewModelFactory
import com.example.mystoryapp2.view.auth.LoginActivity
import com.example.mystoryapp2.databinding.ActivityMainBinding
import com.example.mystoryapp2.view.story.Home.HomeActivity
import com.example.mystoryapp2.view.utils.SessionPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionPref: SessionPref
    private var token = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionPref = SessionPref(this)
        token = "bearer ${sessionPref.getToken}"
        if (sessionPref.isLogin == false) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    }

//    fun checkLogin() {
//        // Cek apakah sudah login atau belum
//        if (sessionPref.isLogin) {
//            // Jika sudah login, langsung masuk ke halaman utama
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        } else {
//            // Jika belum login, masuk ke halaman login
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
