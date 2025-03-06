package test.febri.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import test.febri.data.model.ArticleResponse
import test.febri.data.model.AuthorResponse
import test.febri.data.model.BaseResponse
import test.febri.data.model.SocialsResponse
import test.febri.data.remote.ApiService
import test.febri.data.remote.RemoteDataSource

class RemoteDataSourceTest {
    @Mock
    private lateinit var apiService: ApiService

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = RemoteDataSource(apiService)
    }

    @Test
    fun `getArticles maps API response correctly`() = runTest {
        val articles = listOf( ArticleResponse(
            id = 1,
            title = "Test Article",
            authors = listOf( AuthorResponse("John Doe", SocialsResponse(
                x = "https://example.com/x",
                youtube = "https://example.com/youtube",
                instagram = "https://example.com/instagram",
                linkedin = "https://example.com/linkedin",
                mastodon = "https://example.com/mastodon",
                bluesky = "https://example.com/bluesky"
            ))),
            url = "https://example.com",
            imageUrl = "https://example.com/image.jpg",
            newsSite = "Example News Site",
            summary = "This is a test article",
            publishedAt = "2023-08-01T12:00:00Z",
            updatedAt = "2023-08-01T12:00:00Z",
            featured = false,
            launches = emptyList(),
            events = emptyList()
        ))

        val baseResponse = BaseResponse(
            count = 951,
            next = "https://api.spaceflightnewsapi.net/v4/articles/?has_event=true&has_launch=true&limit=30&news_site=&offset=30&ordering=published_at",
            previous = null,
            results = articles
        )

        doReturn(baseResponse).`when`(apiService).getArticles(
            limit = 30,
            offset = 0,
            titleQuery = "",
            newsSite = "",
            ordering = "published_at"
        )

        val result = remoteDataSource.getArticles(
            limit = 30,
            offset = 0,
            titleQuery = "",
            newsSite = "",
            ordering = "published_at")

        Assert.assertEquals(baseResponse, result)
//        Assert.assertEquals(baseResponse.results.size, result.results.size)
        verify(apiService).getArticles(
            limit = 30,
            offset = 0,
            titleQuery = "",
            newsSite = "",
            ordering = "published_at"
        )
    }
}