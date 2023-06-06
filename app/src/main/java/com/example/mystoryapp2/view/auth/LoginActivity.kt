package com.example.mystoryapp2.view.auth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.example.mystoryapp2.data.viewmodel.AuthViewModel
import com.example.mystoryapp2.databinding.ActivityLoginBinding
import com.example.mystoryapp2.view.story.Home.HomeActivity
import com.example.mystoryapp2.view.utils.Constant
import com.example.mystoryapp2.view.utils.SessionPref


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionPref: SessionPref
    private lateinit var loginViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionPref = SessionPref(this)
        loginViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        disableBtnLogin()

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            newLogin(email, password)

        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun newLogin(email: String, password: String) {

        loginViewModel.login(email, password)
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.loginResult.observe(this) { loginResponse ->
            sessionPref.apply {
                sessionPref.setStringPreference(
                    Constant.PREF_USER_ID,
                    loginResponse.loginResult?.userId ?: ""
                )
                sessionPref.setStringPreference(
                    Constant.PREF_USERNAME,
                    loginResponse.loginResult?.name ?: ""
                )
                sessionPref.setStringPreference(
                    Constant.PREF_TOKEN,
                    loginResponse.loginResult?.token ?: ""
                )
                sessionPref.setBooleanPreference(Constant.PREF_IS_LOGIN, true)
            }
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

        loginViewModel.error.observe(this) { error ->
            // Show error message
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableBtnLogin() {
        binding.btnLogin.isEnabled = binding.edLoginPassword.text.toString().length >= 8

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                binding.btnLogin.isEnabled = password.length >= 8
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}