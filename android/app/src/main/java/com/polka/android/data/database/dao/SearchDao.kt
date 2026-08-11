package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query(
        """
        SELECT * FROM users
        WHERE username LIKE '%' || :name || '%'
        ORDER BY username ASC
    """
    )
    fun searchUsersByUsername(name: String): Flow<List<UserEntity>>

    /*
    TODO: after server realization
    fun searchGamesInBgg(): Flow<List<GameEntity>>
     */

    /*
    TODO: after server realization
    fun searchGamesByTagsInBgg(): Flow<List<GameEntity>>
     */

    /*
    TODO:
    fun searchGamesInCollectionByTags()
     */

    @Query(
        """
    SELECT games.* FROM games
    INNER JOIN collection_items ON games.id = collection_items.gameId
    WHERE games.name LIKE '%' || :gameName || '%'
    ORDER BY collection_items.displayOrder ASC
    """
    )
    fun searchGameInCollection(gameName: String): Flow<List<GameEntity>>

    /*
    TODO: mb move this func to collectionItemDao?
     */
    @Query("""
        SELECT * FROM collection_items
        ORDER BY displayOrder ASC
    """)
    fun getSortedUserCollection(): Flow<List<CollectionItemEntity>>

    /*
    TODO: mb move this func to friendshipDao?
     */
    @Query("""
        SELECT users.* FROM users
        INNER JOIN friendships ON users.id = friendships.friendId
        WHERE friendships.userId = :userId
        ORDER BY users.username ASC
    """)
    fun getUserFriends(userId: Long): Flow<List<UserEntity>>
}