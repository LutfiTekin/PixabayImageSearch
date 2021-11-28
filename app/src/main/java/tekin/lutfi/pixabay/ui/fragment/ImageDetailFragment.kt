package tekin.lutfi.pixabay.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import tekin.lutfi.pixabay.R
import tekin.lutfi.pixabay.databinding.FragmentImageDetailBinding

@AndroidEntryPoint
class ImageDetailFragment : Fragment() {

    //region Variables
    private var binding: FragmentImageDetailBinding? = null

    private val viewModel: ImageListViewModel by activityViewModels()
    //endregion


    //region LifeCycle methods
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
    //endregion


}