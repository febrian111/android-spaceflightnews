package test.febri.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import test.febri.domain.AppDataRepository
import test.febri.domain.model.ArticleModel
import test.febri.domain.util.AppConst.SortOrder
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    operator fun invoke(
        titleQuery: String = "",
        newsSite: String = "",
        sortOrder: SortOrder = SortOrder.SORT_ASC_PUBLISH_DATE
    ): Flow<PagingData<ArticleModel>> =
        repository.getArticlesPagingSource(titleQuery, newsSite, sortOrder)
}