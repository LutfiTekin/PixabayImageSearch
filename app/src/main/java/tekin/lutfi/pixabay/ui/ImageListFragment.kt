package tekin.lutfi.pixabay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import tekin.lutfi.pixabay.adapter.PixabayImageAdapter
import tekin.lutfi.pixabay.databinding.ImageListFragmentBinding

class ImageListFragment : Fragment() {


    private val viewModel: ImageListViewModel by activityViewModels()

    private var binding: ImageListFragmentBinding? = null

    private val pixabayImageAdapter by lazy { PixabayImageAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImageListFragmentBinding.inflate(inflater, container, false)
        binding?.imageListRV?.adapter = pixabayImageAdapter
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImages()
    }

    private fun loadImages() {
        lifecycleScope.launchWhenCreated {
            viewModel.imageFlow.collectLatest {
                pixabayImageAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}