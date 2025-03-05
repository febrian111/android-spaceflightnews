package test.febri.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import test.febri.data.model.ArticleResponse
import test.febri.data.model.BaseResponse

interface ApiService {
    @GET("/v4/articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("news_site") newsSite: String,
        @Query("ordering") ordering: String
    ): BaseResponse<ArticleResponse>
}
