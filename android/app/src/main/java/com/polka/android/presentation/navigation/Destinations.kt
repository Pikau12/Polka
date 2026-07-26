package com.polka.android.presentation.navigation

sealed class Destination(val route: String) {
    // ===== АВТОРИЗАЦИЯ =====
    object Overview : Destination("overview")
    object Login : Destination("login")
    object Signin : Destination("signin")

    // ===== ОСНОВНЫЕ ЭКРАНЫ =====
    object Collection : Destination("collection")
    object Sessions : Destination("sessions")
    object User : Destination("user")

    // ===== ИГРЫ =====
    object Game : Destination("game")
    object GamesSearch : Destination("games_search")
    object GameCard : Destination("game_card/{gameId}") {
        fun pass(gameId: String) = "game_card/$gameId"
    }

    // ===== СЕССИИ (ПАРТИИ) =====
    object Session : Destination("session")
    object SessionsSearch : Destination("sessions_search")
    object SessionCard : Destination("session_card/{sessionId}") {
        fun pass(sessionId: String) = "session_card/$sessionId"
    }

    // ===== ДРУЗЬЯ =====
    object Friends : Destination("friends")
    object FriendsSearch : Destination("friends_search")
    object UserInfo : Destination("user_info/{userId}") {
        fun pass(userId: String) = "user_info/$userId"
    }
    object UserCollection : Destination("user_collection/{userId}") {
        fun pass(userId: String) = "user_collection/$userId"
    }

    // ===== РЕКОМЕНДАЦИИ =====
    object Recommendations : Destination("recommendations")

    // ===== НАСТРОЙКИ =====
    object Settings : Destination("settings")
    object Account : Destination("account")
    object SupportProject : Destination("support_project")
}