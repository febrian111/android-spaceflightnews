package test.febri.domain.usecase

import kotlinx.coroutines.flow.Flow
import test.febri.domain.AppDataRepository
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BaseNewsModel
import test.febri.domain.model.BlogModel
import test.febri.domain.util.AppConst.SortOrder
import javax.inject.Inject

class GetBlogsUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    operator fun invoke(
        titleQuery: String = "",
        offset: Int = 0,
        newsSite: String = "",
        sortOrder: SortOrder = SortOrder.SORT_DES_PUBLISH_DATE
    ): Flow<BaseNewsModel<BlogModel>> =
        repository.getBlogs(
            offset = offset,
            titleQuery = titleQuery,
            newsSite = newsSite,
            sortOrder = sortOrder)
}