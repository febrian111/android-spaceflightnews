package test.febri.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BaseNewsModel
import test.febri.domain.model.BlogModel
import test.febri.domain.model.ReportModel
import test.febri.domain.util.AppConst

interface AppDataRepository {
    //    fun getArticlesPagingSource(
//        newsSite: String,
//        sortOrder: AppConst.SortOrder
//    ): Flow<PagingSource<Int, ArticleModel>>
    fun getArticlesPagingSource(
        titleQuery: String,
        newsSite: String,
        sortOrder: AppConst.SortOrder
    ): Flow<PagingData<ArticleModel>>

    fun getArticles(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: AppConst.SortOrder
    ) : Flow<BaseNewsModel<ArticleModel>>

    fun getBlogs(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: AppConst.SortOrder
    ) : Flow<BaseNewsModel<BlogModel>>

    fun getReports(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: AppConst.SortOrder
    ) : Flow<BaseNewsModel<ReportModel>>
}