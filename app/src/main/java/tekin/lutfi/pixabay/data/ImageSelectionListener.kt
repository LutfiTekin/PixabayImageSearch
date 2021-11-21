package tekin.lutfi.pixabay.data

import tekin.lutfi.pixabay.databinding.ItemThumbnailBinding

interface ImageSelectionListener {

    fun onImageSelected(image: PixabayImage, binding: ItemThumbnailBinding)

}