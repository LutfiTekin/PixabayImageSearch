package tekin.lutfi.pixabay.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import tekin.lutfi.pixabay.api.datasource.PixabayApi
import tekin.lutfi.pixabay.api.datasource.PixabayImagePagingSource
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.utils.DEFAULT_PAGE_SIZE

class ImageListViewModel : ViewModel() {


    private val source = PixabayApi()

    val imageFlow: Flow<PagingData<PixabayImage>>
        get() {
            val config = PagingConfig(DEFAULT_PAGE_SIZE)
            return Pager(config) {
                PixabayImagePagingSource(source)
            }.flow.cachedIn(viewModelScope)
        }
}