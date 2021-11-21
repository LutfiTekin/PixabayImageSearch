package tekin.lutfi.pixabay.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import tekin.lutfi.pixabay.R
import tekin.lutfi.pixabay.databinding.FragmentImageDetailBinding


class ImageDetailFragment : Fragment() {

    private var binding: FragmentImageDetailBinding? = null

    private val viewModel: ImageListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        return binding?.root
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


}