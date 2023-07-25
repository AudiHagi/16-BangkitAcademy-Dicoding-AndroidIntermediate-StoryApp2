package com.dicoding.storyapp.view.register

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
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.utils.emailValidation
import com.dicoding.storyapp.utils.passwordValidation
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.login.LoginActivity
import com.dicoding.storyapp.view.login.LoginPreferences

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerViewModel = obtainViewModel(this as AppCompatActivity)
        showLoading(false)
        setupView()
        setupAnimation()
        nameET()
        emailET()
        passwordET()
        setupAction()
    }

    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }

    private fun nameET() {
        val myRegisterNameET = registerBinding.edRegisterName
        myRegisterNameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val name = registerBinding.edRegisterName.text.toString()
                if (name.isNotEmpty()) {
                    registerBinding.errorName.visibility = View.GONE
                } else {
                    registerBinding.errorName.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun emailET() {
        val myRegisterEmailET = registerBinding.edRegisterEmail
        myRegisterEmailET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setupAction()
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun passwordET() {
        val myRegisterPasswordET = registerBinding.edRegisterPassword
        myRegisterPasswordET.addTextChangedListener(object : TextWatcher {
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
        registerBinding.errorName.visibility = View.GONE
        registerBinding.errorEmail.visibility = View.GONE
        registerBinding.errorPass.visibility = View.GONE
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(registerBinding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title =
            ObjectAnimator.ofFloat(registerBinding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val name =
            ObjectAnimator.ofFloat(registerBinding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameInput =
            ObjectAnimator.ofFloat(registerBinding.nameEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val email =
            ObjectAnimator.ofFloat(registerBinding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailInput = ObjectAnimator.ofFloat(registerBinding.emailEditTextLayout, View.ALPHA, 1f)
            .setDuration(500)
        val password =
            ObjectAnimator.ofFloat(registerBinding.passwordTextView, View.ALPHA, 1f)
                .setDuration(500)
        val passwordInput =
            ObjectAnimator.ofFloat(registerBinding.passwordEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val register =
            ObjectAnimator.ofFloat(registerBinding.registerButton, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameInput,
                email,
                emailInput,
                password,
                passwordInput,
                register
            )
            start()
        }
    }

    private fun setupAction() {
        registerBinding.registerButton.setOnClickListener {
            val name = registerBinding.edRegisterName.text.toString()
            val email = registerBinding.edRegisterEmail.text.toString()
            val password = registerBinding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    registerBinding.errorName.text = getString(R.string.insert_name)
                }

                email.isEmpty() -> {
                    registerBinding.errorEmail.text = getString(R.string.insert_email)
                }

                password.isEmpty() -> {
                    registerBinding.errorPass.text = getString(R.string.insert_pass)
                }

                else -> {
                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
                            name
                        )
                    ) {
                        if (passwordValidation(password) && emailValidation(email)) {
                            register(name, email, password)
                        } else {
                            showAlert(
                                getString(R.string.regis_gagal),
                                "Invalid Email/Password"
                            )
                            { }
                        }
                    } else {
                        showAlert(
                            getString(R.string.regis_gagal),
                            getString(R.string.regis_gagal_1)
                        )
                        { finish() }
                    }
                }
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        registerViewModel.userRegister(name, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showAlert(
                                getString(R.string.regis_gagal),
                                getString(R.string.regis_gagal_2)
                            )
                            { }
                        }

                        is Result.Success -> {
                            showLoading(false)
                            registerSuccess()
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

    private fun registerSuccess() {
        showAlert(
            getString(R.string.regis_berhasil),
            getString(R.string.regis_berhasil_1)
        )
        { navigateToLogin() }
        registerBinding.edRegisterName.text?.clear()
        registerBinding.edRegisterEmail.text?.clear()
        registerBinding.edRegisterPassword.text?.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        registerBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}