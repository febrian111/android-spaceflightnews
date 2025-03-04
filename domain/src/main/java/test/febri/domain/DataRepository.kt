package test.febri.domain

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import test.febri.domain.model.ArticleModel

interface DataRepository {
    fun getArticlesPagingSource(): PagingSource<Int, ArticleModel>
}