package test.febri.domain.util

sealed class AppResultData<out T> {
    data object Loading : AppResultData<Nothing>()
    data class Success<T>(val data: T) : AppResultData<T>()
    data class Error<T>(val exception: Exception,
        val cachedData: T? = null
    ) : AppResultData<T>()
}