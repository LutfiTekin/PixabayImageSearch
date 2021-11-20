package tekin.lutfi.pixabay.utils

import androidx.recyclerview.widget.DiffUtil
import tekin.lutfi.pixabay.data.PixabayImage

object PixabayImageComparator : DiffUtil.ItemCallback<PixabayImage>() {
    override fun areItemsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
        return try {
            oldItem == newItem
        }catch (e: Exception){
            false
        }
    }
}