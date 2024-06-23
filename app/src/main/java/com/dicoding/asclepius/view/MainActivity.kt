package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(uri : Uri) {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        binding.progressIndicator.visibility = View.VISIBLE
        currentImageUri?.let {
            binding.progressIndicator.visibility = View.GONE
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        val uri  = currentImageUri
        if (uri == null) {
            showToast("Foto Jangan Kosong")
            return
        }

        binding.progressIndicator.visibility = View.GONE
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                    binding.progressIndicator.visibility = View.GONE
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    if (results.isNullOrEmpty() || results[0].categories.isEmpty()) {
                        showToast("Tidak ada hasil dari Klasifikasi")
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.progressIndicator.visibility = View.GONE
                        }, 5000)
                        return
                    }

                    val displayResult = results[0].categories
                        .sortedByDescending { it.score }
                        .joinToString("\n") { category ->
                            "${category.label} ${
                                NumberFormat.getPercentInstance().format(category.score).trim()
                            }"
                        }
                    moveToResult(displayResult)
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressIndicator.visibility = View.GONE
                    }, 2000)
                }
            }
        ).apply {
            classifyStaticImage(uri)
        }
    }

    private fun moveToResult(displayResult: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("ImageUri", currentImageUri.toString())
            putExtra("DisplayResult", displayResult)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}