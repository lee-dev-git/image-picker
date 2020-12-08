package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.R
import a.alt.z.imagepicker.databinding.ActivityMainBinding
import a.alt.z.imagepicker.model.Bucket
import a.alt.z.imagepicker.ui.imagepicker.ImagePickerFragment
import a.alt.z.imagepicker.ui.imagepicker.adapter.BucketAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.ImageAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.SelectedImageAdapter
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val requestPermission = registerForActivityResult(RequestPermission()) { granted ->
        if(granted)
            showImagePickerFragment()
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        val permissionGranted
                = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if(!permissionGranted) requestPermission.launch(READ_EXTERNAL_STORAGE)
        else showImagePickerFragment()
    }

    private fun showImagePickerFragment() = supportFragmentManager.commit { replace(R.id.mainRootLayout, ImagePickerFragment()) }
}