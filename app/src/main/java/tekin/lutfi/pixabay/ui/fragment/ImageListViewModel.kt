package tekin.lutfi.pixabay.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import tekin.lutfi.pixabay.api.datasource.PixabayRepository
import tekin.lutfi.pixabay.api.datasource.PixabayImagePagingSource
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.di.DEFAULT_PAGE_SIZE
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(private val api: PixabayRepository) : ViewModel() {


    val selectedImage = MutableLiveData<PixabayImage>(null)

    val source = MutableLiveData(api)

    val imageFlow: Flow<PagingData<PixabayImage>>
        get() {
            val config = PagingConfig(DEFAULT_PAGE_SIZE)
            return Pager(config) {
                PixabayImagePagingSource(source.value)
            }.flow.cachedIn(viewModelScope)
        }


    fun updateColor(color: String){
        val currentSource = source.value ?: api
        currentSource.color = color
        source.value = currentSource
    }

    fun updateQuery(query: String){
        val currentSource = source.value ?: api
        currentSource.query = query
        source.value = currentSource
    }
}