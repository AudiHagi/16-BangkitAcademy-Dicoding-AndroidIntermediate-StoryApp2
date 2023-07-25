package com.dicoding.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.LoginResult
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.utils.emailValidation
import com.dicoding.storyapp.utils.passwordValidation
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.liststory.ListStoryActivity
import com.dicoding.storyapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginViewModel = obtainViewModel(this as AppCompatActivity)
        showLoading(false)
        setupView()
        setupAnimation()
        emailET()
        passwordET()
        setupAction()
        loginBinding.createTextView.setOnClickListener {
            val moveToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(moveToRegister)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    private fun emailET() {
        val myLoginEmailET = loginBinding.edLoginEmail
        myLoginEmailET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setupAction()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun passwordET() {
        val myLoginPasswordET = loginBinding.edLoginPassword
        myLoginPasswordET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setupAction()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        loginBinding.errorPass.visibility = View.GONE
        loginBinding.errorEmail.visibility = View.GONE
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(loginBinding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title =
            ObjectAnimator.ofFloat(loginBinding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(loginBinding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailInput = ObjectAnimator.ofFloat(loginBinding.emailEditTextLayout, View.ALPHA, 1f)
            .setDuration(500)
        val password =
            ObjectAnimator.ofFloat(loginBinding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordInput =
            ObjectAnimator.ofFloat(loginBinding.passwordEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val login =
            ObjectAnimator.ofFloat(loginBinding.loginButton, View.ALPHA, 1f).setDuration(500)
        val create =
            ObjectAnimator.ofFloat(loginBinding.createTextView, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                title,
                email,
                emailInput,
                password,
                passwordInput,
                login,
                create
            )
            start()
        }
    }

    private fun setupAction() {
        loginBinding.loginButton.setOnClickListener {
            val email = loginBinding.edLoginEmail.text.toString()
            val password = loginBinding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    loginBinding.errorEmail.text = getString(R.string.insert_email)
                }

                password.isEmpty() -> {
                    loginBinding.errorPass.text = getString(R.string.insert_pass)
                }

                else -> {
                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        if (passwordValidation(password) && emailValidation(email)) {
                            login(email, password)
                        } else {
                            showAlert(
                                getString(R.string.login_gagal),
                                "Invalid Email/Password"
                            )
                            { }
                        }
                    } else {
                        showAlert(
                            getString(R.string.login_gagal),
                            getString(R.string.login_gagal_1)
                        )
                        { finish() }
                    }
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.userLogin(email, password).observe(this@LoginActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showAlert(
                            getString(R.string.login_gagal),
                            getString(R.string.login_gagal_2)
                        )
                        { }
                    }

                    is Result.Success -> {
                        showLoading(false)
                        loginSuccess(result.data)
                    }
                }
            }
        }
    }

    private fun showAlert(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun loginSuccess(loginResponse: LoginResponse) {
        saveLoginData(loginResponse)
        navigateToMain()
    }

    private fun saveLoginData(loginResponse: LoginResponse) {
        val loginPreference = LoginPreferences(this)
        val loginResult = loginResponse.loginResult
        val loginModel = LoginResult(
            name = loginResult?.name, userId = loginResult?.userId, token = loginResult?.token
        )
        loginPreference.setLogin(loginModel)
    }

    private fun navigateToMain() {
        val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        loginBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}