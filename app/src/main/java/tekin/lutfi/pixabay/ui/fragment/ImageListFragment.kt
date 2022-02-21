package tekin.lutfi.pixabay.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import petrov.kristiyan.colorpicker.ColorPicker
import tekin.lutfi.pixabay.R
import tekin.lutfi.pixabay.adapter.PixabayImageAdapter
import tekin.lutfi.pixabay.data.ImageSelectionListener
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.databinding.ImageListFragmentBinding
import tekin.lutfi.pixabay.utils.acceptedColors
import tekin.lutfi.pixabay.utils.confirm
import tekin.lutfi.pixabay.utils.debounce

const val LAST_SELECTED_IMAGE = "lsi"

@AndroidEntryPoint
class ImageListFragment : Fragment(), ImageSelectionListener {


    //region Variables
    private val viewModel: ImageListViewModel by activityViewModels()

    private var binding: ImageListFragmentBinding? = null

    private val pixabayImageAdapter by lazy { PixabayImageAdapter(this) }

    private var imageLoadJob: Job? = null

    private var lastSelectedImage: PixabayImage? = null

    //https://stackoverflow.com/a/55039009/3742074
    private var storedView: View? = null
    //endregion

    //region LifeCycle methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (storedView != null) return storedView
        binding = ImageListFragmentBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenStarted {
            loadImages()
        }
        storedView = binding?.root
        return storedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(LAST_SELECTED_IMAGE,lastSelectedImage)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        lastSelectedImage = savedInstanceState?.getSerializable(LAST_SELECTED_IMAGE) as PixabayImage?
        lastSelectedImage?.let {
            onImageSelected(it)
        }
    }

    //endregion

    private fun initUI() {
        val colorSelectionListener = object : ColorPicker.OnChooseColorListener {
            override fun onChooseColor(position: Int, color: Int) {
                val selectedColor = acceptedColors.keys.toList().getOrNull(position).orEmpty()
                viewModel.updateColor(selectedColor)
            }

            override fun onCancel() {

            }
        }
        binding?.apply {
            imageListRV.adapter = pixabayImageAdapter
            inputQuery.doAfterTextChanged {
                viewModel.updateQuery(it.toString())
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

    override fun onImageSelected(image: PixabayImage) {
        lifecycleScope.launchWhenResumed {
            lastSelectedImage = image
            val userAccepted = confirm(requireContext(),R.string.dialog_title_detail,R.string.dialog_message_detail)
            if (userAccepted){
                viewModel.selectedImage.value = image
                findNavController().navigate(R.id.nav_image_detail)
            }
            lastSelectedImage = null //Dialog consumed
        }
    }



}