package tekin.lutfi.pixabay.api.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.utils.DEFAULT_PAGE_SIZE

const val STARTING_PAGE = 1

class PixabayImagePagingSource(private val source: PixabayApi?): PagingSource<Int,PixabayImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayImage> {
        return try {
            val requestedPage = params.key ?: STARTING_PAGE
            val data = source?.getImages(requestedPage) ?: emptyList()
            val prevKey = if (requestedPage == STARTING_PAGE) null else requestedPage - 1
            val nextKey = if (data.size < DEFAULT_PAGE_SIZE) null else (source?.page ?: STARTING_PAGE) + 1
            LoadResult.Page(data,prevKey,nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PixabayImage>): Int? {
        return state.anchorPosition
    }

}