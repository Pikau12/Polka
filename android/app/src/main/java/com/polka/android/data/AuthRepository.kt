package com.polka.android.data

import jakarta.inject.Inject

interface AuthRepository {
}

class DefaultAuthRepository @Inject constructor() : AuthRepository {
}
