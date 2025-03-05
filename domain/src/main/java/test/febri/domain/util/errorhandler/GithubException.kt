package test.febri.domain.util.errorhandler



sealed class SpaceflightException : Exception() {
    abstract override val message: String
}

class ApiRateLimitException : SpaceflightException() {
    override val message: String = "API rate limit exceeded. Please try again later."
}

class ResourceNotFoundException : SpaceflightException() {
    override val message: String = "The requested resource was not found."
}

class NetworkException(override val cause: Exception) : SpaceflightException() {
    override val message: String = cause.message ?: "Network error occurred. Please check your internet connection."
}

class UnknownException(override val cause: Exception) : SpaceflightException() {
    override val message: String = cause.message ?: "An unexpected error occurred."
}

class UnauthorizedException : SpaceflightException() {
    override val message: String = "Authentication failed. Please check your credentials."
}

class ForbiddenException : SpaceflightException() {
    override val message: String = "You don't have permission to access this resource."
}

class ServiceUnavailableException : SpaceflightException() {
    override val message: String = "Service is temporarily unavailable. Please try again later."
}

// Helper function to get a user-friendly error message
fun Exception.extractMessage(): String {
    return when (this) {
        is SpaceflightException -> message
        else -> "An unexpected error occurred. Please try again."
    }
}
