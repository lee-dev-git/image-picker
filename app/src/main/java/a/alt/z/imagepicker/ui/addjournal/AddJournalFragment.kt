package a.alt.z.imagepicker.ui.addjournal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import a.alt.z.imagepicker.R
import a.alt.z.imagepicker.databinding.FragmentAddJournalBinding
import a.alt.z.imagepicker.util.viewBinding

class AddJournalFragment : Fragment(R.layout.fragment_add_journal) {

    private val binding by viewBinding(FragmentAddJournalBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setupObserver()
    }

    private fun init() {

    }

    private fun setupObserver() {

    }
}