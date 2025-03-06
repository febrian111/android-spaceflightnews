package test.febri.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import test.febri.data.mapper.toDomain
import test.febri.data.model.ArticleResponse
import test.febri.data.model.BaseResponse
import test.febri.data.paging.ArticlesPagingSource
import test.febri.data.remote.RemoteDataSource
import test.febri.domain.AppDataRepository
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BaseNewsModel
import test.febri.domain.model.BlogModel
import test.febri.domain.model.ReportModel
import test.febri.domain.util.AppConst.SortOrder
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : AppDataRepository {
    override fun getArticlesPagingSource(
        titleQuery: String,
        newsSite: String,
        sortOrder: SortOrder
    ): Flow<PagingData<ArticleModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ArticlesPagingSource(
                    newsSite = newsSite,
                    sortOrder = sortOrder.value,
                    remoteDataSource = remoteDataSource
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getArticles(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: SortOrder
    ): Flow<BaseNewsModel<ArticleModel>> = flow {

        val response = remoteDataSource.getArticles(
            limit = 30,
            offset = offset,
            titleQuery = titleQuery,
            newsSite = newsSite,
            ordering = sortOrder.value
        )

        emit(with(response) {
            BaseNewsModel(
                count = count,
                next = next,
                previous = previous,
                results = results.map { it.toDomain() })
        })

    }

    override fun getBlogs(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: SortOrder
    ): Flow<BaseNewsModel<BlogModel>> = flow {

        val response = remoteDataSource.getBlogs(
            limit = 30,
            offset = offset,
            titleQuery = titleQuery,
            newsSite = newsSite,
            ordering = sortOrder.value
        )

        emit(with(response) {
            BaseNewsModel(
                count = count,
                next = next,
                previous = previous,
                results = results.map { it.toDomain() })
        })

    }

    override fun getReports(
        offset: Int,
        titleQuery: String,
        newsSite: String,
        sortOrder: SortOrder
    ): Flow<BaseNewsModel<ReportModel>>  = flow {

        val response = remoteDataSource.getReports(
            limit = 30,
            offset = offset,
            titleQuery = titleQuery,
            newsSite = newsSite,
            ordering = sortOrder.value
        )

        emit(with(response) {
            BaseNewsModel(
                count = count,
                next = next,
                previous = previous,
                results = results.map { it.toDomain() })
        })

    }
}