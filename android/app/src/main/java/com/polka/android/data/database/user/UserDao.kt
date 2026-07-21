package com.polka.android.data.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    // ----- Simple operations -----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    // ----- Receipt requests -----

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT createdAt FROM users WHERE id = :userId")
    suspend fun getUserRegistrationDate(userId: String): Long?

    // ----- Partial updates -----

    @Query("UPDATE users SET username = :newName WHERE id = :userId")
    suspend fun updateUserName(userId: String, newName: String)

    @Query("UPDATE users SET login = :newLogin WHERE id = :userId")
    suspend fun updateUserLogin(userId: String, newLogin: String)

    @Query("UPDATE users SET avatar = :newAvatar WHERE id = :userId")
    suspend fun updateUserAvatar(userId: String, newAvatar: String?)

    // ----- Working with the last logged user -----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastLoggedInUser(lastUser: LastLoggedInUser)

    @Query("SELECT * FROM last_logged_in_user LIMIT 1")
    suspend fun getLastLoggedInUser(): LastLoggedInUser?
}
