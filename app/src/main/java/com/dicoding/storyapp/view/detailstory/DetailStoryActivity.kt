package com.dicoding.storyapp.view.detailstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.utils.DateFormatter
import com.dicoding.storyapp.view.detailmap.DetailMapActivity
import java.util.TimeZone

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailStoryBinding
    private var username: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        setupView()
        val desc = intent.getStringExtra(DESCRIPTION)
        val photo = intent.getStringExtra(PHOTO)
        date = intent.getStringExtra(DATE)
        val formattedDate = DateFormatter.formatDate(date!!, TimeZone.getDefault().id)
        username = intent.getStringExtra(USERNAME)
        latitude = intent.getStringExtra(LAT)
        longitude = intent.getStringExtra(LON)
        detailBinding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(photo)
                .into(storyImages)
            storyName.text = username
            storyDesc.text = desc
            storyDate.text = formattedDate
        }

        detailBinding.fbShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_subject))
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Dipost oleh $username pada $formattedDate\n\nDengan deskripsi :\n$desc"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setupView() {
        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.detail_page)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (latitude?.toDouble() == 0.0 && longitude?.toDouble() == 0.0) {
            return false
        } else {
            val inflater = menuInflater
            inflater.inflate(R.menu.detail_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                val moveToMap =
                    Intent(this@DetailStoryActivity, DetailMapActivity::class.java).apply {
                        putExtra(DetailMapActivity.USERNAME, username)
                        putExtra(DetailMapActivity.DATE, date)
                        putExtra(DetailMapActivity.LAT, latitude)
                        putExtra(DetailMapActivity.LON, longitude)
                    }
                startActivity(moveToMap)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val USERNAME = "name"
        const val DESCRIPTION = "desc"
        const val PHOTO = "photo"
        const val DATE = "date"
        const val LAT = "lat"
        const val LON = "lon"
    }
}