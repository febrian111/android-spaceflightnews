package test.febri.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.febri.data.mapper.toDomain
import test.febri.data.paging.ArticlesPagingSource
import test.febri.data.remote.RemoteDataSource
import test.febri.domain.AppDataRepository
import test.febri.domain.model.ArticleModel
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
}