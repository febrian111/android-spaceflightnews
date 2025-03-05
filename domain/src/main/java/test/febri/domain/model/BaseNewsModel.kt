package test.febri.domain.model

data class BaseNewsModel<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
