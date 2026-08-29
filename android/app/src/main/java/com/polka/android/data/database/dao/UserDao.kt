package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.polka.android.data.database.model.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserByLogin(login: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET name = :name WHERE id = :userId")
    suspend fun updateUserName(
        userId: Long,
        name: String
    )

    @Query("UPDATE users SET login = :login WHERE id = :userId")
    suspend fun updateUserLogin(
        userId: Long,
        login: String
    )

    @Query("UPDATE users SET avatar = :avatar WHERE id = :userId")
    suspend fun updateUserAvatar(
        userId: Long,
        avatar: String?
    )

    @Delete
    suspend fun deleteUser(user: UserEntity)
}