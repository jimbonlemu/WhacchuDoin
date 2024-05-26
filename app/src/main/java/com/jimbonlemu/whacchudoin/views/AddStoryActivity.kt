package com.jimbonlemu.whacchudoin.views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityAddStoryBinding
import com.jimbonlemu.whacchudoin.utils.GetHelp
import com.jimbonlemu.whacchudoin.view_models.AddStoryViewModel
import com.yalantis.ucrop.UCrop
import org.koin.android.ext.android.inject
import java.io.File
import java.util.Date

class AddStoryActivity : CoreActivity<ActivityAddStoryBinding>() {

    private val storyViewModel: AddStoryViewModel by inject()

    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (!permissionGranted) {
            Toast.makeText(
                this,
                getString(R.string.access_permission),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButtonAction()
        setupPermission()
        setupObserver()

    }
    private fun setupPermission(){
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@AddStoryActivity)
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityAddStoryBinding =
        ActivityAddStoryBinding.inflate(layoutInflater)

    private fun setupButtonAction() {
        binding.apply {
            btnCamera.setOnClickListener {
                currentImageUri = GetHelp.getImageUri(this@AddStoryActivity)
                cameraLauncher.launch(currentImageUri!!)
            }
            btnGallery.setOnClickListener {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            buttonAdd.setOnClickListener {
                val desc = edAddDescription.text.toString()
                if (desc.isNotEmpty() && currentImageUri != null) {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                        AlertDialog.Builder(this@AddStoryActivity).apply {
                            setTitle(getString(R.string.post_story_option))
                            setMessage(getString(R.string.post_with_your_current_location))
                            setPositiveButton(getString(R.string.yes_post_with_current_location)) { _, _ ->
                                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        latitude = location.latitude
                                        longitude = location.longitude

                                    }
                                    storyViewModel.addStory(
                                        currentImageUri!!,
                                        desc,
                                        latitude ?: 0.0,
                                        longitude ?: 0.0,
                                    )
                                }

                            }
                            setNegativeButton(getString(R.string.no_don_t_post_with_location)) { _, _ ->
                                storyViewModel.addStory(
                                    currentImageUri!!,
                                    desc,
                                )
                            }
                            create()
                            show()
                        }
                    }

                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        getString(R.string.don_t_left_the_image_and_description_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupObserver() {
        storyViewModel.addStoryResult.observe(this@AddStoryActivity) { state ->
            binding.apply {
                when (state) {
                    is ResponseState.Loading -> {
                        setEnableComponent(false)
                        buttonAdd.text = getString(R.string.loading_upload_story)
                    }

                    is ResponseState.Success -> {
                        setEnableComponent(true)
                        buttonAdd.text = resources.getString(R.string.btn_add_text)
                        Toast.makeText(this@AddStoryActivity, getString(R.string.success_post_story), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddStoryActivity, HomeActivity::class.java))
                        finish()
                    }

                    is ResponseState.Error -> {
                        setEnableComponent(true)
                        buttonAdd.text = resources.getString(R.string.btn_add_text)
                        Toast.makeText(this@AddStoryActivity, state.errorMessage, Toast.LENGTH_SHORT).show()

                    }

                    else -> buttonAdd.isEnabled = true
                }
            }
        }
    }

    private fun setEnableComponent(isEnabled: Boolean) {
        binding.apply {
            if (isEnabled) {
                edAddDescription.isEnabled = true
                btnGallery.isEnabled = true
                btnCamera.isEnabled = true
                buttonAdd.isEnabled = true
            } else {
                edAddDescription.isEnabled = false
                btnGallery.isEnabled = false
                btnCamera.isEnabled = false
                buttonAdd.isEnabled = false
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            launchUCrop(uri)
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { launchUCrop(it) }
        }
    }

    private val uCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    currentImageUri = resultUri
                    setImagePreview()
                }
            }
        }

    private fun launchUCrop(uri: Uri) {
        val timestamp = Date().time
        val cachedImage = File(this@AddStoryActivity.cacheDir, "cropped_image_${timestamp}.jpg")

        val destinationUri = Uri.fromFile(cachedImage)

        val uCrop = UCrop.of(uri, destinationUri).withAspectRatio(1f, 1f)

        uCrop.getIntent(this@AddStoryActivity).apply {
            uCropLauncher.launch(this)
        }
    }

    private fun setImagePreview() {
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this@AddStoryActivity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}