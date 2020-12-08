package a.alt.z.imagepicker.ui.imagepicker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import a.alt.z.imagepicker.R
import a.alt.z.imagepicker.databinding.FragmentLightImagePickerBinding
import a.alt.z.imagepicker.util.viewBinding

class LightImagePickerFragment: Fragment(R.layout.fragment_light_image_picker) {

    private val binding by viewBinding(FragmentLightImagePickerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}