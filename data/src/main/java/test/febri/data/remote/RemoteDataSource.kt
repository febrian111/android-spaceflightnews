package test.febri.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import test.febri.data.model.ArticleResponse
import test.febri.data.model.BaseResponse
import test.febri.data.model.BlogResponse
import test.febri.data.model.ReportResponse
import test.febri.data.util.mapApiException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getArticles(
        limit: Int,
        offset: Int,
        titleQuery: String,
        newsSite: String,
        ordering: String
    ): BaseResponse<ArticleResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getArticles(
                    limit,
                    offset,
                    titleQuery,
                    newsSite,
                    ordering
                )
            } catch (e: Exception) {
                throw e.mapApiException()
            }
        }

    suspend fun getBlogs(
        limit: Int,
        offset: Int,
        titleQuery: String,
        newsSite: String,
        ordering: String
    ): BaseResponse<BlogResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getBlogs(
                    limit,
                    offset,
                    titleQuery,
                    newsSite,
                    ordering
                )
            } catch (e: Exception) {
                throw e.mapApiException()
            }
        }

    suspend fun getReports(
        limit: Int,
        offset: Int,
        titleQuery: String,
        newsSite: String,
        ordering: String
    ): BaseResponse<ReportResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getReports(
                    limit,
                    offset,
                    titleQuery,
                    newsSite,
                    ordering
                )
            } catch (e: Exception) {
                throw e.mapApiException()
            }
        }

}