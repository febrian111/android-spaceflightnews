package test.febri.spaceflightnews.searchnews.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BlogModel
import test.febri.domain.usecase.GetArticlesUseCase
import test.febri.domain.usecase.GetBlogsUseCase
import timber.log.Timber

class SearchBlogPagingSource (
    private val getBlogsUseCase: GetBlogsUseCase,
    private val query: String
) : PagingSource<Int, BlogModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BlogModel> {
        val offset = params.key ?: START_PAGE_INDEX
        Timber.e("page: $offset and paramKey: ${params.key}")

        return try {
            val it = getBlogsUseCase(
                titleQuery = query,
                offset = offset).first()

            val nextKey = if (it.results.size < PAGE_SIZE || it.next.isNullOrEmpty())
                null
            else
                offset + PAGE_SIZE

            LoadResult.Page(
                data = it.results,
                prevKey = null,
                nextKey = nextKey
            )
        }catch (t: Throwable){
            LoadResult.Error<Int, BlogModel>(t)
        }
    }


    /** The refresh key is used for the initial load of the next PagingSource, after invalidation
     * We need to get the previous key (or next key if previous is null) of the page
     * that was closest to the most recently accessed index.
     * Anchor position is the most recently accessed index
     */
    override fun getRefreshKey(state: PagingState<Int, BlogModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object{
        private const val START_PAGE_INDEX = 1
        const val PAGE_SIZE = 30
    }
}