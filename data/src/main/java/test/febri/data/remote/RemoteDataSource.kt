package test.febri.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import test.febri.data.model.ArticleResponse
import test.febri.data.model.BaseResponse
import test.febri.data.util.mapApiException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getArticles(
        limit: Int,
        offset: Int,
        newsSite: String,
        ordering: String
    ): BaseResponse<ArticleResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getArticles(
                    limit,
                    offset,
                    newsSite,
                    ordering
                )
            } catch (e: Exception) {
                throw e.mapApiException()
            }
        }

}