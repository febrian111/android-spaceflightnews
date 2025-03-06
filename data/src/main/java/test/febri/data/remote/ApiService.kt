package test.febri.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import test.febri.data.model.ArticleResponse
import test.febri.data.model.BaseResponse
import test.febri.data.model.BlogResponse

interface ApiService {
    @GET("/v4/articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("title_contains") titleQuery: String,
        @Query("news_site") newsSite: String,
        @Query("ordering") ordering: String
    ): BaseResponse<ArticleResponse>

    @GET("/v4/blogs/")
    suspend fun getBlogs(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("news_site") newsSite: String,
        @Query("ordering") ordering: String
    ): BaseResponse<BlogResponse>
}
