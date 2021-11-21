package tekin.lutfi.pixabay.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load


@BindingAdapter("load_image")
fun ImageView.loadImage(image: String?){
    this.load(image)
}
