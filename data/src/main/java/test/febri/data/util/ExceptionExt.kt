package test.febri.data.util


import retrofit2.HttpException
import test.febri.domain.util.errorhandler.ApiRateLimitException
import test.febri.domain.util.errorhandler.NetworkException
import test.febri.domain.util.errorhandler.ResourceNotFoundException
import test.febri.domain.util.errorhandler.ServiceUnavailableException
import test.febri.domain.util.errorhandler.SpaceflightException
import test.febri.domain.util.errorhandler.UnauthorizedException
import test.febri.domain.util.errorhandler.UnknownException
import java.io.IOException


fun Exception.mapApiException(): SpaceflightException {
    return when (this) {
        is HttpException -> {
            when (this.code()) {
                401 -> UnauthorizedException()
                403, 429 -> ApiRateLimitException()
                404 -> ResourceNotFoundException()
                500, 502, 503, 504 -> ServiceUnavailableException()
                else -> NetworkException(this)
            }
        }

        is IOException -> NetworkException(this)
        else -> UnknownException(this)
    }
}