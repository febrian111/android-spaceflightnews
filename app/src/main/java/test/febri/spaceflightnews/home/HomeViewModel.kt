package test.febri.spaceflightnews.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BlogModel
import test.febri.domain.model.ReportModel
import test.febri.domain.usecase.GetArticlesPagingUseCase
import test.febri.domain.usecase.GetArticlesUseCase
import test.febri.domain.usecase.GetBlogsUseCase
import test.febri.domain.usecase.GetReportsUseCase
import javax.inject.Inject

data class HomeUiState(
    val articles: List<ArticleModel> = emptyList(),
    val blogs: List<BlogModel> = emptyList(),
    val reports: List<ReportModel> = emptyList(),
    val isLoading: Boolean = false
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArticlesPagingUseCase: GetArticlesPagingUseCase,
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBlogsUseCase: GetBlogsUseCase,
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {
    //How many things are we waiting for to load?
    private val numLoadingItems = MutableStateFlow(0)

    private val _articles = MutableStateFlow<List<ArticleModel>>(emptyList())
    val articles: StateFlow<List<ArticleModel>> = _articles

    private val _blogs = MutableStateFlow<List<BlogModel>>(emptyList())
    val blogs: StateFlow<List<BlogModel>> = _blogs

    private val _reports = MutableStateFlow<List<ReportModel>>(emptyList())
    val reports: StateFlow<List<ReportModel>> = _reports

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            // Fetch articles
            getArticlesUseCase()
                .catch { e -> /* Handle error */ }
                .collect { baseArticles ->
                    _articles.value = baseArticles.results
                }
        }
        viewModelScope.launch {
            // Fetch blogs
            getBlogsUseCase()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { baseBlogs ->
                    _blogs.value = baseBlogs.results
                }
        }

        viewModelScope.launch {
            getReportsUseCase()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { baseReports ->
                    _reports.value = baseReports.results
                }
        }
    }

    private suspend fun withLoading(block: suspend () -> Unit) {
        try {
            addLoadingElement()
            block()
        } finally {
            removeLoadingElement()
        }
    }

    private fun addLoadingElement() = numLoadingItems.getAndUpdate { num -> num + 1 }
    private fun removeLoadingElement() = numLoadingItems.getAndUpdate { num -> num - 1 }
}