package com.polka.android.presentation.navigation

sealed class Destination(val route: String) {
    // ===== Auth =====
    object Overview : Destination("overview")
    object Login : Destination("login/{showSigninSuccess}"){
        fun pass(showSigninSuccess: Boolean = false) = "login/$showSigninSuccess"
    }
    object Signin : Destination("login/{showSigninFailure}"){
        fun pass(showSigninFailure: Boolean = false) = "login/$showSigninFailure"
    }

    // ===== Core =====
    object CollectionCore : Destination("collection_core")
    object SessionsCore : Destination("sessions_core")
    object User : Destination("user")

    // ===== Games =====
    object CollectionGames : Destination("collection_games")
    object Game : Destination("game/{gameId}"){
        fun pass(gameId: Long) = "game/$gameId"
    }
    object GamesSearch : Destination("games_search")
    object GameCard : Destination("game_card/{gameId}") {
        fun pass(gameId: Int) = "game_card/$gameId"
    }

    // ===== Sessions =====
    object SessionsSessions : Destination("sessions_sessions")
    object Session : Destination("session/{sessionId}"){
        fun pass(sessionId: Int) = "session/$sessionId"
    }
    object SessionsSearch : Destination("sessions_search")
    object SessionCard : Destination("session_card/{gameId}") {
        fun pass(gameId: Long) = "session_card/$gameId"
    }

    // ===== Friends =====
    object Friends : Destination("friends")
    object FriendsSearch : Destination("friends_search")
    object UserInfo : Destination("user_info/{userId}") {
        fun pass(userId: Int) = "user_info/$userId"
    }
    object UserCollection : Destination("user_collection/{userId}") {
        fun pass(userId: Int) = "user_collection/$userId"
    }

    // ===== Recommendation =====
    object Recommendations : Destination("recommendations")

    // ===== Settings =====
    object Settings : Destination("settings")
    object Account : Destination("account")
    object SupportProject : Destination("support_project")

    companion object{
        fun getNextDestinationAfterLogin(userId: Int): String{
            return if (/*AuthRepository.isFirstLogin(userId)*/ true){ // I need to use AuthRepository there, but it is not there right now :(
                Overview.route
            } else{
                CollectionCore.route
            }
        }
    }
}