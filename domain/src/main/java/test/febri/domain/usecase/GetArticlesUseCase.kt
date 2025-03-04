package test.febri.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import test.febri.domain.DataRepository
import test.febri.domain.model.ArticleModel
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: DataRepository
) {
    operator fun invoke(): Flow<PagingData<ArticleModel>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { repository.getArticlesPagingSource() }
        ).flow
    }
}