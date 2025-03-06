package test.febri.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import test.febri.data.model.ArticleResponse
import test.febri.data.remote.RemoteDataSource
import test.febri.domain.model.ArticleModel

class ArticlesPagingSource(
//    private val apiService: SpaceflightNewsApiService
    private val titleQuery: String = "",
    private val newsSite: String,
    private val sortOrder: String,
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, ArticleResponse>() {

    private val LIMIT_PAGE = 30

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse> {
        return try {
            val offset = params.key ?: 0
//            val response = apiService.getArticles(page, params.loadSize)
            val response = remoteDataSource.getArticles(
                limit = LIMIT_PAGE,
                offset = offset,
                titleQuery = titleQuery,
                newsSite = newsSite,
                ordering = sortOrder
            )

            val nextKey =
                if (response.results.isEmpty() || response.results.size < LIMIT_PAGE) null else offset + LIMIT_PAGE

            LoadResult.Page(
                data = response.results,
                prevKey = if (offset == 1) null else offset - LIMIT_PAGE,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(LIMIT_PAGE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(LIMIT_PAGE)
        }
    }
}