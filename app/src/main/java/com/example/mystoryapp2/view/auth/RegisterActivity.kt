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
import com.example.mystoryapp2.databinding.ActivityRegisterBinding
import com.example.mystoryapp2.view.utils.SessionPref

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionPref: SessionPref
    private lateinit var registerViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionPref = SessionPref(this)
        registerViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        disableBtnRegister()
        binding.buttonRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            register(name, email, password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        registerViewModel.register(name, email, password)
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.error.observe(this) { error ->
            // Show error message
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
        registerViewModel.registerSuccess.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun disableBtnRegister() {
        binding.buttonRegister.isEnabled = binding.edRegisterPassword.text.toString().length >= 8

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                binding.buttonRegister.isEnabled = password.length >= 8
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}