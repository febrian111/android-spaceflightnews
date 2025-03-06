package test.febri.spaceflightnews.searchnews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.febri.domain.usecase.GetArticlesUseCase
import test.febri.domain.usecase.GetBlogsUseCase
import test.febri.domain.usecase.GetReportsUseCase
import test.febri.domain.util.AppConst
import test.febri.githubapp.util.NavConstant
import test.febri.spaceflightnews.searchnews.adapter.SearchArticlePagingSource
import test.febri.spaceflightnews.searchnews.adapter.SearchBlogPagingSource
import test.febri.spaceflightnews.searchnews.adapter.SearchReportPagingSource
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getBlogsUseCase: GetBlogsUseCase,
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {

    val searchType =
        savedStateHandle.getStateFlow<String>(NavConstant.NAV_ARGS_SEARCH_TYPE, "News Search")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _newsSite = MutableStateFlow("")
    val newsSite = _newsSite.asStateFlow()

    private val _sortOrder = MutableStateFlow(AppConst.SortOrder.SORT_DES_PUBLISH_DATE)
    val sortOrder = _sortOrder.asStateFlow()

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    private val _refreshTriggerShared = MutableSharedFlow<Unit>(replay = 1)

    val news = combine(
        _refreshTriggerShared.onStart { emit(Unit) },
        searchQuery.debounce(300L),
        newsSite,
        sortOrder
    ) { _, query, newsSite, sortOrder ->
        query to Unit // todo map to an object filter
    }.flatMapLatest { (query, _) ->
        Pager(
            config = PagingConfig(
                pageSize = SearchArticlePagingSource.PAGE_SIZE,
                initialLoadSize = SearchArticlePagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchArticlePagingSource(
                    getArticlesUseCase = getArticlesUseCase,
                    query = query,
                    newsSite = "",
                    sortOrder = AppConst.SortOrder.SORT_DES_PUBLISH_DATE
                )
            }
        ).flow
    }.cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PagingData.empty()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            _refreshTriggerShared.emit(Unit)
            delay(500)
            _refreshing.value = false
        }
    }
}