package com.dicoding.storyapp.view.insertstory

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.ActivityInsertStoryBinding
import com.dicoding.storyapp.utils.createCustomTempFile
import com.dicoding.storyapp.utils.reduceFileImage
import com.dicoding.storyapp.utils.rotateFile
import com.dicoding.storyapp.utils.uriToFile
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.liststory.ListStoryActivity
import com.dicoding.storyapp.view.login.LoginPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class InsertStoryActivity : AppCompatActivity() {
    private lateinit var insertBinding: ActivityInsertStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var insertStoryViewModel: InsertStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCB: CheckBox
    private var getFile: File? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        insertBinding = ActivityInsertStoryBinding.inflate(layoutInflater)
        setContentView(insertBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        insertStoryViewModel = obtainViewModel(this as AppCompatActivity)
        locationCB = insertBinding.checkBoxLocation
        showLoading(false)
        myLocation()
        setupView()
        setupAction()
        insertBinding.cameraButton.setOnClickListener { startTakePhoto() }
        insertBinding.galleryButton.setOnClickListener { startGallery() }
    }

    private fun obtainViewModel(activity: AppCompatActivity): InsertStoryViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[InsertStoryViewModel::class.java]
    }

    private fun setupView() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = getString(R.string.add_page)
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    myLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    myLocation()
                }

                permissions[Manifest.permission.CAMERA] ?: false -> {}
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false -> {}
                else -> {
                    Toast.makeText(
                        this@InsertStoryActivity,
                        getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun myLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkPermission(Manifest.permission.CAMERA) &&
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                } else {
                    Toast.makeText(
                        this@InsertStoryActivity,
                        getString(R.string.location_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                rotateFile(myFile, true)
                getFile = myFile
                insertBinding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@InsertStoryActivity,
                "com.dicoding.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@InsertStoryActivity)
            getFile = myFile
            insertBinding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun setupAction() {
        insertBinding.uploadButton.setOnClickListener {
            if (locationCB.isChecked) {
                if (insertBinding.edAddDescription.text.isNullOrEmpty()) {
                    showToast(getString(R.string.desc_require))
                } else {
                    val description = insertBinding.edAddDescription.text.toString()
                    if (!TextUtils.isEmpty(description) && getFile != null && latitude != null && longitude != null) {
                        postStory(description)
                    } else {
                        showAlert(
                            getString(R.string.post_fail_1),
                            getString(R.string.post_fail)
                        )
                        { }
                    }
                }
            } else {
                latitude = 0.0
                longitude = 0.0
                if (insertBinding.edAddDescription.text.isNullOrEmpty()) {
                    showToast(getString(R.string.desc_require))
                } else {
                    val description = insertBinding.edAddDescription.text.toString()
                    if (!TextUtils.isEmpty(description) && getFile != null && latitude != null && longitude != null) {
                        postStory(description)
                    } else {
                        showAlert(
                            getString(R.string.post_fail_1),
                            getString(R.string.post_fail)
                        )
                        { }
                    }
                }
            }
        }
    }

    private fun convertImage(): MultipartBody.Part {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
    }

    private fun convertDescription(description: String): RequestBody {
        return description.toRequestBody("text/plain".toMediaType())
    }

    private fun postStory(description: String) {
        val image = convertImage()
        val desc = convertDescription(description)
        insertStoryViewModel.insertStory(
            image,
            desc,
            latitude!!,
            longitude!!
        ).observe(this@InsertStoryActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showAlert(
                            getString(R.string.post_fail_1),
                            getString(R.string.post_fail_2)
                        )
                        { }
                    }

                    is Result.Success -> {
                        showLoading(false)
                        postSuccess()
                    }
                }
            }
        }
    }

    private fun postSuccess() {
        showAlert(getString(R.string.post_success), getString(R.string.post_success_1))
        { navigateToList() }
        insertBinding.previewImageView.setImageResource(R.drawable.ic_place_holder)
        insertBinding.edAddDescription.text?.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        insertBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun showAlert(
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

    private fun navigateToList() {
        val intent = Intent(this@InsertStoryActivity, ListStoryActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun showToast(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}