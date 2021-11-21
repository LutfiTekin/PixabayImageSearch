package tekin.lutfi.pixabay.ui.fragment

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
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import petrov.kristiyan.colorpicker.ColorPicker
import tekin.lutfi.pixabay.R
import tekin.lutfi.pixabay.adapter.PixabayImageAdapter
import tekin.lutfi.pixabay.api.datasource.PixabayApi
import tekin.lutfi.pixabay.data.ImageSelectionListener
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.databinding.ImageListFragmentBinding
import tekin.lutfi.pixabay.databinding.ItemThumbnailBinding
import tekin.lutfi.pixabay.utils.acceptedColors
import tekin.lutfi.pixabay.utils.ask
import tekin.lutfi.pixabay.utils.debounce
import tekin.lutfi.pixabay.utils.sharedElements

class ImageListFragment : Fragment(), ImageSelectionListener {


    private val viewModel: ImageListViewModel by activityViewModels()

    private var binding: ImageListFragmentBinding? = null

    private val pixabayImageAdapter by lazy { PixabayImageAdapter(this) }

    //region LifeCycle
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
    //endregion

    private fun initUI() {
        val colorSelectionListener = object : ColorPicker.OnChooseColorListener {
            override fun onChooseColor(position: Int, color: Int) {
                val selectedColor = acceptedColors.keys.toList()[position]
                viewModel.updateColor(selectedColor)
            }

            override fun onCancel() {

            }
        }
        binding?.apply {
            imageListRV.apply {
                adapter = pixabayImageAdapter
                postponeEnterTransition()
                viewTreeObserver
                    .addOnPreDrawListener {
                        startPostponedEnterTransition()
                        true
                    }
            }
            inputQuery.doAfterTextChanged {
                viewModel.source.postValue(PixabayApi().apply {
                    query = it.toString()
                })
            }
            colorPicker.setOnClickListener {
                activity ?: return@setOnClickListener
                ColorPicker(activity).apply {
                    setColors(ArrayList(acceptedColors.values.toList()))
                    setColumns(4)
                    setTitle(getString(R.string.dialog_title_filter_images_by_color))
                    setOnChooseColorListener(colorSelectionListener)
                    show()
                }

            }
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

    override fun onImageSelected(image: PixabayImage, binding: ItemThumbnailBinding) {
        Log.d("ImageSelected","$image")
        lifecycleScope.launchWhenResumed {
            val userAccepted = ask(requireContext(),R.string.dialog_title_detail,R.string.dialog_message_detail)
            if (userAccepted){
                viewModel.selectedImage.value = image
                findNavController().navigate(R.id.nav_image_detail, null, null, binding.sharedElements)
            }
        }
    }



}