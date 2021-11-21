package tekin.lutfi.pixabay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import tekin.lutfi.pixabay.data.ImageSelectionListener
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.databinding.ItemThumbnailBinding
import tekin.lutfi.pixabay.ui.viewholder.ImageThumbnailViewHolder
import tekin.lutfi.pixabay.utils.PixabayImageComparator


class PixabayImageAdapter(private val imageSelectionListener: ImageSelectionListener): PagingDataAdapter<PixabayImage, ImageThumbnailViewHolder>(PixabayImageComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageThumbnailViewHolder {
        val binding = ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageThumbnailViewHolder(binding, imageSelectionListener)
    }

    override fun onBindViewHolder(holder: ImageThumbnailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


