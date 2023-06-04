package com.dicoding.storyapp.view.account

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.LoginResult
import com.dicoding.storyapp.databinding.ActivityAccountBinding
import com.dicoding.storyapp.utils.AlarmReceiver
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.login.LoginActivity
import com.dicoding.storyapp.view.login.LoginPreferences
import com.dicoding.storyapp.view.setting.AccountViewModel

class AccountActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var accountBinding: ActivityAccountBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var loginPreference: LoginPreferences
    private lateinit var loginResult: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountBinding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(accountBinding.root)

        accountViewModel = obtainViewModel(this as AppCompatActivity)
        loginPreference = LoginPreferences(this)
        loginResult = loginPreference.getUser()
        accountBinding.name.text = loginResult.name
        showLoading(false)
        darkMode()
        setupView()
        setupAction()
        accountBinding.btnSetRepeatingAlarm.setOnClickListener(this)
        accountBinding.btnCancelRepeatingAlarm.setOnClickListener(this)
        accountBinding.btnSetLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        alarmReceiver = AlarmReceiver()
    }

    private fun setupAction() {
        accountBinding.btnLogout.setOnClickListener {
            showLoading(true)
            loginPreference.removeUser()
            val intent = Intent(this@AccountActivity, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun setupView() {
        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.account_page)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        val repeatMessage = getString(R.string.alarm_message)
        when (v.id) {
            R.id.btn_set_repeating_alarm -> alarmReceiver.setRepeatingAlarm(this, repeatMessage)
            R.id.btn_cancel_repeating_alarm -> alarmReceiver.cancelAlarm(this)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AccountViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[AccountViewModel::class.java]
    }

    private fun darkMode() {
        accountBinding.switchTheme.apply {
            accountViewModel.getThemeSettings().observe(this@AccountActivity) { isDark ->
                val mode =
                    if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(mode)
                isChecked = isDark
            }
            setOnCheckedChangeListener { _, isChecked ->
                accountViewModel.saveThemeSettings(isChecked)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        accountBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}