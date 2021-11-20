package tekin.lutfi.pixabay.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import tekin.lutfi.pixabay.adapter.PixabayImageAdapter
import tekin.lutfi.pixabay.api.datasource.PixabayApi
import tekin.lutfi.pixabay.databinding.ImageListFragmentBinding
import tekin.lutfi.pixabay.utils.debounce

class ImageListFragment : Fragment() {


    private val viewModel: ImageListViewModel by activityViewModels()

    private var binding: ImageListFragmentBinding? = null

    private val pixabayImageAdapter by lazy { PixabayImageAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImageListFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        loadImages()
    }

    private fun initUI() {
        binding?.imageListRV?.adapter = pixabayImageAdapter
        binding?.inputQuery?.doAfterTextChanged {
            viewModel.source.postValue(PixabayApi().apply {
                query = it.toString()
            })
        }
    }

    private fun loadImages() {
        viewModel.source.debounce(1000,lifecycleScope)
            .observe(viewLifecycleOwner, Observer {
                imageLoadJob?.cancel()
                imageLoadJob = lifecycleScope.launchWhenCreated {
                    viewModel.imageFlow.collectLatest {
                        pixabayImageAdapter.submitData(it)
                    }
                }
        })

    }

    private var imageLoadJob: Job? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}