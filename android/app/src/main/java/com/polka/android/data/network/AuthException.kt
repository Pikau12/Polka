package com.polka.android.data.network

import retrofit2.HttpException
import java.io.IOException

sealed class AuthException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class InvalidCredentials(cause: Throwable? = null) : AuthException("Invalid login or password", cause)
    class LoginTaken(cause: Throwable? = null) : AuthException("This login or email is already taken", cause)
    class InvalidRequest(cause: Throwable? = null) : AuthException("Invalid request", cause)
    class Network(cause: Throwable? = null) : AuthException("No connection to the server", cause)
    class Server(cause: Throwable? = null) : AuthException("Server error, try again later", cause)
    class NotAuthenticated : AuthException("You are not logged in")
    class Unknown(message: String = "Unknown error", cause: Throwable? = null) : AuthException(message, cause)

    override fun toString(): String = message ?: super.toString()
}

internal fun Throwable.toAuthException(): AuthException = when (this) {
    is AuthException -> this
    is HttpException -> when (code()) {
        400 -> AuthException.InvalidRequest(this)
        401 -> AuthException.InvalidCredentials(this)
        409 -> AuthException.LoginTaken(this)
        in 500..599 -> AuthException.Server(this)
        else -> AuthException.Unknown("Unexpected response: ${code()}", this)
    }
    is IOException -> AuthException.Network(this)
    else -> AuthException.Unknown(cause = this)
}