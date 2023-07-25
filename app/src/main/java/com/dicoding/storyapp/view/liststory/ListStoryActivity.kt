package com.dicoding.storyapp.view.liststory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityListStoryBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.account.AccountActivity
import com.dicoding.storyapp.view.adapter.ListStoryAdapter
import com.dicoding.storyapp.view.adapter.LoadingStateAdapter
import com.dicoding.storyapp.view.insertstory.InsertStoryActivity
import com.dicoding.storyapp.view.login.LoginPreferences
import com.dicoding.storyapp.view.map.MapActivity

class ListStoryActivity : AppCompatActivity() {
    private lateinit var listBinding: ActivityListStoryBinding
    private lateinit var listStoryViewModel: ListStoryViewModel
    private lateinit var preference: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(listBinding.root)

        listStoryViewModel = obtainViewModel(this as AppCompatActivity)
        setupView()
        showLoading(true)
        preference = LoginPreferences(this)
        listBinding.rvListstory.layoutManager = LinearLayoutManager(this)
        getData()
        listBinding.fbAdd.setOnClickListener {
            val moveToInsert = Intent(this, InsertStoryActivity::class.java)
            startActivity(moveToInsert)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ListStoryViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[ListStoryViewModel::class.java]
    }

    private fun setupView() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = getString(R.string.list_page)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_account -> {
                val moveToAccount = Intent(this@ListStoryActivity, AccountActivity::class.java)
                startActivity(moveToAccount)
            }

            R.id.menu_maps -> {
                val moveToMap = Intent(this@ListStoryActivity, MapActivity::class.java)
                startActivity(moveToMap)
            }

            R.id.menu_refresh -> {
                showLoading(true)
                getData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        listBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getData() {
        showLoading(false)
        val adapter = ListStoryAdapter()
        listBinding.rvListstory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        listStoryViewModel.getListStory.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }
}