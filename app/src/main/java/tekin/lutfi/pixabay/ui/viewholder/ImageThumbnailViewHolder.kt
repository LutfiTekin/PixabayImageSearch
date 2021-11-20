package tekin.lutfi.pixabay.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.databinding.ItemThumbnailBinding

class ImageThumbnailViewHolder internal constructor(
    private val binding: ItemThumbnailBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(image: PixabayImage?){
            binding.pixabayImage = image
        }

}