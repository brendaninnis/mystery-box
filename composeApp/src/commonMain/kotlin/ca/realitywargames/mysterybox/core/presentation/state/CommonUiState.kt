package ca.realitywargames.mysterybox.core.presentation.state

/**
 * Common UI state properties that can be mixed into feature-specific states
 */
data class LoadingState(
    val isLoading: Boolean = false
)

data class ErrorState(
    val error: String? = null
) {
    val hasError: Boolean get() = error != null
}

/**
 * Combines loading and error states for common UI patterns
 */
data class AsyncState(
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasError: Boolean get() = error != null
    val isIdle: Boolean get() = !isLoading && error == null
}

/**
 * Represents async operations that can be in different states
 */
sealed class AsyncResult<out T> {
    data object Loading : AsyncResult<Nothing>()
    data class Success<T>(val data: T) : AsyncResult<T>()
    data class Error(val message: String) : AsyncResult<Nothing>()
    
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

/**
 * Extension functions for easier state management
 */
fun <T> AsyncResult<T>.getDataOrNull(): T? {
    return when (this) {
        is AsyncResult.Success -> data
        else -> null
    }
}

fun <T> AsyncResult<T>.getErrorOrNull(): String? {
    return when (this) {
        is AsyncResult.Error -> message
        else -> null
    }
}
