package test.febri.domain

import androidx.paging.PagingData
import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.Author
import test.febri.domain.model.Socials
import test.febri.domain.usecase.GetArticlesUseCase
import test.febri.domain.util.AppConst

@RunWith(MockitoJUnitRunner::class)
class GetArticlesUseCaseTest {

    private lateinit var getArticlesUseCase: GetArticlesUseCase

    @Mock
    private lateinit var repository: AppDataRepository

    @Before
    fun setup() {
        getArticlesUseCase = GetArticlesUseCase(repository)
    }

    @Test
    fun `invoke returns flow of paging data from repository`() = runTest {

        val fakePagingData = PagingData.from(
            listOf(
                ArticleModel(
                    id = 1,
                    title = "Test Article One",
                    authors = listOf(
                        Author(
                            "John Doe", Socials(
                                x = "https://example.com/x",
                                youtube = "https://example.com/youtube",
                                instagram = "https://example.com/instagram",
                                linkedin = "https://example.com/linkedin",
                                mastodon = "https://example.com/mastodon",
                                bluesky = "https://example.com/bluesky"
                            )
                        )
                    ),
                    url = "https://example.com",
                    imageUrl = "https://example.com/image.jpg",
                    newsSite = "Example News Site",
                    summary = "This is a test article",
                    publishedAt = "2023-08-01T12:00:00Z",
                    updatedAt = "2023-08-01T12:00:00Z",
                    featured = false,
                    launches = emptyList(),
                    events = emptyList()
                ),
                ArticleModel(
                    id = 2,
                    title = "Test Article Two",
                    authors = listOf(
                        Author(
                            "John Doe", Socials(
                                x = "https://example.com/x",
                                youtube = "https://example.com/youtube",
                                instagram = "https://example.com/instagram",
                                linkedin = "https://example.com/linkedin",
                                mastodon = "https://example.com/mastodon",
                                bluesky = "https://example.com/bluesky"
                            )
                        )
                    ),
                    url = "https://example.com",
                    imageUrl = "https://example.com/image.jpg",
                    newsSite = "Example News Site",
                    summary = "This is a test article",
                    publishedAt = "2023-08-01T12:00:00Z",
                    updatedAt = "2023-08-01T12:00:00Z",
                    featured = false,
                    launches = emptyList(),
                    events = emptyList()
                )
            )
        )


        doReturn(flowOf(fakePagingData))
            .`when`(repository)
            .getArticlesPagingSource(
                titleQuery = "",
                newsSite = "",
                sortOrder = AppConst.SortOrder.SORT_ASC_PUBLISH_DATE
            )

        val result = getArticlesUseCase(
            titleQuery = "",
            newsSite = "",
            sortOrder = AppConst.SortOrder.SORT_ASC_PUBLISH_DATE
        )

        result.test {
            Assert.assertEquals(awaitItem(), fakePagingData)
            awaitComplete()
        }
    }
}